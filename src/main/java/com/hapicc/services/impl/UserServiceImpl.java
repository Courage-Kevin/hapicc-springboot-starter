package com.hapicc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.CaseFormat;
import com.hapicc.mappers.SysUserMapper;
import com.hapicc.mappers.SysUserMapperCustom;
import com.hapicc.pojo.JqGridResult;
import com.hapicc.pojo.SysUser;
import com.hapicc.services.UserService;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserMapperCustom userMapperCustom;

    @Override
    @Transactional
    public int save(SysUser user) throws Exception {
        return userMapper.insertSelective(user);
    }

    @Override
    @Transactional
    public int update(SysUser user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional
    public int delete(String userId) {
        return userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SysUser get(String userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public JqGridResult list(SysUser user, Integer page, Integer rows, String sidx, String sord, boolean needTotal)
            throws Exception {

        PageHelper.startPage(page != null ? page : 1, rows != null ? rows : 10, needTotal);

        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(user.getName())) {
            criteria.orLike("name", "%" + user.getName() + "%");
        }

        if (!StringUtils.isEmpty(user.getLoginName())) {
            criteria.orLike("loginName", "%" + user.getLoginName() + "%");
        }

        String validSidx = "date_created";
        if (!StringUtils.isEmpty(sidx)) {
            validSidx = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sidx);
        }

        example.setOrderByClause(validSidx + " " + (sord != null ? sord : "asc"));

        List<SysUser> userList = userMapper.selectByExample(example);

        JqGridResult result = new JqGridResult();
        result.setPage(page);
        result.setRows(userList);

        if (needTotal) {
            PageInfo<SysUser> pageInfo = new PageInfo<>(userList);
            result.setTotal(pageInfo.getPages());
            result.setRecords(pageInfo.getTotal());
        }

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SysUser getUserSimpleInfo(String userId) {

        List<SysUser> userList = userMapperCustom.selectSimpleInfoById(userId);

        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        }

        return null;
    }

    @Override
    // 注解中事务的传播行为 propagation 的默认值即为 Propagation.REQUIRED
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUserTestTransaction(SysUser user) {

        userMapper.insertSelective(user);

        System.out.println(1 / 0);

        user.setGender((byte) 1);
        userMapper.updateByPrimaryKeySelective(user);
    }
}
