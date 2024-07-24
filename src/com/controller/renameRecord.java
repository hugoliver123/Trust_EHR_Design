package com.controller;

import com.audit.AuditLog;
import com.audit.Block;
import com.entity.User;
import com.mapper.userMapper;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class renameRecord extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String target = req.getParameter("fileName");

        SqlSession sqlSessionsession = getSqlSession.creatSqlSession();
        userMapper user_mapper = sqlSessionsession.getMapper(userMapper.class);
        String builderName = user_mapper.findBuilder(target);

        if(user.getUserPosition().equals("admin") || builderName.equals(user.getUserName())){
            try {
                if(rename(target)){

                    resp.getWriter().println("<!DOCTYPE html>\n" +
                            "<html lang=\"en\">" + "\n" +
                            "<body>");
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = dateFormat.format(date);     //時間和日期

                    int patientId = Integer.parseInt(target.split("-")[0]);

                    SqlSession sqlsession = getSqlSession.creatSqlSession();
                    userMapper user_mapper1 = sqlsession.getMapper(userMapper.class);
                    User userPatient = user_mapper1.queryUserById(patientId);


                    AuditLog auditLog = new AuditLog(formattedDate,user.getUserId(), req.getRemoteAddr(), "DEL",  target,
                            patientId, userPatient.getUserName());
                    Block block = new Block(auditLog, 110, "police"); //权威者签名，提交
//                    System.out.println(formattedDate + user.getUserId() + req.getRemoteAddr() + "DEL" +  target +
//                            patientId + userPatient.getUserName());


                    resp.getWriter().println("delete success");
                    resp.getWriter().println("<form action=\"backMain\" method=\"post\">\n" +
                            "        <input type=\"submit\" value=\"back\">\n" +
                            "    </form>");
                }else {
                    resp.getWriter().println("<!DOCTYPE html>\n" +
                            "<html lang=\"en\">" + "\n" +
                            "<body>");
                    resp.getWriter().println("delete fail");
                    resp.getWriter().println("<form action=\"backMain\" method=\"post\">\n" +
                            "        <input type=\"submit\" value=\"back\">\n" +
                            "    </form>");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            resp.getWriter().println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">" + "\n" +
                    "<body>");
            resp.getWriter().println("delete fail, You do not have permission to delete this record.");
            resp.getWriter().println("<form action=\"backMain\" method=\"post\">\n" +
                    "        <input type=\"submit\" value=\"back\">\n" +
                    "    </form>");
        }
    }

    public static boolean rename(String str) throws Exception{
        String path = "/Users/yuanxuteng/Desktop/EHRrecord/";
        File oldName = new File(path+str);
        // 新的文件或目录
        File newName = new File(path+str+".disabled");
        if (newName.exists()) {  //  确保新的文件名不存在
            throw new java.io.IOException("file exists");
        }
        if(oldName.renameTo(newName)) {
            return true;
        } else {
            return false;
        }
    }
}
