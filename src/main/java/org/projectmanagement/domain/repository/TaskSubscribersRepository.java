package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.TaskSubscribers;

import java.util.List;
import java.util.UUID;

public interface TaskSubscribersRepository {
    boolean save(TaskSubscribers taskSubscribers);

    List<TaskSubscribers> getSubscriberByTaskId(UUID taskId);

    TaskSubscribers getSubscriberByTaskIdAndUserId(UUID taskId, UUID userId);

    boolean deleteOne(UUID taskId, UUID userId);
}
