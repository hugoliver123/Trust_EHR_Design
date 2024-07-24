package com.test;

import com.mapper.userMapper;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public class TestRecourdCount {
    public static void main(String[] args) {
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        user_mapper.plusOneCount(3);
        session.commit();


        SqlSession session2 = getSqlSession.creatSqlSession();
        userMapper user_mapper2 = session2.getMapper(userMapper.class);
        int pCount = user_mapper2.findRecordCount(3);

        System.out.println(pCount);
    }
}
