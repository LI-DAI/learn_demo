package com.learn.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.admin.entity.Role;


import java.util.List;

/**
 * @author LD
 * @date 2020/3/26 15:06
 */
public interface RoleService extends IService<Role> {

    /**
     * 删除角色
     *
     * @param roleIds 角色ID集合
     */
    void deleteRoles(List<Integer> roleIds);

    /**
     * 添加角色
     *
     * @param role 角色信息
     * @return true: 添加成功
     */
    boolean addRole(Role role);

    /**
     * 清空缓存
     *
     * @return true：清空成功
     */
    boolean clearCache();

}
