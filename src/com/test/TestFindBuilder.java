package com.test;

import com.mapper.userMapper;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

public class TestFindBuilder {
    public static void main(String[] args) {
        SqlSession sqlSessionsession = getSqlSession.creatSqlSession();
        userMapper user_mapper = sqlSessionsession.getMapper(userMapper.class);
        String key = user_mapper.findBuilder("3-1.ehrd");
        System.out.println(key);
    }
}
