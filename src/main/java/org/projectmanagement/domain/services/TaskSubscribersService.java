package org.projectmanagement.domain.services;

import org.springframework.stereotype.Repository;


public interface TaskSubscribersService {

    boolean subscribeToTask(String taskId);

    boolean unsubscribeToTask(String taskId);
}
