package com.railse.hiring.workforcemgmt.controller;

import com.railse.hiring.workforcemgmt.dto.TaskCommentDto;
import com.railse.hiring.workforcemgmt.dto.TaskActivityDto;
import com.railse.hiring.workforcemgmt.service.TaskHistoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/tasks/{taskId}")
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;

    public TaskHistoryController(TaskHistoryService taskHistoryService) {
        this.taskHistoryService = taskHistoryService;
    }

    @PostMapping("/comments")
    public ResponseEntity<TaskCommentDto> addComment(
            @PathVariable Long taskId,
            @RequestBody Map<String, String> request
    ) {
        Long userId = Long.valueOf(request.get("userId"));
        String userName = request.get("userName");
        String text = request.get("text");
        TaskCommentDto dto = taskHistoryService.addComment(taskId, userId, userName, text);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/activities")
    public ResponseEntity<TaskActivityDto> addActivity(
            @PathVariable Long taskId,
            @RequestBody Map<String, String> request
    ) {
        Long userId = Long.valueOf(request.get("userId"));
        String userName = request.get("userName");
        String activityType = request.get("activityType");
        String details = request.get("details");
        TaskActivityDto dto = taskHistoryService.addActivity(taskId, userId, userName, activityType, details);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}