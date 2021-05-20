package com.learn.admin.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.admin.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LD
 * @date 2020/3/5 21:39
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取roles
     *
     * @param userId /
     * @return /
     */
    @Select(value = "select br.* from bmsys_role br " +
            "left join bmsys_user_role bur on br.role_id = bur.role_id " +
            "where bur.user_id = #{userId} " +
            "and br.role_type = 'R' and br.status = '0'")
    List<Role> getRolesByUserId(@Param("userId") Integer userId);
}