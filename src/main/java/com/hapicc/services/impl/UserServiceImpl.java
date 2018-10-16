package com.hapicc.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hapicc.mappers.SysUserMapper;
import com.hapicc.mappers.SysUserMapperCustom;
import com.hapicc.pojo.JqGridResult;
import com.hapicc.pojo.SysUser;
import com.hapicc.services.UserService;
import com.hapicc.utils.common.ReqUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

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
    public JqGridResult list(Map<String, String> params) throws Exception {

        Integer page = ReqUtils.getPage(params);
        Integer rows = ReqUtils.getRows(params);
        String sidx = ReqUtils.getSidx(params, false);
        String sord = ReqUtils.getSord(params);
        boolean needTotal = params != null && "true".equals(params.get("needTotal"));
        String q = params != null ? params.get("q") : null;

        PageHelper.startPage(page, rows, needTotal);

        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(q)) {
            criteria.orLike("name", "%" + q + "%");
            criteria.orLike("loginName", "%" + q + "%");
        }

        example.setOrderByClause(sidx + " " + (sord != null ? sord : "asc"));

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
