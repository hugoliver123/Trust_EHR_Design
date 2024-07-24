package com.controller;

import com.audit.BlockCheck;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class checkSignature extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int logId = Integer.parseInt(req.getParameter("logID"));
        int stateCode = 100;
        try{
            stateCode = BlockCheck.SignatureCheck(logId);
        }catch (Exception e){
            stateCode = 100;
        }
        String recMsg = "";
        switch (stateCode) {
            case 0:
                recMsg = "Missing block or files broken";
                break;
            case 1:
                recMsg = "OK, Signature test passed";
                break;
            case 2:
                recMsg = "unknown issuer";
                break;
            case 3:
                recMsg = "Issuer's Public Key do not match";
                break;
            case 4:
                recMsg = "Invalid Signature";
                break;
            default:
                recMsg = "Perform Error!";
                break;
        }
        req.setAttribute("recMsg", recMsg);
        req.setAttribute("LogIdrec", logId);
        req.getRequestDispatcher("Record/auditReader.jsp").forward(req, resp);
    }
}
