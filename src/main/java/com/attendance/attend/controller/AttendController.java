package com.attendance.attend.controller;

import com.attendance.attend.entity.Attend;
import com.attendance.attend.service.AttendService;
import com.attendance.attend.vo.QueryCondition;
import com.attendance.common.page.PageQueryBean;
import com.attendance.user.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Tomacs.
 */
@Controller
@RequestMapping("attend")
public class AttendController {

    @Autowired
    private AttendService attendService;

    @RequestMapping
    public String toAttend(){

        return "attend";
    }


    /**
     *@Description 签到
     */
    @RequestMapping("/sign")
    @ResponseBody
    public String signAttend(@RequestBody Attend attend, HttpSession session) throws Exception {

      attendService.signAttend(attend);

      return "succ";
    }


    /**
     *@Description  考勤数据分页查询
     */
    @RequiresPermissions("attend:attendList")
    @RequestMapping("/attendList")
    @ResponseBody
    public PageQueryBean listAttend(QueryCondition condition,HttpSession session){
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
        String [] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        condition.setUserId(user.getId());
        PageQueryBean result = attendService.listAttend(condition);
        return result;
    }



}
