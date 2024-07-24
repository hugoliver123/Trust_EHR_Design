package com.mapper;

import com.entity.AesDataFrame;
import com.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户接口
 */
public interface userMapper {
    public User queryUserByName(String userName);
    public User queryUserById(int userId);
    public List<Map<Object, Object>> findPatients();
    public int findRecordCount(int userId);
    public void plusOneCount(int userId);
    public User queryPatientByUid(int userId);
    public void insertAesKey(AesDataFrame data);
    public String findAesKey(String name);
    public String findBuilder(String name);
}
