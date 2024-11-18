package org.projectmanagement.infrastructure;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.repository.TaskSubscribersRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class TaskSubscribersRepositoryImpl {

    private final InMemoryDatabase inMemoryDatabase;


    public boolean save(TaskSubscribers sub) {
        return inMemoryDatabase.getTaskSubscribers().add(sub);
    }


    public List<TaskSubscribers> getSubscriberByTaskId(UUID taskId) {
        return inMemoryDatabase.getTaskSubscribers().stream().filter(sub -> sub.getTaskId().equals(taskId)).toList();
    }


    public TaskSubscribers getSubscriberByTaskIdAndUserId(UUID taskId, UUID userId) {
        return inMemoryDatabase.getTaskSubscribers().stream()
                .filter(sub -> sub.getTaskId().equals(taskId) && sub.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }


    public boolean deleteOne(UUID taskId, UUID userId) {
        TaskSubscribers subscribers =  inMemoryDatabase.getTaskSubscribers().stream().filter(sub ->
                sub.getTaskId().equals( sub.getTaskId()) && sub.getUserId().equals(userId)).findFirst().orElse(null);
        if (subscribers == null) return false;
        return inMemoryDatabase.getTaskSubscribers().remove(subscribers);
    }
}
