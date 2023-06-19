package com.howe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.howe.dto.Result;
import com.howe.entity.User;
/**
 * @author howe
 */
public interface UserService extends IService<User> {
    Result login(User user);

}
