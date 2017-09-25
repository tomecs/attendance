package com.attendance.attend.service;

import com.attendance.attend.dao.AttendMapper;
import com.attendance.attend.entity.Attend;
import com.attendance.attend.vo.QueryCondition;
import com.attendance.common.page.PageQueryBean;
import com.attendance.common.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tomecs.
 */
@Service("attendServiceImpl")
public class AttendServiceImpl implements AttendService{

    /**
     * 中午十二点 判定上下午
     */
    private static final int NOON_HOUR = 12 ;
    private static final int NOON_MINUTE = 00 ;

    /**
     * 早晚上班时间判定
     */
    private static final int MORNING_HOUR = 9;
    private static final int MORNING_MINUTE = 30;
    private static final int EVENING_HOUR = 18;
    private static final int EVENING_MINUTE = 30;

    /**
     * 缺勤一整天
     */
    private static final Integer ABSENCE_DAY =480 ;
    /**
     * 考勤异常状态
     */
    private static final Byte ATTEND_STATUS_ABNORMAL = 2;
    private static final Byte ATTEND_STATUS_NORMAL = 1;


    private Log log = LogFactory.getLog(AttendServiceImpl.class);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private AttendMapper attendMapper;

    @Override
    @Transactional
    public void signAttend(Attend attend) throws Exception {
        try {
            Date today = new Date();
            attend.setAttendDate(today);
            attend.setAttendWeek((byte)DateUtils.getTodayWeek());
            Attend todayRecord=attendMapper.selectTodaySignRecord(attend.getUserId());
            Date noon = DateUtils.getDate(NOON_HOUR,NOON_MINUTE);
            Date morningAttend = DateUtils.getDate(MORNING_HOUR,MORNING_MINUTE);
            if(todayRecord==null){
                if(today.compareTo(noon)<=0){
                    attend.setAttendMoring(today);
                    if(today.compareTo(morningAttend)>0){
                        attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        attend.setAbsence(DateUtils.getMunite(morningAttend,today));
                    }

                }else {
                    attend.setAttendEvening(today);
                }
                attendMapper.insertSelective(attend);
            }else{
                if(today.compareTo(noon)<=0){
                    return;
                }else {
                    todayRecord.setAttendEvening(today);
                    Date eveningAttend = DateUtils.getDate(EVENING_HOUR,EVENING_MINUTE);
                    if(today.compareTo(eveningAttend)<0){
                        todayRecord.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        todayRecord.setAbsence(DateUtils.getMunite(today,eveningAttend));
                    }else {
                        todayRecord.setAttendStatus(ATTEND_STATUS_NORMAL);
                        todayRecord.setAbsence(0);
                    }
                    attendMapper.updateByPrimaryKeySelective(todayRecord);
                }
            }

        }catch (Exception e){
            log.error("用户签到异常",e);
            throw e;
        }

    }

    @Override
    public PageQueryBean listAttend(QueryCondition condition) {
        int count = attendMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if(count>0){
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            List<Attend> attendList =  attendMapper.selectAttendPage(condition);
            pageResult.setItems(attendList);
        }
        return pageResult;
    }

    /**
     * @Description: 检查考勤异常数据
     */
    @Override
    @Transactional
    public void checkAttend() {
        List<Long> userIdList =attendMapper.selectTodayAbsence();
        if(CollectionUtils.isNotEmpty(userIdList)){
            List<Attend> attendList = new ArrayList<>();
            for(Long userId:userIdList){
                Attend attend = new Attend();
                attend.setUserId(userId);
                attend.setAttendDate(new Date());
                attend.setAttendWeek((byte)DateUtils.getTodayWeek());
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendList.add(attend);
            }
            attendMapper.batchInsert(attendList);
        }
        List<Attend> absenceList = attendMapper.selectTodayEveningAbsence();
        if(CollectionUtils.isNotEmpty(absenceList)){
            for(Attend attend : absenceList){
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendMapper.updateByPrimaryKeySelective(attend);
            }
        }

    }


}
