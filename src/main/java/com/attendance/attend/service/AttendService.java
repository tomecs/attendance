package com.attendance.attend.service;

import com.attendance.attend.entity.Attend;
import com.attendance.attend.vo.QueryCondition;
import com.attendance.common.page.PageQueryBean;

/**
 * Created by Tomecs.
 */
public interface AttendService {

    void signAttend(Attend attend) throws Exception;

    PageQueryBean listAttend(QueryCondition condition);

    void checkAttend();
}
