package com.howe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.howe.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author howe
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT p.permission_key FROM tb_permission p " +
            "JOIN tb_role_permission rp ON p.id = rp.permission_id " +
            "JOIN tb_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> getPermissionKeysByUserId(@Param("userId") Long userId);

    @Select("SELECT r.role_key FROM tb_role r " +
            "JOIN tb_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> getRoleKeysByUserId(@Param("userId") Long userId);

}
