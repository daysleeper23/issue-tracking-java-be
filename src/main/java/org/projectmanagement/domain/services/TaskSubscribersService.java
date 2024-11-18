package org.projectmanagement.domain.services;

import org.springframework.stereotype.Repository;


public interface TaskSubscribersService {

    boolean subscribeToTask(String taskId, String userId);

    boolean unsubscribeToTask(String taskId, String userId);
}
