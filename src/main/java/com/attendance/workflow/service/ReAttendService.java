package com.attendance.workflow.service;

import com.attendance.workflow.entity.ReAttend;

import java.util.List;

/**
 * Created by Tomecs.
 */
public interface ReAttendService {

    void startReAttendFlow(ReAttend reAttend);

    List<ReAttend> listTasks(String userName);

    void approve(ReAttend reAttend);

    List<ReAttend> listReAttend(String username);
}
