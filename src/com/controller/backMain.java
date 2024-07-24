package com.controller;

import com.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class backMain extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println(user.getUserPosition());
        if(user.getUserPosition().equals("D")){
            req.getRequestDispatcher("PositionWelcome/careProvider.jsp").forward(req, resp);
        }else if(user.getUserPosition().equals("admin")){
            req.getRequestDispatcher("PositionWelcome/administrator.jsp").forward(req, resp);
        }else if(user.getUserPosition().equals("P")){
            req.getRequestDispatcher("PositionWelcome/patients.jsp").forward(req, resp);
        }else{
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
