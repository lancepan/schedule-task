package com.example.scheduletask.entity;

import com.example.scheduletask.enums.TaskStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
//一个IScheduleTask实现，只能对应一个定时任务
@Table(indexes = @Index(name = "task_class_path",columnList = "taskClassPath",unique = true))
public class ScheduleTask {
    @Id
    @GenericGenerator(name = "my-uuid",strategy = "uuid")
    @GeneratedValue(generator = "my-uuid")
    private String id;
    private String taskClassPath;
    private String cron;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
