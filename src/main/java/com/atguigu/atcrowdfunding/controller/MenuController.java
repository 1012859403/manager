package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @RequestMapping("/index")
    public String toMenuPage(){
        return "menus/menu";
    }
    @Autowired
    MenuService menuService;
    @ResponseBody
    @RequestMapping("/getMenus")
    public List<TMenu> getMenus(){
        return menuService.getPMenu();
    }
    //新增
    @ResponseBody
    @RequestMapping("/addMenu")
    public String addMenu(TMenu tMenu){
        menuService.addMenu(tMenu);
        return "ok";
    }
    //删除
    @ResponseBody
    @RequestMapping("/deleteMenuById")
    public String deleteMenuById(Integer id){
        menuService.deleteMenuById(id);
        return "ok";
    }
    //获得一个菜单
    @ResponseBody
    @RequestMapping("/getMenuById")
    public TMenu getMenuById(Integer id){
        TMenu menu = menuService.getMenuById(id);
        return menu;
    }
//更新菜单
@ResponseBody
@RequestMapping("/updateMenu")
public String updateMenu(TMenu tMenu){
    menuService.updateMenu(tMenu);
    return "ok";
}
}
