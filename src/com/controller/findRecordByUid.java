package com.controller;

import com.entity.RecordFile;
import com.entity.User;
import com.service.RecordFileFinder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class findRecordByUid extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int findId = Integer.parseInt(req.getParameter("pName"));

        ArrayList<RecordFile> allFile= RecordFileFinder.getAllEhrFiles();
        ArrayList<RecordFile> targetFile = new ArrayList<>();
        for(RecordFile recordFile: allFile){
            if(recordFile.getPatientId() == findId){
                targetFile.add(recordFile);
            }
        }
        System.out.println(targetFile.toString());
        req.setAttribute("pRrdList", targetFile);
        if(user.getUserPosition().equals("D")){
            req.getRequestDispatcher("PositionWelcome/careProvider.jsp").forward(req, resp);
        }else if(user.getUserPosition().equals("admin")){
            req.getRequestDispatcher("PositionWelcome/administrator.jsp").forward(req, resp);
        }else{
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
