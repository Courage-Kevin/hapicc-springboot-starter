package com.hapicc.services;

import com.hapicc.pojo.JqGridResult;
import com.hapicc.pojo.SysUser;

import java.util.Map;

public interface UserService {

    int save(SysUser user) throws Exception;

    int update(SysUser user);

    int delete(String userId);

    SysUser get(String userId);

    JqGridResult list(Map<String, String> params) throws Exception;

    SysUser getUserSimpleInfo(String userId);

    void saveUserTestTransaction(SysUser user);
}
