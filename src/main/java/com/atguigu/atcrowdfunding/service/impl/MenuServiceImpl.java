package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.mapper.TMenuMapper;
import com.atguigu.atcrowdfunding.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    TMenuMapper tMenuMapper;
    @Override
    public List<TMenu> getPMenu() {
        List<TMenu> menus = tMenuMapper.selectByExample(null);
        if(CollectionUtils.isEmpty(menus)){
            return null;
        }
        Map<Integer , TMenu> pmenus = new HashMap<>();
        //挑选出父菜单集合
        for (TMenu menu : menus) {
            if(menu.getPid()==0){
                //使用父菜单的id作为键，使用父菜单对象作为值
                pmenus.put(menu.getId() ,menu);
            }
        }
        //将子菜单设置给自己的父菜单
        for (TMenu menu : menus) {
            TMenu pMenu = pmenus.get(menu.getPid());
            //当一个菜单的pid！=0并且pid等于某个父菜单的id时，就是父子关系
            if(menu.getPid()!=0 && pMenu!=null){
                //将子菜单设置到父菜单的children集合中
                pMenu.getChildren().add(menu);
            }
        }
        //将封装了父子菜单结构的集合返回
        return new ArrayList<TMenu>(pmenus.values());
    }

    @Override
    public void addMenu(TMenu tMenu) {
        tMenuMapper.insertSelective(tMenu);
    }

    @Override
    public void deleteMenuById(Integer id) {
        tMenuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public TMenu getMenuById(Integer id) {
        TMenu tMenu = tMenuMapper.selectByPrimaryKey(id);
        return tMenu;
    }

    @Override
    public void updateMenu(TMenu tMenu) {
        tMenuMapper.updateByPrimaryKeySelective(tMenu);
    }
}

