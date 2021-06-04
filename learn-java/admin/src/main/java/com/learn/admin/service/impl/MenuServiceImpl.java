package com.learn.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.admin.dao.MenuMapper;
import com.learn.admin.entity.Menu;
import com.learn.admin.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LD
 * @date 2020/3/26 15:13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final MenuMapper menuMapper;

    public MenuServiceImpl(MenuMapper menuMapper ) {
        this.menuMapper = menuMapper;
    }

    @Override
    public List<Menu> loadAllMenus() {
        return treeMenu(list(), 0);
    }

    @Override
    public List<Menu> getMenusByUserId(Integer userId) {
        return menuMapper.getMenuByUserId(userId);
    }

    private List<Menu> treeMenu(List<Menu> menus, Integer parentId) {
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(treeMenu(menus, menu.getMenuId()));
                result.add(menu);
            }
        }
        return result;
    }

}
