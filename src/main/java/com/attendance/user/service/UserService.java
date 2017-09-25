package com.attendance.user.service;

import com.attendance.user.entity.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Tomecs.
 */
public interface UserService {


    User findUserByUserName(String username);

    void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;
}
