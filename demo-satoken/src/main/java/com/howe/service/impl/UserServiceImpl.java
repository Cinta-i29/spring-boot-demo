package com.howe.service.impl;


import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.howe.dto.Result;
import com.howe.entity.User;
import com.howe.mapper.UserMapper;
import com.howe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author howe
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, StpInterface {
    @Autowired
    UserMapper userMapper;

    /**
     * 登录查询数据库
     * @param user
     * @return
     */
    @Override
    public Result login(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword());
        User tempUser = getOne(queryWrapper);
        if(tempUser != null && tempUser.getId() != null){
            StpUtil.login(tempUser.getId());
            return Result.success(tempUser,"登录成功");
        };
        return Result.fail("账号或密码错误");
    }

    /**
     * 返回一个账号所拥有的权限集合
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return userMapper.getPermissionKeysByUserId(Long.parseLong(loginId.toString()));
    }

    /**
     * 登录方法
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userMapper.getRoleKeysByUserId(Long.parseLong(loginId.toString()));
    }
}