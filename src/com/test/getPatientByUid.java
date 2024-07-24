package com.test;

import com.entity.User;
import com.mapper.userMapper;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

public class getPatientByUid {
    public static void main(String[] args) {
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        User user = user_mapper.queryPatientByUid(3);
        System.out.println(user.getUserName());
    }
}
