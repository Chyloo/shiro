package com.example.demo.config;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.shiro.AuthSubjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author:chy
 * @Date:2020/7/17 0017下午 17:26
 */
@Slf4j
@SuppressWarnings("all")
public class MyRealm extends AuthorizingRealm {
    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("##################执行Shiro权限认证##################");
        //获取当前登入的用户
        User user = (User) principalCollection.getPrimaryPrincipal();
        //到数据库查看是否有此对象
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            if (AuthSubjectUtil.isPatformAdmin()) {
                //判断如果是admin的话就添加所有权限*
                info.addStringPermission("*");
            }
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            for (Role role : user.getRoleList()) {
                info.addRole(role.getName());
                //添加权限，在service里面的就是就要添加了,这里是假数据
                Set<String> perNameSet = new HashSet<>();
                perNameSet.add("hello:view");
                info.addStringPermissions(perNameSet);
            }
            return info;
        }
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
    }

    /**
     * 登入认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //向上转型 UsernamePasswordToken是authenticationToken的实现类
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //ReflectionToStringBuilder：Object类的toString
        log.info("验证当前Subject时获取到token为：" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
        //查出是否有此用户
        //假数据，真正的从数据库查询
        User user = new User("chyloo", "123456", null);
        //User user = userService.findByName(token.getUsername());
        if (user != null) {
            //set:假数据 正常要从role表里面查询拥有改角色的用户名
            Set<String> setName = new HashSet<>();
            setName.add(user.getUserName());
            List<Permission> permissionList = Arrays.asList(new Permission(1L, 1, "hello", "hello", "hello"));
            List<Role> list = Arrays.asList(new Role(1, "admin", permissionList, setName));
            user.setRoleList(list);
            user.getRoleList().forEach(System.out::println);
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            return new SimpleAuthenticationInfo(user, user.getPwd(), user.getUserName());
        }
        return null;
    }
}
