package com.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskManager {
    TaskRepo repo;

    @Autowired
    public TaskManager(TaskRepo repo) {
        this.repo = repo;
    }

    public List<Task> findAll() {
        return repo.findAll();
    }

    public Task save(Task task) {
        return repo.save(task);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public void update(Task task) {
        Task tempTask=repo.findById(task.getId()).get();
        if(tempTask!=null) {
            tempTask.copyObject(task);
            repo.save(tempTask);
        }
    }
}
