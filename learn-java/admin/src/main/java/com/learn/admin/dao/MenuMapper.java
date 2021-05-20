package com.learn.admin.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.admin.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LD
 * @date 2020/3/5 21:39
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取权限菜单
     *
     * @param userId /
     * @return /
     */
    @Select(value = "select distinct bm.* from bmsys_user bu " +
            "left join bmsys_user_role bur on bu.user_id = bur.user_id " +
            "left join bmsys_role br on bur.role_id = br.role_id " +
            "left join bmsys_role_menu brm on br.role_id = brm.role_id " +
            "left join bmsys_menu bm on brm.menu_id = bm.menu_id " +
            "where br.role_type = 'R' and br.status = '0' " +
            "and bu.user_id = #{userId}")
    List<Menu> getMenuByUserId(@Param("userId") Integer userId);

}