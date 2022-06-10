package com.example.scheduletask.config;

import com.example.scheduletask.entity.ScheduleTask;
import com.example.scheduletask.service.ScheduleTaskService;
import com.example.scheduletask.task.IScheduleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;

import java.util.List;

@Configuration
@EnableScheduling //开启定时任务支持
public class ScheduleTaskConfig implements SchedulingConfigurer {
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<ScheduleTask> scheduleTaskList = scheduleTaskService.findAllTasks();
        scheduleTaskList.forEach(e -> {
            try {
                //spring启动时，注册数据库中全部定时任务
                Class clazz = Class.forName(e.getTaskClassPath());
                Assert.isAssignable(IScheduleTask.class, clazz,e.getTaskClassPath() + "没有实现定时任务接口IScheduleTask");
                Runnable runnable = (Runnable) clazz.newInstance();
                //不使用addCronTask，而使用addTriggerTask注册，触发器内部则实现实时重新计算触发时间的逻辑，就可以实现修改数据库，从而修改定时任务时间
                taskRegistrar.addTriggerTask(runnable, triggerContext -> {
                    String actualCron = scheduleTaskService.findByTaskClassPath(e.getTaskClassPath()).getCron();
                    return new CronTrigger(actualCron).nextExecutionTime(triggerContext);
                });
            } catch (ClassNotFoundException classNotFoundException) {
                throw new IllegalArgumentException("无定时任务实现类：" + e.getTaskClassPath());
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            } catch (InstantiationException instantiationException) {
                instantiationException.printStackTrace();
            } catch (IllegalArgumentException ie){
                ie.printStackTrace();
            }
        });
    }
}
