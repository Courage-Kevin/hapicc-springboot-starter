package com.hapicc.services.mybatis;

import com.hapicc.pojo.JqGridResult;
import com.hapicc.pojo.SysUser;

import java.util.Map;

public interface UserService {

    int save(SysUser user) throws Exception;

    int update(String id, SysUser user);

    int delete(String id);

    SysUser get(String id);

    JqGridResult list(Map<String, String> params) throws Exception;

    SysUser getUserSimpleInfo(String id);

    void saveUserTestTransaction(SysUser user);
}
