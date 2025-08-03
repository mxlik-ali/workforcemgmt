package com.railse.hiring.workforcemgmt.dto;

import java.util.List;

public class TaskDetailsDto {
    private TaskManagementDto task;
    private List<TaskCommentDto> comments;
    private List<TaskActivityDto> activities;

    // Getters and setters
    public TaskManagementDto getTask() { return task; }
    public void setTask(TaskManagementDto task) { this.task = task; }

    public List<TaskCommentDto> getComments() { return comments; }
    public void setComments(List<TaskCommentDto> comments) { this.comments = comments; }

    public List<TaskActivityDto> getActivities() { return activities; }
    public void setActivities(List<TaskActivityDto> activities) { this.activities = activities; }
}
