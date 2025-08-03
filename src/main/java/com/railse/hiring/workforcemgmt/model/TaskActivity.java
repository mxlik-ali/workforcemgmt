package com.railse.hiring.workforcemgmt.model;

import com.railse.hiring.workforcemgmt.model.enums.ActivityType;
import lombok.Data;
import java.time.Instant;

@Data
public class TaskActivity {
    private Long id;
    private Long taskId;
    private Long userId;
    private String userName;
    private ActivityType activityType;
    private String details;
    private Instant timestamp;
}
