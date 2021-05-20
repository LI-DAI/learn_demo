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
@TableName(value = "bmsys_role_menu")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer roleMenuId;

    private Integer roleId;

    private Integer menuId;

    public RoleMenu() {
    }

    public RoleMenu(Integer roleMenuId, Integer roleId, Integer menuId) {
        this.roleMenuId = roleMenuId;
        this.roleId = roleId;
        this.menuId = menuId;
    }
}

