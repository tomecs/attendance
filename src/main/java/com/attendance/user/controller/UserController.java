package com.attendance.user.controller;

import com.attendance.user.entity.User;
import com.attendance.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Tomecs.
 */
@Controller
@RequestMapping("user")
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping("/home")
    public String home(){

     return "home";
    }

    /**
     *@Description  获取用户信息
     */
    @RequestMapping("/userinfo")
    @ResponseBody
    public User getUser(){
       User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
       return user;
    }


    /**
     *@Description 登出系统
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }

}
