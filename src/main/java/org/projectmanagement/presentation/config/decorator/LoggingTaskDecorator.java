package org.projectmanagement.presentation.config.decorator;

import lombok.extern.log4j.Log4j2;

import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

@Log4j2
public class LoggingTaskDecorator implements TaskDecorator {
    @Override

    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        return ()->{
            log.info("Starting a new task");
            runnable.run();
            log.info("Task finished");
        };
    }
}
