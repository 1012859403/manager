package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.bean.TRoleExample;
import com.atguigu.atcrowdfunding.bean.TRolePermissionExample;
import com.atguigu.atcrowdfunding.mapper.TRoleMapper;
import com.atguigu.atcrowdfunding.mapper.TRolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Service
public class RoleService implements com.atguigu.atcrowdfunding.service.RoleService {
    @Autowired
    TRoleMapper tRoleMapper;

    @Override
    public List<TRole> getRoles(String condition) {
//        没有条件的情况下
        if (StringUtils.isEmpty(condition)) {
            List<TRole> roles = tRoleMapper.selectByExample(null);
            return roles;
        }
//        有条件的情况下
        TRoleExample example = new TRoleExample();
//        条件查询
        example.createCriteria().andNameLike("%" + condition + "%");
        return tRoleMapper.selectByExample(example);
    }

    @Override
    public void deleteRoleById(Integer id) {
        tRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void addRole(TRole tRole) {
        tRoleMapper.insertSelective(tRole);
    }

    @Override
    public TRole getRoleById(Integer id) {
        return tRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateRole(TRole tRole) {
        tRoleMapper.updateByPrimaryKeySelective(tRole);
    }

    @Override
    public void batchDeleteRoles(List<Integer> ids) {
        TRoleExample example = new TRoleExample();
        example.createCriteria().andIdIn(ids);
        tRoleMapper.deleteByExample(example);
    }
@Autowired
    TRolePermissionMapper tRolePermissionMapper;
    @Override
    public void reAssignPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        //删除所有角色选中的权限
        TRolePermissionExample example = new TRolePermissionExample();
        example.createCriteria().andRoleidEqualTo(roleId);
        tRolePermissionMapper.deleteByExample(example);
        //重新分配权限
        tRolePermissionMapper.reAssignPermissionsToRole(roleId,permissionIds);
    }
}
