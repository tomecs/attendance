package com.attendance.workflow.controller;

import com.attendance.user.entity.User;
import com.attendance.workflow.entity.ReAttend;
import com.attendance.workflow.service.ReAttendService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Tomecs.
 */
@Controller
@RequestMapping("reAttend")
public class ReAttendController {

    @Autowired
    private ReAttendService reAttendService;

    /**
     *@Description 补签数据页面
     */
    @RequestMapping
    public String toReAttend(Model model,HttpSession session){

        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
        List<ReAttend> reAttendList = reAttendService.listReAttend(user.getUsername());
        model.addAttribute("reAttendList",reAttendList);
        return "reAttend";
    }



    @RequestMapping("/start")
    public void startReAttendFlow(@RequestBody ReAttend reAttend, HttpSession session){
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
        reAttend.setReAttendStarter(user.getRealName());
        reAttendService.startReAttendFlow(reAttend);
    }


    @RequiresPermissions("reAttend:list")
    @RequestMapping("/list")
    public String listReAttendFlow(Model model,HttpSession session){
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
        String userName = user.getUsername();
        List<ReAttend> tasks = reAttendService.listTasks(userName);
        model.addAttribute("tasks",tasks);
        return  "reAttendApprove";
    }


    @RequestMapping("/approve")
    public void approveReAttendFlow(@RequestBody ReAttend reAttend){
        reAttendService.approve(reAttend);
    }

}
