package org.projectmanagement.infrastructure;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.repository.TaskSubscribersRepository;
import org.projectmanagement.domain.repository.jpa.TaskSubscribersJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class TaskSubscribersRepositoryImpl implements TaskSubscribersRepository {

    private TaskSubscribersJpaRepository jpaRepository;

    @Override
    public TaskSubscribers save(TaskSubscribers taskSubscribers) {
        return jpaRepository.save(taskSubscribers);
    }

    public List<TaskSubscribers> getSubscriberByTaskId(UUID taskId) {
        return jpaRepository.findByTaskId(taskId);
    }


    public TaskSubscribers getSubscriberByTaskIdAndUserId(UUID taskId, UUID userId) {
        return jpaRepository.findByTaskIdAndUserId(taskId, userId);
    }

    @Override
    public boolean deleteByTaskIdAndUserId(UUID taskId, UUID userId) {
        return jpaRepository.deleteByTaskIdAndUserId(taskId, userId);
    }

}
