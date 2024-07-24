package com.service;

import com.entity.User;
import com.mapper.userMapper;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class getPatients {
    public static ArrayList<User> getPatients(){
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        List<Map<Object, Object>> user = user_mapper.findPatients();
        ArrayList<User> backValue = new ArrayList<>();
        for(Map i: user){
            User temp = new User();
            temp.setUserId((Integer) i.get("userId"));
            temp.setUserName((String) i.get("userName"));
            backValue.add(temp);
        }
        return backValue;
    }
    public static User queryPatientByUid(int uid){
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        User user = user_mapper.queryPatientByUid(uid);
        return user;
    }
}
