package com.railse.hiring.workforcemgmt.service.impl;


import com.railse.hiring.workforcemgmt.common.exception.ResourceNotFoundException;
import com.railse.hiring.workforcemgmt.dto.*;
import com.railse.hiring.workforcemgmt.mapper.ITaskManagementMapper;
import com.railse.hiring.workforcemgmt.model.TaskManagement;
import com.railse.hiring.workforcemgmt.model.enums.Task;
import com.railse.hiring.workforcemgmt.model.enums.TaskStatus;
import com.railse.hiring.workforcemgmt.repository.TaskRepository;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TaskManagementServiceImpl implements TaskManagementService {


    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;


    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }


    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.modelToDto(task);
    }


    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagement> createdTasks = new ArrayList<>();
        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskStartTime(System.currentTimeMillis());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");
            createdTasks.add(taskRepository.save(newTask));
        }
        return taskMapper.modelListToDtoList(createdTasks);
    }


    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();
        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));


            if (item.getTaskStatus() != null) {
                task.setStatus(item.getTaskStatus());
            }
            if (item.getDescription() != null) {
                task.setDescription(item.getDescription());
            }
            updatedTasks.add(taskRepository.save(task));
        }
        return taskMapper.modelListToDtoList(updatedTasks);
    }

//    @Override
//    public String assignByReference(AssignByReferenceRequest request) {
//        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
//        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());
//
//
//        for (Task taskType : applicableTasks) {
//            List<TaskManagement> tasksOfType = existingTasks.stream()
//                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
//                    .collect(Collectors.toList());
//
//
//            // BUG #1 is here. It should assign one and cancel the rest.
//            // Instead, it reassigns ALL of them.
//            if (!tasksOfType.isEmpty()) {
//                for (TaskManagement taskToUpdate : tasksOfType) {
//                    taskToUpdate.setAssigneeId(request.getAssigneeId());
//                    taskRepository.save(taskToUpdate);
//                }
//            } else {
//                // Create a new task if none exist
//                TaskManagement newTask = new TaskManagement();
//                newTask.setReferenceId(request.getReferenceId());
//                newTask.setReferenceType(request.getReferenceType());
//                newTask.setTask(taskType);
//                newTask.setAssigneeId(request.getAssigneeId());
//                newTask.setStatus(TaskStatus.ASSIGNED);
//                taskRepository.save(newTask);
//            }
//        }
//        return "Tasks assigned successfully for reference " + request.getReferenceId();
//    }


    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            // Find all tasks of this type and reference that are not completed
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .collect(Collectors.toList());

            boolean alreadyAssigned = false;
            for (TaskManagement oldTask : tasksOfType) {
                if (oldTask.getAssigneeId().equals(request.getAssigneeId()) && oldTask.getStatus() == TaskStatus.ASSIGNED) {
                    // The new assignee already has an assigned task, do nothing
                    alreadyAssigned = true;
                } else if (oldTask.getStatus() == TaskStatus.ASSIGNED) {
                    // Cancel any other ASSIGNED tasks
                    oldTask.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(oldTask);
                }
            }

            if (!alreadyAssigned) {
                // Create a new ASSIGNED task for the new assignee
                TaskManagement templateTask = tasksOfType.isEmpty() ? null : tasksOfType.get(0);
                TaskManagement newTask = new TaskManagement();
                newTask.setReferenceId(request.getReferenceId());
                newTask.setReferenceType(request.getReferenceType());
                newTask.setTask(taskType);
                newTask.setAssigneeId(request.getAssigneeId());
                newTask.setStatus(TaskStatus.ASSIGNED);
                newTask.setDescription("Task reassigned to new assignee.");
                newTask.setTaskStartTime(System.currentTimeMillis());
                if (templateTask != null) {
                    newTask.setPriority(templateTask.getPriority());
                    newTask.setTaskDeadlineTime(templateTask.getTaskDeadlineTime());
                }
                taskRepository.save(newTask);
            }
        }
        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }


    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task ->
                        !TaskStatus.CANCELLED.equals(task.getStatus()) &&
                                !TaskStatus.COMPLETED.equals(task.getStatus()) &&
                                (
                                        (task.getTaskStartTime() >= request.getStartDate() && task.getTaskStartTime() <= request.getEndDate())||
                                                (task.getTaskStartTime() < request.getStartDate())
                                )
                )
                .collect(Collectors.toList());


        return taskMapper.modelListToDtoList(filteredTasks);
    }
}


