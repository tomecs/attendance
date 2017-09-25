package com.attendance.common.task;

import com.attendance.attend.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Tomecs.
 */
public class AttendCheckTask {

    @Autowired
    private AttendService attendService;

    public  void checkAttend(){

        attendService.checkAttend();

    }
}
