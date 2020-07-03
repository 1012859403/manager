package com.atguigu.atcrowdfunding.service.impl;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TAdminExample;
import com.atguigu.atcrowdfunding.mapper.TAdminMapper;
import com.atguigu.atcrowdfunding.service.AdminService;
import com.atguigu.atcrowdfunding.utils.DateUtil;
import com.atguigu.atcrowdfunding.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    TAdminMapper tAdminMapper;
    @Override
    public TAdmin doLogin(String loginacct, String userpswd) {
        TAdminExample example = new TAdminExample();
        example.createCriteria().andLoginacctEqualTo(loginacct).andUserpswdEqualTo(MD5Util.digest(userpswd));
        List<TAdmin> admins = tAdminMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(admins)||admins.size()>1){
            return null;
        }
        return admins.get(0);
    }

    @Override
    public List<TAdmin> getAllAdmins(String condition) {
//        判断condition是否为空，没有参数，没有就查询所有
        if(StringUtils.isEmpty(condition)) {
            return tAdminMapper.selectByExample(null);
        }
//        有条件，按条件查询
        TAdminExample example = new TAdminExample();
//        条件1
        example.createCriteria().andLoginacctLike("%"+condition+"%");
//        条件2
        TAdminExample.Criteria c2 = example.createCriteria();
        c2.andUsernameLike("%"+condition+"%");
//        条件3
        TAdminExample.Criteria c3 = example.createCriteria();
        c3.andEmailLike("%"+condition+"%");
//        把c2，c3和example三个条件进行or拼接
        example.or(c2);
        example.or(c3);
        return tAdminMapper.selectByExample(example);
    }

    @Override
    public void deleteAdminById(Integer id) {
        tAdminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void addAdmin(TAdmin tAdmin) {
        TAdminExample example = new TAdminExample();
        example.createCriteria().andLoginacctEqualTo(tAdmin.getLoginacct());
        long count = tAdminMapper.countByExample(example);
//        判断用户名是否大于0
        if(count>0){
            throw new RuntimeException("账户异常，用户名已被占用");
        }
        example.clear();
        example.createCriteria().andEmailEqualTo(tAdmin.getEmail());
        long count1 = tAdminMapper.countByExample(example);
        if(count1>0){
            throw new RuntimeException("账户异常，邮箱已被占用");
        }
        tAdmin.setUserpswd(MD5Util.digest(tAdmin.getUserpswd()));
        tAdmin.setCreatetime(DateUtil.getFormatTime());
        tAdminMapper.insertSelective(tAdmin);
    }

    @Override
    public void batchDeleteAdmin(List<Integer> ids) {
        TAdminExample example = new TAdminExample();
        example.createCriteria().andIdIn(ids);
        tAdminMapper.deleteByExample(example);
    }

    @Override
    public TAdmin getAdminById(Integer id) {
        return tAdminMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateAdminById(TAdmin tAdmin) {
        tAdminMapper.updateByPrimaryKeySelective(tAdmin);
    }
}
