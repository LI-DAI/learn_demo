package com.learn.admin.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.learn.common.constant.Constant;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @author LD
 * @date 2020/3/5 13:56
 */
@Data
@TableName(value = "bmsys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer userId;

    @ExcelProperty(value = "用户名")
    private String username;

    @ExcelProperty(value = "昵称")
    private String nickname;

    @ExcelProperty(value = "邮箱")
    @Email(message = "邮箱格式异常")
    private String email;

    @ExcelProperty(value = "电话")
    @Pattern(regexp = Constant.REGEX_PHONE, message = "电话格式异常")
    private String phone;

    @ExcelProperty("性别")
    private String gender;

    private String password;

    private String unit;

    private String department;

    private String status;

    private String remark;

    private Integer enabled;

    private Date createTime;

    private Date updateTime;

    public User() {
    }

    public User(Integer userId, String username, String nickname, String email, String phone, String gender, String password, String unit, String department, String status, String remark, Integer enabled, Date createTime, Date updateTime) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
        this.unit = unit;
        this.department = department;
        this.status = status;
        this.remark = remark;
        this.enabled = enabled;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

}