package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.TaskActivity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryTaskActivityRepository implements TaskActivityRepository {
    private final Map<Long, TaskActivity> activityStore = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public TaskActivity save(TaskActivity activity) {
        if (activity.getId() == null) {
            activity.setId(idGenerator.getAndIncrement());
        }
        activityStore.put(activity.getId(), activity);
        return activity;
    }

    @Override
    public List<TaskActivity> findByTaskId(Long taskId) {
        return activityStore.values().stream()
                .filter(activity -> Objects.equals(activity.getTaskId(), taskId))
                .sorted(Comparator.comparing(TaskActivity::getTimestamp))
                .collect(Collectors.toList());
    }
}
