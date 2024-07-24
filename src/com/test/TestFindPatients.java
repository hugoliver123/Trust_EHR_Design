package com.test;

import com.mapper.userMapper;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public class TestFindPatients {
    public static void main(String[] args) {
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        List<Map<Object, Object>> user = user_mapper.findPatients();
        for(Map i: user){
            System.out.println(i.get("userId"));
            System.out.println(i.get("userName"));
        }
    }
}
