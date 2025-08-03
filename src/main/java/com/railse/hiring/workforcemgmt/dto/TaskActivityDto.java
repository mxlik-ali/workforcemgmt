package com.railse.hiring.workforcemgmt.dto;

import java.time.Instant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.railse.hiring.workforcemgmt.model.enums.ActivityType;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskActivityDto {
    private Long userId;
    private String userName;
    private Long taskId;
    private ActivityType activityType; // If you want to keep using enum
    // private String activityType;    // If you want to allow custom strings instead
    private String details;
    private Instant timestamp;
}
