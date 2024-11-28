package org.projectmanagement.application.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.TaskSubscribers;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.TasksRepository;
import org.projectmanagement.domain.repository.TaskSubscribersRepository;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.services.TaskSubscribersService;
import org.projectmanagement.presentation.config.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskSubscribersServiceImpl implements TaskSubscribersService {

    private final TasksRepository tasksRepository;
    private final TaskSubscribersRepository subscribersRepository;
    private final UsersRepository usersRepository;


    @Override
    public boolean subscribeToTask(String taskId) {
        UserDetails user = SecurityUtils.getCurrentUser();
        Users users = usersRepository.findOneByEmail(user.getUsername())
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND));
        if (tasksRepository.findById(UUID.fromString(taskId)) == null){
            throw new ApplicationException(AppMessage.TASK_NOT_FOUND);
        }
        TaskSubscribers sub = new TaskSubscribers(UUID.fromString(taskId),users.getId());
        subscribersRepository.save(sub);
        return true;
    }

    @Transactional
    @Override
    public boolean unsubscribeToTask(String taskId) {
        UserDetails user = SecurityUtils.getCurrentUser();
        Users users = usersRepository.findOneByEmail(user.getUsername())
                .orElseThrow(() -> new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND));
        if (subscribersRepository.getSubscriberByTaskIdAndUserId(UUID.fromString(taskId), users.getId()) != null){
            return subscribersRepository.deleteByTaskIdAndUserId(UUID.fromString(taskId), users.getId()) > 0;
        }
        return false;
    }
}
