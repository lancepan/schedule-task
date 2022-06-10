package com.example.scheduletask.task;

import com.example.scheduletask.entity.ScheduleTask;
import com.example.scheduletask.enums.TaskStatus;
import com.example.scheduletask.service.ScheduleTaskService;
import com.example.scheduletask.utils.SpringUtils;

public interface IScheduleTask extends Runnable{
    /**
     * 任务内容
     */
    void execute();


    @Override
    default void run(){
        //定时任务执行时其实执行的run方法，execute方法为我们真正的任务体，在run方法被定时调用时实时查询数据库任务状态来决定是否调用
        //execute方法，从而控制定时任务的开关
        ScheduleTaskService ScheduleTaskService = SpringUtils.getBean(ScheduleTaskService.class);
        ScheduleTask scheduleTask = ScheduleTaskService.findByTaskClassPath(this.getClass().getName());
        if(scheduleTask.getStatus() != null && TaskStatus.ENABLED.equals(scheduleTask.getStatus())){
            execute();
        }
    }
}
