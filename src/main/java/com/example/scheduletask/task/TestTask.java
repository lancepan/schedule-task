package com.example.scheduletask.task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTask implements IScheduleTask{
    @Override
    public void execute() {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
