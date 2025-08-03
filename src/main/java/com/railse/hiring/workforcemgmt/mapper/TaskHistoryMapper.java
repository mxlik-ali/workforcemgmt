package com.railse.hiring.workforcemgmt.mapper;

import com.railse.hiring.workforcemgmt.model.TaskComment;
import com.railse.hiring.workforcemgmt.dto.TaskCommentDto;
import com.railse.hiring.workforcemgmt.model.TaskActivity;
import com.railse.hiring.workforcemgmt.dto.TaskActivityDto;

public class TaskHistoryMapper {

    public static TaskCommentDto toCommentDto(TaskComment comment) {
        TaskCommentDto dto = new TaskCommentDto();
        dto.setUserName(comment.getUserName());
        dto.setText(comment.getText());
        dto.setTimestamp(comment.getTimestamp());
        return dto;
    }

    public static TaskActivityDto toActivityDto(TaskActivity activity) {
        TaskActivityDto dto = new TaskActivityDto();
        dto.setUserId(activity.getUserId());
        dto.setUserName(activity.getUserName());
        dto.setTaskId(activity.getTaskId());
        dto.setActivityType(activity.getActivityType());
        dto.setDetails(activity.getDetails());
        dto.setTimestamp(activity.getTimestamp());
        return dto;
    }
}