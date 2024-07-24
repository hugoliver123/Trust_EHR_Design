package com.controller;

import com.entity.vo.messageModel;
import com.service.loginLogService;
import com.service.sendUrlGet;
import com.service.userService;
import com.util.Merkle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class userLogin extends HttpServlet {
    private userService user_service = new userService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uname = req.getParameter("userid");
        String upwd = req.getParameter("password"); //获取前端的用户名和密码
        System.out.println(upwd);
        HttpSession saltSession = req.getSession();
        String ssalt = (String) saltSession.getAttribute("salt");//獲取saltSession中的salt

        messageModel message_model = user_service.userLogin(uname, upwd, ssalt); //请求user service服务

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 使用SimpleDateFormat对象的format()方法将Date对象格式化为指定格式的字符串
        String formattedDate = dateFormat.format(date);

        String url = "https://api.ipgeolocation.io/ipgeo?fields=geo&apiKey=8f55462ff25c4125ab3f4d801b0593df&ip="+req.getRemoteAddr();
        String geo="";
        if(req.getRemoteAddr().startsWith("10.") || req.getRemoteAddr().startsWith("127.0")
                || req.getRemoteAddr().startsWith("192.168.") || req.getRemoteAddr().startsWith("172.")){
            geo = "{Local Network}";
        }else {
            geo = sendUrlGet.sendGet(url); //要錢的，測試注釋掉這一行
        }
        String content = formattedDate + "," + message_model.getCode() + "," + uname + "," + req.getRemoteAddr() + "," + geo +"\n";
        loginLogService.loginLogService(content); //记录log， 有状态码，尝试的用户名，日期和来源ip

        Merkle merkle = new Merkle("/Users/yuanxuteng/Desktop/EHRrecord",
                "/Users/yuanxuteng/Desktop/log/tree.root");

        if(message_model.getCode() == 1) {
            HttpSession session1 = req.getSession();
            // System.out.println("administrator");
            session1.setAttribute("user", message_model.getObject());

            Boolean integrity_check = merkle.checkRoot();
            session1.setAttribute("integrity", integrity_check);
            req.getRequestDispatcher("PositionWelcome/administrator.jsp").forward(req, resp);
        }
        else if(message_model.getCode() == 2) {
            // System.out.println("doctor");
            if(merkle.checkRoot()){
                HttpSession session1 = req.getSession();
                session1.setAttribute("user", message_model.getObject());
                req.getRequestDispatcher("PositionWelcome/careProvider.jsp").forward(req, resp);
            }else {
                message_model.setMsg("Integrity Check failed, please contact administrator !");
                req.setAttribute("messageModel", message_model);
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        }
        else if(message_model.getCode() == 3) {
            // System.out.println("Patients");
            if(merkle.checkRoot()){
                HttpSession session1 = req.getSession();
                session1.setAttribute("user", message_model.getObject());
                req.getRequestDispatcher("PositionWelcome/patients.jsp").forward(req, resp);
            }else {
                message_model.setMsg("Integrity Check failed, please contact administrator !");
                req.setAttribute("messageModel", message_model);
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        }
        else if(message_model.getCode() == 4) {
            merkle.updateRoot();
            message_model.setMsg("Integrity Check: Merkle Tree Root Rested !");
            req.setAttribute("messageModel", message_model);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
        else{
            System.out.println(message_model.getMsg());
            req.setAttribute("messageModel", message_model);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
