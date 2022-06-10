package com.example.scheduletask.controller;

import com.example.scheduletask.entity.ScheduleTask;
import com.example.scheduletask.enums.TaskStatus;
import com.example.scheduletask.service.ScheduleTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScheduleTaskController {
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @GetMapping("/tasks")
    public List<ScheduleTask> findAllTasks(){
        return scheduleTaskService.findAllTasks();
    }

    /**
     * 新添加的定时任务需要重启服务，修改定时任务等则无须重启服务
     */
    @PostMapping("/task/add")
    public ScheduleTask addTask(@RequestBody ScheduleTask scheduleTask){
        if(!CronExpression.isValidExpression(scheduleTask.getCron())){
            throw new IllegalArgumentException(scheduleTask.getCron() + "不是合法的cron表达式");
        }
        scheduleTask.setStatus(TaskStatus.ENABLED);
        return scheduleTaskService.save(scheduleTask);
    }
    @PutMapping("/task/{id}")
    public ScheduleTask modiftTask(@PathVariable("id") String id,String cron){
        if(!CronExpression.isValidExpression(cron)){
            throw new IllegalArgumentException(cron + "不是合法的cron表达式");
        }
        return scheduleTaskService.update(id,cron);
    }
    @PostMapping("/task/{id}/disable")
    public String disableTask(@PathVariable("id") String id){
        return scheduleTaskService.disableTaskById(id);
    }
    @PostMapping("/task/{id}/enable")
    public String enbaleTask(@PathVariable("id") String id){
        return scheduleTaskService.enableTaskById(id);
    }
}
