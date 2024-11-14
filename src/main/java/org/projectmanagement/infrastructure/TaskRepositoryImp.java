package org.projectmanagement.infrastructure;

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
    List<Tasks> db = new ArrayList<>();

    public Tasks save(Tasks task) {
        //Replace with actual implementation for jpa
         int index =IntStream.range(0, db.size()).filter(i -> db.get(i).getId().equals(task.getId()))
                .findFirst().orElse(-1);
         if (index != -1) {
             db.set(index, task);
         } else {
             db.add(task);
         }
        return task;
    }

    public List<Tasks> getTasksByProjectId(UUID projectId) {
        return db.stream().filter(task -> task.getProjectId().equals(projectId)).toList();
    }

    public List<Tasks> getTasks() {
        return db;
    }

    @Override
    public Tasks findOne(UUID taskId) {
        return db.stream().filter(task -> task.getId().equals(taskId)).findFirst().orElse(null);
    }

    public Tasks getTask(UUID taskId) {
        return null;
    }
}
