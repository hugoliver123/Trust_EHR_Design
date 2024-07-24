package com.controller.operationRecord;

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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class copyRecord extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user"); // 获取Session User
        String fileName = (String) session.getAttribute("recordFileName");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(date);     //時間和日期

        int patientId = Integer.parseInt(fileName.split("-")[0]);

        SqlSession sqlsession = getSqlSession.creatSqlSession();
        userMapper user_mapper = sqlsession.getMapper(userMapper.class);
        User userPatient = user_mapper.queryUserById(patientId);

        AuditLog auditLog = new AuditLog(formattedDate,user.getUserId(), req.getRemoteAddr(), "COPY", fileName,
                patientId, userPatient.getUserName());
        Block block = new Block(auditLog, 110, "police"); //权威者签名，提交

        req.getRequestDispatcher("/Record/reader.jsp").forward(req, resp);
    }
}