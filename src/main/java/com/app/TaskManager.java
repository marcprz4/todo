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

    @Modifying
    @Query("update Task t set t.done=?1, t.important=?2, t.text=?3 where t.id=?4")
    public void updateById(Boolean done, Boolean important, String text, Long id) {}
}
