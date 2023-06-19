package com.howe.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.howe.dto.Result;
import com.howe.entity.User;
import com.howe.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author howe
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    //用户层
    @Autowired
    private UserServiceImpl userService;


    /**
     * 登录接口
     * @param user 用户名
     * @return 用户登录信息
     */
    @Operation(summary = "登录页面", description = "登录登录登录")
    @PostMapping("/login")
    public Result loginUser(@RequestBody User user) {
        return userService.login(user);
    }

    /**
     * 注销接口
     * @param user 用户信息
     * @return 对象
     */
    @Operation(summary = "注销 认证页面", description = "退出登录")
    @PostMapping("/logout")
    public Result logoutUser(@RequestBody User user) {
        StpUtil.logout(user.getId());
        return Result.success("退出登录成功");
    }

    /**
     * 查看登录状态
     * @return
     */
    @Operation(summary = "登录状态" ,description = "查询当前用户是否已登录")
    @GetMapping("/islogin")
    public Result isLogin() {
        if(StpUtil.isLogin()){
            return Result.success("用户{"+StpUtil.getLoginId()+"}已经处于登录状态");
        }else{
            return Result.fail("你没有处于登录状态");
        }
    }

    /**
     * 查看token
     * @return
     */
    @Operation(summary = "查看token" ,description = "查询当前用户的token")
    @GetMapping("/token")
    public Result token() {
        if(StpUtil.isLogin()){
            //创建一个键值对集合
            Map<String, Object> map = new HashMap<>();
            map.put("token", StpUtil.getTokenValue());
            return Result.success(map);
        }else{
            return Result.fail("获取Token失败，请先登录");
        }
    }

    /**
     * 查询所有用户
     * @return 用户列表
     */
    @SaCheckPermission("user:query")
    @Operation(summary = "查询所有用户", description = "你会得到所有用户的信息")
    @GetMapping()
    public Result queryUserList() {
        List<User> list = userService.list();
        return Result.success(list);
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    @SaCheckPermission("user:query")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户信息")
    @GetMapping("/{id}")
    public Result queryUserById(
            @Parameter(description = "用户ID", example = "1") @PathVariable("id") Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id);
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.fail("用户不存在");
        }
    }

    /**
     * 保存用户
     *
     * @param user 用户
     * @return 是否成功
     */
    @SaCheckPermission("user:create")
    @Operation(summary = "保存用户", description = "保存用户")
    @PostMapping()
    public Result saveUser(@RequestBody User user) {
        if (userService.save(user)) {
            return Result.success("添加用户成功！");
        } else {
            return Result.fail("添加用户失败！");
        }
    }

    /**
     * 根据ID修改用户
     *
     * @param user 用户
     * @return 是否成功
     */
    @SaCheckPermission("user:update")
    @Operation(summary = "根据ID修改用户", description = "根据用户ID修改用户信息")
    @PutMapping()
    public Result updateUser(@RequestBody User user) {
        if (userService.updateById(user)) {
            return Result.success("修改成功");
        } else {
            return Result.fail("修改失败");
        }
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    @SaCheckPermission("user:delete")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户信息")
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") Long id) {
        if (userService.remove(new LambdaQueryWrapper<User>().eq(User::getId, id))) {
            return Result.success("删除成功");
        } else {
            return Result.fail("删除失败");
        }
    }

}
