package com.railse.hiring.workforcemgmt.model;

import lombok.Data;
import java.time.Instant;

@Data
public class TaskComment {
    private Long id;
    private Long taskId;
    private Long userId;
    private String userName;
    private String text;
    private Instant timestamp;
}
