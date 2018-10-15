package com.hapicc.mappers;

import java.util.List;

import com.hapicc.pojo.SysUser;
import com.hapicc.utils.generator.MyMapper;

public interface SysUserMapperCustom extends MyMapper<SysUser> {

    List<SysUser> selectSimpleInfoById(String id);
}