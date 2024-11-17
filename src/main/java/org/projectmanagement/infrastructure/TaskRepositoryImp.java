package org.projectmanagement.infrastructure;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.repository.TasksRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Repository

public class TaskRepositoryImp implements TasksRepository {

    //Replace with actual implementation for jpa
    private InMemoryDatabase inMemoryDatabase;

    public TaskRepositoryImp(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public Tasks save(Tasks task) {
        //Replace with actual implementation for jpa
        Tasks save;
        int index = IntStream.range(0, inMemoryDatabase.getTasks().size())
                .filter(i -> inMemoryDatabase.getTasks().get(i).getId() != null &&
                        inMemoryDatabase.getTasks().get(i).getId().equals(task.getId())
                )
                .findFirst()
                .orElse(-1);
        if (index != -1) {
            save = inMemoryDatabase.getTasks().set(index, task);
        } else {
            save = task.toBuilder().id(UUID.randomUUID()).build();
            inMemoryDatabase.getTasks().add(save);
        }
        return save;
    }

    public List<Tasks> getTasksByProjectId(UUID projectId) {
        return inMemoryDatabase.getTasks().stream().filter(task -> task.getProjectId().equals(projectId)).toList();
    }

    public List<Tasks> getTasks() {
        return inMemoryDatabase.getTasks();
    }

    @Override
    public Tasks findOne(UUID taskId) {
        return inMemoryDatabase.getTasks().stream().filter(task -> task.getId().equals(taskId)).findFirst().orElse(null);
    }

    public Tasks getTask(UUID taskId) {
        return null;
    }
}
