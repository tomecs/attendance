package com.attendance.user.service;

import com.attendance.common.utils.MD5Utils;
import com.attendance.user.dao.UserMapper;
import com.attendance.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Tomecs
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;


    /**
     *@Description 根据用户名查询用户
     */
    @Override
    public User findUserByUserName(String username) {
        User user=null;
        try {
             user =userMapper.selectByUserName(username);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return user;
    }


    @Override
    public void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        user.setPassword(MD5Utils.encryptPassword(user.getPassword()));

        userMapper.insertSelective(user);
    }
}
