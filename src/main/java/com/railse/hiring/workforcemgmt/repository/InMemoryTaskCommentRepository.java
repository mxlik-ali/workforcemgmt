package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.TaskComment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
@Repository
public class InMemoryTaskCommentRepository implements TaskCommentRepository {
    private final Map<Long, TaskComment> commentStore = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public TaskComment save(TaskComment comment) {
        if (comment.getId() == null) {
            comment.setId(idGenerator.getAndIncrement());
        }
        commentStore.put(comment.getId(), comment);
        return comment;
    }

    @Override
    public List<TaskComment> findByTaskId(Long taskId) {
        return commentStore.values().stream()
                .filter(comment -> Objects.equals(comment.getTaskId(), taskId))
                .sorted(Comparator.comparing(TaskComment::getTimestamp))
                .collect(Collectors.toList());
    }
}
