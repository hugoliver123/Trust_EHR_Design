package com.test;

import com.entity.User;
import com.mapper.userMapper;
import org.apache.ibatis.session.SqlSession;
import com.util.getSqlSession;

public class Test {
    public static void main(String[] args) {
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        User user = user_mapper.queryUserById(13);
        System.out.println(user.getUserName());
    }
}
