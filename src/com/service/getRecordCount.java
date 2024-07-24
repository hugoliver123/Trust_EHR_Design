package com.service;

import com.mapper.userMapper;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

public class getRecordCount {
    public static int getRecordCount(int userId){
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        int pCount = user_mapper.findRecordCount(userId);
        return pCount;
    }
    public static void plusOneRecord(int userId){
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        user_mapper.plusOneCount(userId);
        session.commit();
    }
}
