package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:chy
 * @Date:2020/7/17 0017下午 15:08
 */

@RestController
@Slf4j
public class LoginController {
    @PostMapping("doLogin")
    public void doLogin(String username, String password) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNoneBlank(password)) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, "dologin");
            //得到当前用户
            Subject subject = SecurityUtils.getSubject();
            log.info("对用户[" + username + "]进行登录验证..验证开始");
            try {
                subject.login(token);
                //验证是否登录成功
                if (subject.isAuthenticated()) {
                    log.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                } else {
                    token.clear();
                }
            } catch (UnknownAccountException uae) {
                log.info("对用户[" + username + "]进行登录验证..验证失败-username wasn't in the system");
            } catch (IncorrectCredentialsException ice) {
                log.info("对用户[" + username + "]进行登录验证..验证失败-password didn't match");
            } catch (LockedAccountException lae) {
                log.info("对用户[" + username + "]进行登录验证..验证失败-account is locked in the system");
            } catch (AuthenticationException ae) {
                log.error(ae.getMessage());
            }
        }
    }

    @PostMapping("hello")
    /**
     * 判断是否有view
     */
    @RequiresPermissions("hello:view")
    public String hello() {
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password, "dologin");
//        subject.login(token);
//        //判断是否有admin这个角色
//        if (subject.hasRole("admin")){
//            return "hello";
//        }
            return "hello";
    }

    @GetMapping("login")
    public String login() {
        return "please login!";
    }

    @PostMapping("noPermissions")
    public String noPermissions(){
        return "noPermissions";
    }
}
