package com.audit;

import com.entity.vo.AuditMsgModel;
import com.mysql.cj.Session;
import com.util.AES;

import javax.servlet.http.HttpServlet;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.io.FileNotFoundException;
import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;


public class Block{
    private static String zero_block = "CSCI531_CRYPTO_FINALPROJECT_ANYUYANG+XUTENGYUAN";
    private static String usl = "jdbc:mysql://auditlog.cd1jsfvwnh46.us-west-1.rds.amazonaws.com:3306/AuditLog";
    private static String  user = "admin";
    private static String password = "1vMHZp6RuPpfu2T5fuFk";
    public static String folderName = "/Users/yuanxuteng/Desktop/block/";
    public static int Auditcompany1 = 6;
    public static int Auditcompany2 = 17;
    public static int Auditcompany3 = 18;
    public static String Audit1name = "xuteng";
    public static String Audit2name = "USC";
    public static String Audit3name = "AnyuYang";
    AuditLog myauditLog;
    int myBlockId;
    long mytimestamp;
    String myHash;

    public Block(AuditLog auditLog,int SignerID,String Signer_name)
    {
        this.myauditLog = auditLog;
        this.myBlockId = getID()+1;
        this.mytimestamp = System.currentTimeMillis() ;
        String times = String.valueOf(this.mytimestamp);
        int patientID = auditLog.patientID;
        String content = auditLog.combinedmess;
        String lastHash = getHash(this.myBlockId-1);
        DigitalKeyManager digitalKeyManager = new DigitalKeyManager(SignerID,Signer_name,"sk");
        digitalKeyManager.getKeyfromDB(SignerID,Signer_name,"pk");
        String temp_block = Integer.toString(this.myBlockId) + "||" + times + "||" +
                Integer.toString(patientID) + "||" + content + "||" + lastHash;
        System.out.println(temp_block);
        byte[] bytesignature = DigitalSign.digitalSign(digitalKeyManager.getECDSAsk(),temp_block);
        String signature = DigitalSign.byte2String(bytesignature);
        String final_block = temp_block + "||" + signature + "||" + Integer.toString(SignerID) +"||"
                + Signer_name + "||" + digitalKeyManager.getECDSApk();
        System.out.println(final_block);
        writeintofile(this.myBlockId,final_block);
        this.myHash = CalHash.Hash(final_block);

        //add into DB
        add2DB();
    }

    public static int getID()
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int IDnumber = -1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(usl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT COUNT(*) FROM auditentry";
        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if(resultSet.next())
            {
                IDnumber = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return IDnumber;
    }
    public static String getHash(int ID)
    {
        if(ID == 0)
        {
            return CalHash.Hash(zero_block);
        }
        if(ID < 0)
        {
            return null;
        }
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(usl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT Hash FROM auditentry where AUDItID=?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1,ID);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if(resultSet.next())
            {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private static void writeintofile(int BlockID,String blockContent)
    {
        String filename = Integer.toString(BlockID) + ".auditlog";
        filename = folderName + filename;
        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter = new FileWriter(filename);
            fileWriter.write(blockContent);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void add2DB()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(usl,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*
            Auditid INT AUTO_INCREMENT PRIMARY KEY,
            timestamp BIGINT UNSIGNED,
            PatientID INT UNSIGNED,
            Hash VARCHAR(64),
            PatientKey VARCHAR(600),
            AuditKey1 VARCHAR(600),
            AuditKey2 VARCHAR(600),
            AuditKey3 VARCHAR(600)
        * */
        String sql = "INSERT INTO auditentry (Auditid,timestamp,PatientID,Hash,PatientKey," +
                "AuditKey1,AuditKey2,AuditKey3) VALUES (?,?,?,?,?,?,?,?);";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.setInt(1,this.myBlockId);
            statement.setLong(2,this.mytimestamp);
            statement.setInt(3,this.myauditLog.patientID);
            statement.setString(4,this.myHash);
            String AESkey = this.myauditLog.AESkey;
            RSAKeyManager keys = new RSAKeyManager(this.myauditLog.patientID,
                    this.myauditLog.patientname,2,"pk");
            byte[] cipherinbyte = PKencryption.RSAEncrypt(keys.getRSApk(),AESkey);
            String ciphertext = PKencryption.byte2String(cipherinbyte);
            statement.setString(5,ciphertext);
            keys = new RSAKeyManager(Auditcompany1,Audit1name,1,"pk");
            cipherinbyte = PKencryption.RSAEncrypt(keys.getRSApk(),AESkey);
            ciphertext = PKencryption.byte2String(cipherinbyte);
            statement.setString(6,ciphertext);
            keys = new RSAKeyManager(Auditcompany2,Audit2name,1,"pk");
            cipherinbyte = PKencryption.RSAEncrypt(keys.getRSApk(),AESkey);
            ciphertext = PKencryption.byte2String(cipherinbyte);
            statement.setString(7,ciphertext);
            keys = new RSAKeyManager(Auditcompany3,Audit3name,1,"pk");
            cipherinbyte = PKencryption.RSAEncrypt(keys.getRSApk(),AESkey);
            ciphertext = PKencryption.byte2String(cipherinbyte);
            statement.setString(8,ciphertext);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            int is_update= statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //return -1表示查了不该查的
    //return 1表示查询正确
    public static AuditMsgModel doQuery(int queryType, int queryColumn, String SK)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(usl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = null;
        //audit company query all audit log
        if(queryType == 0)
        {
            String encryptedKey = null;
            String AESKey = null;
            if(queryColumn == 1)
            {
                sql = "SELECT AuditID,AuditKey1 FROM auditentry;";
            }
            else if(queryColumn == 2)
            {
                sql = "SELECT AuditID,AuditKey2 FROM auditentry;";
            }
            else if(queryColumn == 3)
            {
                sql = "SELECT AuditID,AuditKey3 FROM auditentry;";
            }
            else {
                return new AuditMsgModel(-1, "You can only query your own Log!");
            }
            try {
                statement = connection.prepareStatement(sql);
                resultSet = statement.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                String ret = "";
                while(resultSet.next())
                {
                    int auditId = resultSet.getInt(1);
                    encryptedKey = resultSet.getString(2);
                    byte[] encrypted = PKencryption.string2Byte(encryptedKey);
                    AESKey = PKencryption.RSADecrypt(SK,encrypted);
                    String fileName = folderName + Integer.toString(auditId) + ".auditlog";
                    BufferedReader reader = null;
                    String fileContent;
                    try {
                        reader = new BufferedReader(new FileReader(fileName));
                        fileContent = reader.readLine();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String content = BlockCheck.splitFile(fileContent,3);
                    String contentInClear = AES.aes_decrypt(content,AESKey);
                    //This is the auditlog content
                    //System.out.println(contentInClear);
                    if(contentInClear.length() != 0){
                        contentInClear = contentInClear + "<br>";
                    }
                    ret = ret + contentInClear;
                }
                return new AuditMsgModel(1, ret);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            String encryptedKey = null;
            String AESKey = null;
            if(queryColumn == 1)
            {
                sql = "SELECT AuditID,AuditKey1 FROM auditentry where PatientID=?;";
            }
            else if(queryColumn == 2)
            {
                sql = "SELECT AuditID,AuditKey2 FROM auditentry where PatientID=?;";
            }
            else if(queryColumn == 3)
            {
                sql = "SELECT AuditID,AuditKey3 FROM auditentry where PatientID=?;";
            }
            else {
                sql = "SELECT AuditID,Patientkey FROM auditentry where PatientID=?;";
            }
            try {
                statement = connection.prepareStatement(sql);
                statement.setInt(1, queryType);
                resultSet = statement.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                String ret = "";
                while(resultSet.next())
                {
                    int auditId = resultSet.getInt(1);
                    encryptedKey = resultSet.getString(2);
                    byte[] encrypted = PKencryption.string2Byte(encryptedKey);
                    AESKey = PKencryption.RSADecrypt(SK,encrypted);
                    String fileName = folderName + Integer.toString(auditId) + ".auditlog";
                    BufferedReader reader = null;
                    String fileContent;
                    try {
                        reader = new BufferedReader(new FileReader(fileName));
                        fileContent = reader.readLine();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String content = BlockCheck.splitFile(fileContent,3);
                    String contentInClear = AES.aes_decrypt(content,AESKey);
                    //This is the auditlog content
                    //System.out.println(contentInClear);
                    if(contentInClear.length() != 0){
                        contentInClear = contentInClear + "<br>";
                    }
                    ret = ret + contentInClear;
                }
                return new AuditMsgModel(1, ret);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
