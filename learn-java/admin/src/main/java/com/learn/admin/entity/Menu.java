package com.learn.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author LD
 * @date 2020/3/5 21:39
 */
@Data
@TableName(value = "bmsys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Integer menuId;

    private String menuName;

    private Integer parentId;

    private Integer orderNum;

    private String url;

    private String menuType;

    private String visible;

    private String perms;

    private String remark;

    @TableField(exist = false)
    private List<Menu> children;

    public Menu() {
    }

    public Menu(Integer menuId, String menuName, Integer parentId, Integer orderNum, String url, String menuType, String visible, String perms, String remark) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.parentId = parentId;
        this.orderNum = orderNum;
        this.url = url;
        this.menuType = menuType;
        this.visible = visible;
        this.perms = perms;
        this.remark = remark;
    }
}

