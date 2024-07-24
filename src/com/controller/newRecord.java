package com.controller;

import com.audit.AuditLog;
import com.audit.Block;
import com.entity.AesDataFrame;
import com.entity.User;
import com.mapper.userMapper;
import com.service.getPatients;
import com.service.getRecordCount;
import com.service.userService;
import com.util.AES;
import com.util.MAC;
import com.util.Merkle;
import com.util.getSqlSession;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.out;

public class newRecord extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Merkle merkle = new Merkle("/Users/yuanxuteng/Desktop/EHRrecord",
                    "/Users/yuanxuteng/Desktop/log/tree.root");
            if(merkle.checkRoot()){
                int pId = Integer.parseInt(req.getParameter("pName"));
                int count = getRecordCount.getRecordCount(pId) + 1; //从数据库获取record数量
                getRecordCount.plusOneRecord(pId); //增加一个记录

                HttpSession session1 = req.getSession();
                User doctor = (User) session1.getAttribute("user");
                out.println(doctor.getUserName());

                User patient = getPatients.queryPatientByUid(pId);
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 使用SimpleDateFormat对象的format()方法将Date对象格式化为指定格式的字符串
                String formattedDate = dateFormat.format(date);

                String recordName = req.getParameter("pName") + "-" + count + ".ehrd";
                String fileName = "/Users/yuanxuteng/Desktop/EHRrecord/" + recordName;
                String content = "record ID: "+ req.getParameter("pName") + "-" + count + "\n"
                        + "Creat Time: " + formattedDate +"\n"
                        + "Care Provider Name: " + doctor.getUserName() + ", title: " + doctor.getUserPosition() + "; ID: " + doctor.getUserId() + "\n"
                        + "Patient Name: " + patient.getUserName() +"\n"
                        + "Care Record:"+ "\n" + req.getParameter("pRecord"); //生成Record内容

                String key = AES.generateRandomKey_aes128();

                content = AES.aes_encrypt(content, key); // AES encryption

                FileWriter writer = new FileWriter(fileName, true);
                String sha256 = calculateSHA256(content); // 計算內容的哈希

                SqlSession session2 = getSqlSession.creatSqlSession();
                userMapper user_mapper2 = session2.getMapper(userMapper.class);
                user_mapper2.insertAesKey(new AesDataFrame(recordName, key, doctor.getUserName(), sha256, MAC.FileMAC(content)));
                session2.commit();               //生成的AES密钥，传送给数据库

                writer.write(content);
                writer.close();
                merkle.updateRoot();
                out.println("文本成功附加到文件 " + fileName);

//                out.println(formattedDate + doctor.getUserId() + req.getRemoteAddr() + "CREATE" +  recordName +
//                        patient.getUserId() + patient.getUserName());
                AuditLog auditLog = new AuditLog(formattedDate, doctor.getUserId(), req.getRemoteAddr(), "CREATE",  recordName,
                        patient.getUserId(), patient.getUserName());
                Block block = new Block(auditLog, 110, "police"); //权威者签名，提交


                resp.getWriter().println("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">" + "\n" +
                        "<body>");
                resp.getWriter().println("<h6>Text successfully attached to file "+ recordName +"</h6>");
                //resp.getWriter().println("<a href=\"index.jsp\">back home page</a>" + "\n" + "</body>\n" +
                //        "</html>");
                resp.getWriter().println("<form action=\"backMain\" method=\"post\">\n" +
                        "        <input type=\"submit\" value=\"back\">\n" +
                        "    </form>");
            }else{
                resp.getWriter().println("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">" + "\n" +
                        "<body>");
                resp.getWriter().println("<h6>Text failed attached: Integrity check failed ! "+"</h6>");
                //resp.getWriter().println("<a href=\"index.jsp\">back home page</a>" + "\n" + "</body>\n" +
                //        "</html>");
                resp.getWriter().println("<form action=\"backMain\" method=\"post\">\n" +
                        "        <input type=\"submit\" value=\"back\">\n" +
                        "    </form>");
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            out.println("Abnormalities occur " + e);
            resp.getWriter().println("Abnormalities occur " + e);
            e.printStackTrace();
        }
    }
    public static String calculateSHA256(String inputString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // 建立SHA-256訊息摘要
        byte[] hash = digest.digest(inputString.getBytes(StandardCharsets.UTF_8)); // 計算哈希值

        // 將哈希值轉換為16進制字串
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String sha256String = hexString.toString();

        return sha256String;
    }
}
