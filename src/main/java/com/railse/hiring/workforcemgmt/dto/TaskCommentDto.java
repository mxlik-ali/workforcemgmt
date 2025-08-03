package com.railse.hiring.workforcemgmt.dto;

import java.time.Instant;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskCommentDto {
    private String userName;
    private String text;
    private Instant timestamp;
}
