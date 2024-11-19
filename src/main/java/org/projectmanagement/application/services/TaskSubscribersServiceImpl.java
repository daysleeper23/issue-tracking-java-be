package org.projectmanagement.application.services;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.exception.AppMessage;
import org.projectmanagement.application.exception.ApplicationException;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.repository.TasksRepository;
import org.projectmanagement.domain.repository.TaskSubscribersRepository;
import org.projectmanagement.domain.services.TaskSubscribersService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskSubscribersServiceImpl implements TaskSubscribersService {

    private final TasksRepository tasksRepository;
    private final TaskSubscribersRepository subscribersRepository;


    @Override
    public boolean subscribeToTask(String taskId, String userId) {
        if (tasksRepository.findById(UUID.fromString(taskId)) == null){
            throw new ApplicationException(AppMessage.TASK_NOT_FOUND);
        }
        TaskSubscribers sub = new TaskSubscribers(UUID.fromString(taskId),UUID.fromString(userId));
        subscribersRepository.save(sub);
        return true;
    }

    @Override
    public boolean unsubscribeToTask(String taskId, String userId) {
        if (subscribersRepository.getSubscriberByTaskIdAndUserId(UUID.fromString(taskId), UUID.fromString(userId)) != null){
            return subscribersRepository.deleteByTaskIdAndUserId(UUID.fromString(taskId), UUID.fromString(userId));
        }
        return false;
    }
}
