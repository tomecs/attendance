package com.attendance.common.security;

import com.attendance.user.entity.Permission;
import com.attendance.user.entity.Role;
import com.attendance.user.entity.User;
import com.attendance.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Tomecs.
 */
public class MyRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;


    /**
     * 授权
     * @param principalCollection
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String)principalCollection.getPrimaryPrincipal();
        User user = userService.findUserByUserName(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for(Role role :user.getRoleList()){
            authorizationInfo.addRole(role.getRole());
            for(Permission permission :role.getPermissionList()){
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return authorizationInfo;
    }


    /**
     * 认证 登录
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToke = (UsernamePasswordToken)authenticationToken;
        String username =  usernamePasswordToke.getUsername();
        User user = userService.findUserByUserName(username);
        if(user==null){
            return null;
        }else {
            AuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
            SecurityUtils.getSubject().getSession().setAttribute("userinfo",user);
            return info;
        }

    }
}
