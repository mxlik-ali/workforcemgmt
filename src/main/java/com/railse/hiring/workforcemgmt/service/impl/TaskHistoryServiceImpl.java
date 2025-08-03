package com.railse.hiring.workforcemgmt.service.impl;

import com.railse.hiring.workforcemgmt.repository.TaskCommentRepository;
import com.railse.hiring.workforcemgmt.repository.TaskActivityRepository;
import com.railse.hiring.workforcemgmt.model.TaskComment;
import com.railse.hiring.workforcemgmt.model.TaskActivity;
import com.railse.hiring.workforcemgmt.dto.TaskCommentDto;
import com.railse.hiring.workforcemgmt.dto.TaskActivityDto;
import com.railse.hiring.workforcemgmt.model.enums.ActivityType;
import com.railse.hiring.workforcemgmt.mapper.TaskHistoryMapper;
import com.railse.hiring.workforcemgmt.service.TaskHistoryService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class TaskHistoryServiceImpl implements TaskHistoryService {

    private final TaskCommentRepository commentRepo;
    private final TaskActivityRepository activityRepo;

    public TaskHistoryServiceImpl(TaskCommentRepository commentRepo, TaskActivityRepository activityRepo) {
        this.commentRepo = commentRepo;
        this.activityRepo = activityRepo;
    }

    @Override
    public TaskCommentDto addComment(Long taskId, Long userId, String userName, String text) {
        TaskComment comment = new TaskComment();
        comment.setTaskId(taskId);
        comment.setUserId(userId);
        comment.setUserName(userName);
        comment.setText(text);
        comment.setTimestamp(Instant.now());
        commentRepo.save(comment);

        // Also log as activity
        logActivity(taskId, userId, userName, ActivityType.COMMENT_ADDED, text);

        return TaskHistoryMapper.toCommentDto(comment);
    }

    @Override
    public TaskActivityDto logActivity(Long taskId, Long userId, String userName, ActivityType type, String details) {
        TaskActivity activity = new TaskActivity();
        activity.setTaskId(taskId);
        activity.setUserId(userId);
        activity.setUserName(userName);
        activity.setActivityType(type);
        activity.setDetails(details);
        activity.setTimestamp(Instant.now());
        activityRepo.save(activity);
        return TaskHistoryMapper.toActivityDto(activity);
    }

    @Override
    public List<TaskCommentDto> getCommentsForTask(Long taskId) {
        return commentRepo.findByTaskId(taskId)
                .stream()
                .map(TaskHistoryMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskActivityDto> getActivitiesForTask(Long taskId) {
        return activityRepo.findByTaskId(taskId)
                .stream()
                .map(TaskHistoryMapper::toActivityDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskActivityDto addActivity(Long taskId, Long userId, String userName, String activityType, String details) {
        ActivityType type;
        try {
            type = ActivityType.valueOf(activityType);
        } catch (IllegalArgumentException e) {
            type = ActivityType.CUSTOM; // Or your fallback/default type
        }
        return logActivity(taskId, userId, userName, type, details);
    }
}
