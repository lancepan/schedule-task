package com.example.scheduletask.repository;

import com.example.scheduletask.entity.ScheduleTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface ScheduleTaskRepository extends JpaRepository<ScheduleTask, Serializable> {
}
