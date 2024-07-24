package com.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class getSqlSession {
    public static SqlSession creatSqlSession(){
        SqlSessionFactory sqlSessionFactory = null;
        InputStream input = null;
        SqlSession session = null;
        try {
            String resource = "mybatis-config.xml";
            input = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
            session = sqlSessionFactory.openSession();
            return session;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(creatSqlSession());
    }
}
