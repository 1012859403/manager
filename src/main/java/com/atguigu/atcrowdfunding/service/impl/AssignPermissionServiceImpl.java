package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.*;
import com.atguigu.atcrowdfunding.mapper.TPermissionMapper;
import com.atguigu.atcrowdfunding.mapper.TRolePermissionMapper;
import com.atguigu.atcrowdfunding.service.AssignPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssignPermissionServiceImpl implements AssignPermissionService {
   @Autowired
    TPermissionMapper tPermissionMapper;
    @Override
    public List<TPermission> getPermissions() {
      return tPermissionMapper.selectByExample(null);
    }
@Autowired
TRolePermissionMapper tRolePermissionMapper;
    @Override
    public List<Integer> getAssignPermissionIds(Integer roleId) {
        //先通过tRolePermissionMapper调用select方法，通过角色id把所有角色拥有的权限id放进去
        TRolePermissionExample example = new TRolePermissionExample();
        example.createCriteria().andRoleidEqualTo(roleId);
        List<TRolePermission> tRolePermissions = tRolePermissionMapper.selectByExample(example);
        ArrayList<Integer> list = new ArrayList<>();
        //判断tRolePermissions集合是否为空，为空返回null
        if(CollectionUtils.isEmpty(tRolePermissions)){
            return null;
        }
        //不为空，遍历集合，把集合中的每一个权限id拿出来放入到新建的集合中
        for (TRolePermission tRolePermission : tRolePermissions) {
            list.add(tRolePermission.getPermissionid());
        }
        return list;
    }

}
