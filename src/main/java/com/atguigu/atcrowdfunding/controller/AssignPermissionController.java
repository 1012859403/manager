package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.TPermission;
import com.atguigu.atcrowdfunding.service.AssignPermissionService;
import com.atguigu.atcrowdfunding.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class AssignPermissionController{
    @Autowired
    AssignPermissionService assignPermissionService;
    //得到所有的权限
    @ResponseBody
    @RequestMapping("/getAssignPermissions")
    public List<TPermission> getAssignPermissions(){
        List<TPermission> permissions = assignPermissionService.getPermissions();
        return permissions;
    }
    //得到角色拥有的权限的id集合
    @ResponseBody
    @RequestMapping("/getRolePermissionIds")
    public List<Integer> getRolePermissionIds(Integer roleId){
        return assignPermissionService.getAssignPermissionIds(roleId);
    }
}
