package com.service;

import com.entity.User;
import com.entity.vo.messageModel;
import com.mapper.userMapper;
import com.util.StringUtil;
import  com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 业务逻辑
 */
public class userService {
    public messageModel userLogin(String uname, String upwd, String ssalt) throws UnsupportedEncodingException {
        messageModel message_model1 = new messageModel();
        if(StringUtil.isEmpty(uname) ||StringUtil.isEmpty(upwd)){
            message_model1.setCode(0);
            message_model1.setMsg("User name OR password cannot be Empty");
            User u = new User();
            u.setUserName(uname);
            u.setUserPassword(upwd);
            message_model1.setObject(u);
            return  message_model1;
        }

        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        User user = user_mapper.queryUserByName(uname);

        if(user == null){
            message_model1.setCode(0);
            message_model1.setMsg("Username do not exist");
            User u = new User();
            u.setUserName(uname);
            u.setUserPassword(upwd);
            message_model1.setObject(u);
            return  message_model1;
        }

        String password = user.getUserPassword() + ssalt;
        StringBuilder sb = new StringBuilder();
        MessageDigest object = null;
        try {
            object = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encrypted = object.digest(password.getBytes("UTF-8"));
        for (byte b : encrypted) {
            sb.append(String.format("%02x", b));
        }
        System.out.println("sb: " + sb);             // sb 是 hash(hash（密碼）+ salt)
        System.out.println("up: " + upwd);

        if(! upwd.equals(sb.toString())){   //sb是一個stringBuilder 需要轉換為string
            message_model1.setCode(0);
            message_model1.setMsg("Username or Password incorrect");
            User u = new User();
            u.setUserName(uname);
            u.setUserPassword(upwd);
            message_model1.setObject(u);
            return  message_model1;
        }

        //登陆成功
        System.out.println("Login succ");
        System.out.println(user.getUserPosition());
        if(user.getUserPosition().equals("admin")){ //管理员身份
            System.out.println("set1");
            message_model1.setCode(1);
        }
        else if(user.getUserPosition().equals("D")){ // 医生身份
            System.out.println("set2");
            message_model1.setCode(2);
        }
        else if(user.getUserPosition().equals("P")){ // 病人身份
            System.out.println("set3");
            message_model1.setCode(3);
        }else if(user.getUserPosition().equals("restRoot")){
            System.out.println("set4");
            message_model1.setCode(4);
        }else if (user.getUserPosition().equals("A")){
            message_model1.setCode(5);
        }
       else{
            message_model1.setCode(0);  // 无法识别身份
            message_model1.setMsg("Unknown identity, Please Contact IT administrator");
            User u = new User();
            u.setUserName(uname);
            u.setUserPassword(upwd);
            message_model1.setObject(u);
            return  message_model1;
        }
        message_model1.setObject(user);
        return message_model1;
    }
}
