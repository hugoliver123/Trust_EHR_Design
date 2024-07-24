package com.test;

import com.entity.AesDataFrame;
import com.mapper.userMapper;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

public class TestInsertAesKey {
    public static void main(String[] args) {
        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        user_mapper.insertAesKey(new AesDataFrame("1.txt","asdkjfirnv", "xuteng", "testhash", "testMac"));
        session.commit();
    }
}
