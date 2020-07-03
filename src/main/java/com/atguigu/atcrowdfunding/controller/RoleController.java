package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.service.impl.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    @GetMapping("/index")
    public String toRolePage(){
        return "roles/role";
    }
    @Autowired
    RoleService roleService;
    @ResponseBody
    @RequestMapping("/getRoles")
    public PageInfo<TRole> getRoles(@RequestParam(required = false,defaultValue = "1") Integer pageNum,@RequestParam(required = false,defaultValue = "") String condition){
//        开启分页查询
        PageHelper.startPage(pageNum,4);
//        获得角色的集合
        List<TRole> roles = roleService.getRoles(condition);
        PageInfo<TRole> pageInfo = new PageInfo<>(roles,3);
//        返回分页详情对象
        return pageInfo;
    }
    //    删除角色
    @ResponseBody
    @RequestMapping("/deleteRoleById")
    public String deleteRoleById(Integer id){
        roleService.deleteRoleById(id);
        return "ok";
    }
    //    新增角色
    @ResponseBody
    @RequestMapping("/addRole")
    public String addRole(TRole tRole){
        roleService.addRole(tRole);
        return "ok";
    }
    //    得到一个角色进行回显
    @ResponseBody
    @RequestMapping("/getRoleById")
    public TRole getRoleById(Integer id){
        TRole role = roleService.getRoleById(id);
        return role;
    }
    //    更新角色
    @ResponseBody
    @RequestMapping("/updateRole")
    public String updateRole(TRole tRole){
        roleService.updateRole(tRole);
        return "ok";
    }
    //    批删除
    @ResponseBody
    @RequestMapping("/batchDeleteRoles")
    public String batchDeleteRoles(@RequestParam("ids") List<Integer> ids){
        roleService.batchDeleteRoles(ids);
        return "ok";
    }
    //给角色重新分配权限
    @ResponseBody
    @RequestMapping("/reAssignPermissionsToRole")
    public String reAssignPermissionsToRole(Integer roleId,@RequestParam("permissionIds") List<Integer> permissionIds){
        roleService.reAssignPermissionsToRole(roleId,permissionIds);
        return "ok";
    }
}
