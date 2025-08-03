package com.railse.hiring.workforcemgmt.service;

import com.railse.hiring.workforcemgmt.dto.TaskCommentDto;
import com.railse.hiring.workforcemgmt.dto.TaskActivityDto;
import java.util.List;

public interface TaskHistoryService {
    TaskCommentDto addComment(Long taskId, Long userId, String userName, String text);
    TaskActivityDto logActivity(Long taskId, Long userId, String userName,
                                com.railse.hiring.workforcemgmt.model.enums.ActivityType activityType, String details);
    List<TaskCommentDto> getCommentsForTask(Long taskId);
    List<TaskActivityDto> getActivitiesForTask(Long taskId);
    TaskActivityDto addActivity(Long taskId, Long userId, String userName, String activityType, String details);
}