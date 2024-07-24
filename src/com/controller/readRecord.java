package com.controller;

import com.audit.AuditLog;
import com.audit.Block;
import com.entity.User;
import com.mapper.userMapper;
import com.util.AES;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class readRecord extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user"); // 获取Session
        System.out.println("这里是获取User Id 比对是否有权限" + user.getUserId()); // 这里是获取User Id 比对是否有权限

        SqlSession sqlSessionsession = getSqlSession.creatSqlSession();
        userMapper user_mapper2 = sqlSessionsession.getMapper(userMapper.class);
        String key = user_mapper2.findAesKey(req.getParameter("rec"));





        if(Integer.parseInt(req.getParameter("rec").split("-")[0]) == (user.getUserId()) ||
                (user.getUserPosition().equals("D") || user.getUserPosition().equals("admin"))){        // 檢查用戶是否有權限
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 使用SimpleDateFormat对象的format()方法将Date对象格式化为指定格式的字符串
            String formattedDate = dateFormat.format(date);     //時間和日期

            int patientId = Integer.parseInt(req.getParameter("rec").split("-")[0]);

            SqlSession sqlsession = getSqlSession.creatSqlSession();
            userMapper user_mapper = sqlsession.getMapper(userMapper.class);
            User userPatient = user_mapper.queryUserById(patientId);
            AuditLog auditLog = new AuditLog(formattedDate,user.getUserId(), req.getRemoteAddr(), "READ",  req.getParameter("rec"),
                    patientId, userPatient.getUserName());
            Block block = new Block(auditLog, 110, "police"); //权威者签名，提交

            String path = "/Users/yuanxuteng/Desktop/EHRrecord/" + req.getParameter("rec");

            // 以下是读取record文件
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(path));
                String line = reader.readLine();
                reader.close();
                String plaintext = AES.aes_decrypt(line,key);
                System.out.println(plaintext);
                HttpSession session1 = req.getSession();
//                req.setAttribute("plaintext", plaintext);
                session1.setAttribute("plaintext", plaintext);
//                req.setAttribute("recordFileName", req.getParameter("rec"));
                session1.setAttribute("recordFileName", req.getParameter("rec"));
                req.getRequestDispatcher("/Record/reader.jsp").forward(req, resp);

            } catch (IOException e) {
                System.out.println("error");
                String line = "";
                e.printStackTrace();
            }
        }else{
            resp.getWriter().println("Access Denied");
        }
    }
}
