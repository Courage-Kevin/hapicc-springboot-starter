package com.hapicc.services;

import com.hapicc.pojo.JqGridResult;
import com.hapicc.pojo.SysUser;

public interface UserService {

    int save(SysUser user) throws Exception;

    int update(SysUser user);

    int delete(String userId);

    SysUser get(String userId);

    JqGridResult list(SysUser user, Integer page, Integer rows, String sidx, String sord, boolean needTotal)
            throws Exception;

    SysUser getUserSimpleInfo(String userId);

    void saveUserTestTransaction(SysUser user);
}
