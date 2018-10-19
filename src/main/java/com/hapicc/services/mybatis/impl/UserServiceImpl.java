package com.hapicc.services.mybatis.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hapicc.mappers.SysUserMapper;
import com.hapicc.mappers.SysUserMapperCustom;
import com.hapicc.pojo.JqGridResult;
import com.hapicc.pojo.SysUser;
import com.hapicc.services.mybatis.UserService;
import com.hapicc.utils.common.RequestUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Sid sid;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserMapperCustom userMapperCustom;

    @Override
    @Transactional
    public int save(SysUser user) throws Exception {
        Date now = new Date();

        user.setId(sid.next());
        if (user.getPassword() == null) {
            user.setPassword(sid.nextShort());
        }
        if (user.getDateCreated() == null) {
            user.setDateCreated(now);
        }
        if (user.getLastUpdated() == null) {
            user.setLastUpdated(now);
        }

        return userMapper.insertSelective(user);
    }

    @Override
    @Transactional
    public int update(String id, SysUser user) {

        user.setId(id);

        if (user.getLastUpdated() == null) {
            Date now = new Date();
            user.setLastUpdated(now);
        }

        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional
    public int delete(String id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SysUser get(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public JqGridResult list(Map<String, String> params) throws Exception {

        Integer page = RequestUtils.getPage(params);
        Integer rows = RequestUtils.getRows(params);
        String sidx = RequestUtils.getSidx(params, false);
        String sord = RequestUtils.getSord(params);
        boolean needTotal = RequestUtils.needTotal(params);
        String q = RequestUtils.getQ(params);

        PageHelper.startPage(page, rows, needTotal);

        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(q)) {
            criteria.orLike("name", "%" + q + "%");
            criteria.orLike("loginName", "%" + q + "%");
        }

        example.setOrderByClause(sidx + " " + (sord != null ? sord : "asc"));

        List<SysUser> userList = userMapper.selectByExample(example);

        return new JqGridResult(new PageInfo<>(userList), needTotal);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SysUser getUserSimpleInfo(String id) {

        List<SysUser> userList = userMapperCustom.selectSimpleInfoById(id);

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
