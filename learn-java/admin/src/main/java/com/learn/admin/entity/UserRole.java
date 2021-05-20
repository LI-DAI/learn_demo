package com.learn.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LD
 * @date 2020/3/5 21:39
 */
@Data
@TableName(value = "bmsys_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer userRoleId;

    private Integer userId;

    private Integer roleId;

    public UserRole() {
    }

    public UserRole(Integer userRoleId, Integer userId, Integer roleId) {
        this.userRoleId = userRoleId;
        this.userId = userId;
        this.roleId = roleId;
    }
}

