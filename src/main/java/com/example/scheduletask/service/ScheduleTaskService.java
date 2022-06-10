package com.example.scheduletask.service;

import com.example.scheduletask.entity.ScheduleTask;
import com.example.scheduletask.enums.TaskStatus;
import com.example.scheduletask.repository.ScheduleTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleTaskService {
    @Autowired
    private ScheduleTaskRepository scheduleTaskRepository;

    public ScheduleTask save(ScheduleTask scheduleTask){
        try {
            Class.forName(scheduleTask.getTaskClassPath());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("无定时任务实现类：" + scheduleTask.getTaskClassPath());
        }
        ScheduleTask target = this.findByTaskClassPath(scheduleTask.getTaskClassPath());
        Assert.isNull(target, "已存在" + scheduleTask.getTaskClassPath() + "的定时任务");
        return scheduleTaskRepository.save(scheduleTask);
    }

    public List<ScheduleTask> findAllTasks(){
        return scheduleTaskRepository.findAll();
    }
    public ScheduleTask findByTaskClassPath(String taskClassPath){
        ScheduleTask scheduleTaskExample = new ScheduleTask();
        scheduleTaskExample.setTaskClassPath(taskClassPath);
        Example<ScheduleTask> example = Example.of(scheduleTaskExample);
        Optional<ScheduleTask> optionalScheduleTask = scheduleTaskRepository.findOne(example);
        if(optionalScheduleTask.isPresent()){
            return optionalScheduleTask.get();
        }
        return null;
    }
    public ScheduleTask update(String id,String cron){
        ScheduleTask target = this.findById(id);
        Assert.notNull(target, "不存在id为" + id + "的定时任务");
        target.setCron(cron);
       return scheduleTaskRepository.save(target);
    }

    public ScheduleTask findById(String id){
        Optional<ScheduleTask> optionalScheduleTask = scheduleTaskRepository.findById(id);
        if(optionalScheduleTask.isPresent()){
            return optionalScheduleTask.get();
        }
        return null;
    }
    public String disableTaskById(String id){
        modifyTaskStatusById(id,TaskStatus.DISABLED);
        return "已禁用任务" + id;
    }
    public String enableTaskById(String id){
        modifyTaskStatusById(id,TaskStatus.ENABLED);
        return "已启用任务" + id;
    }
    public void modifyTaskStatusById(String id,TaskStatus status){
        ScheduleTask target = this.findById(id);
        Assert.notNull(target, "不存在id为" + id+ "的定时任务");
        target.setStatus(status);
        scheduleTaskRepository.save(target);
    }
}
