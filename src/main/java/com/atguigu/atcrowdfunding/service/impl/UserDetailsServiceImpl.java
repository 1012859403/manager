package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TAdminExample;
import com.atguigu.atcrowdfunding.bean.TPermission;
import com.atguigu.atcrowdfunding.bean.TRole;
import com.atguigu.atcrowdfunding.mapper.TAdminMapper;
import com.atguigu.atcrowdfunding.mapper.TAdminRoleMapper;
import com.atguigu.atcrowdfunding.mapper.TPermissionMapper;
import com.atguigu.atcrowdfunding.mapper.TRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    TAdminMapper tAdminMapper;
    @Autowired
    TRoleMapper tRoleMapper;
    @Autowired
    TPermissionMapper tPermissionMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名查询用户信息
        TAdminExample example = new TAdminExample();
        example.createCriteria().andLoginacctEqualTo(username);
        List<TAdmin> tAdmins = tAdminMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(tAdmins)||tAdmins.size()>1){
            return null;
        }
        TAdmin admin = tAdmins.get(0);
        //根据用户id查询角色信息
        List<TRole> roles = tRoleMapper.getRoleByAdminId(admin.getId());
        //根据用户id查询权限集合
        List<TPermission> permissions = tPermissionMapper.getPermissionsByAdminId(admin.getId());
        //3、将用户信息和角色权限信息封装为主体对象返回
        //springsecurity为了表达角色和权限不同，需要角色字符串前拼接前缀:ROLE_角色名称
        List<GrantedAuthority> authorities = new ArrayList<>();
        //遍历角色集合，将角色名称拼接前缀ROLE_ 存到权限集合中
        if(!CollectionUtils.isEmpty(roles)){
            for (TRole role : roles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
            }
        }
        //遍历权限集合，将权限名称存到权限集合中
        if(!CollectionUtils.isEmpty(permissions)){
            for (TPermission permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        //将用户信息和角色权限信息封装为主体对象返回
        User user = new User(admin.getLoginacct(), admin.getUserpswd(), authorities);
        System.out.println("user = " + user);
        return user;
    }
}
