package com.test;

import com.entity.User;
import com.mapper.userMapper;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

public class TestFindAesKeyByName {
    public static void main(String[] args) {
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        String key = user_mapper.findAesKey("3-1.ehrd");
        System.out.println(key);
    }
}
