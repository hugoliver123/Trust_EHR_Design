package com.audit;

import com.entity.vo.AuditMsgModel;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockCheck {
    private static String zero_block = "CSCI531_CRYPTO_FINALPROJECT_ANYUYANG+XUTENGYUAN";
    private static String usl = "jdbc:mysql://auditlog.cd1jsfvwnh46.us-west-1.rds.amazonaws.com:3306/KeyManager";
    private static String  user = "admin";
    private static String password = "1vMHZp6RuPpfu2T5fuFk";
    public static String folderName = "/Users/yuanxuteng/Desktop/block/";

    public static String splitFile(String fileContent, int i) {
        String[] splits = fileContent.split("\\|\\|");
        if (splits.length >= i + 1) {
            return splits[i];
        } else {
            return null;
        }
    }

    public static int BlockCheck() {
        //-1表示filder无file，0表示某个本应该存在的file不存在或者文件内容有问题，1表示true，2表示false
        File folder = new File(folderName);
        File[] files = folder.listFiles();

        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (!file.getName().equals(".DS_Store")) {
                fileList.add(file);
            }
        }
        files = fileList.toArray(new File[fileList.size()]);

        if (files == null) {
            return -1;
        }
        int s = files.length;
        String fileName = null;
        BufferedReader reader = null;
        String fileContent = null;
        String lastHash = CalHash.Hash(zero_block);
        String lastHashnew = null;
        for (int i = 1; i <= s; i++) {
            fileName = folderName + Integer.toString(i) + ".auditlog";
            try {
                reader = new BufferedReader(new FileReader(fileName));
            } catch (FileNotFoundException e) {
                return 0;
            }
            try {
                fileContent = reader.readLine();
            } catch (IOException e) {
                return 0;
            }
            System.out.println(fileContent);
            lastHashnew = splitFile(fileContent, 4);
            System.out.println(Block.getHash(i - 1));
            System.out.println(lastHashnew);
            System.out.println(lastHash);
            if (lastHashnew.equals(lastHash)) {
                lastHash = CalHash.Hash(fileContent);
            } else {
                return 2;
            }
        }
        System.out.println("last step");
        String lastBlock = Block.getHash(s);
        System.out.println(Block.getHash(s));
        lastHash = CalHash.Hash(fileContent);
        System.out.println(lastHash);
        if (lastHash.equals(lastBlock)) {
            return 1;
        } else {
            return 2;
        }

    }

    //0表示某个本应该存在的file不存在或者文件内容有问题，
    //1表示签名是对的
    //2表示issuer不存在
    //3表示issuer的pk与数据库对不上
    //4表示签名不对
    public static int SignatureCheck(int auditLogID) {

        String fileName = folderName + Integer.toString(auditLogID) + ".auditlog";
        BufferedReader reader = null;
        String fileContent = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            return 0;
        }
        try {
            fileContent = reader.readLine();
        } catch (IOException e) {
            return 0;
        }
        String signature = splitFile(fileContent, 5);
        String issuerID = splitFile(fileContent, 6);
        String issuerName = splitFile(fileContent, 7);
        String issuerPK = splitFile(fileContent, 8);
//        System.out.println("signature is:" + signature);
//        System.out.println("issuerID is:" + issuerID);
//        System.out.println("issuerName is:" + issuerName);
//        System.out.println("pk is:" + issuerPK);
        String pkInDB = getSignPK(Integer.parseInt(issuerID), issuerName);
        if (pkInDB == null) {
            //System.out.println("The corresponding issuer doesn't exist");
            return 2;
        }
        if (pkInDB.equals(issuerPK)) {
            //String temp_block = Integer.toString(this.myBlockId) + "||" + times + "||" +
            //                Integer.toString(patientID) + "||" + content + "||" + lastHash;
            String message = splitFile(fileContent, 0) + "||" + splitFile(fileContent, 1) + "||"
                    + splitFile(fileContent, 2) + "||" + splitFile(fileContent, 3) + "||" +
                    splitFile(fileContent, 4);
            String sigg = splitFile(fileContent,5);
            byte[] sig = DigitalSign.string2Byte(sigg);
            boolean iscorret = DigitalSign.verify(pkInDB, message, sig);
            if (iscorret) {
                return 1;
            } else {
                return 4;
            }
        } else {
            //System.out.println("The pk in Block is different from pk in DB");
            return 3;
        }

    }

    /*
    CREATE TABLE ECDSAKeys(
    id INT UNSIGNED PRIMARY KEY,
    name VARCHAR(30),
    ECDSApk VARCHAR(200),
    ECDSAsk VARCHAR(200)
);
     */
    public static String getSignPK(int ID, String issuerName) {
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
        String sql = "SELECT ECDSApk FROM ECDSAKeys where id=? AND name=?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, ID);
            statement.setString(2, issuerName);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //return 0表示DB中查无此人。
    //如果查询者是病人，query只能为自己的ID，否则报错，返回-1
    //如果成功 return1

    //如果查询者是audit company类型，那么query = 0表示查询所有人，否则就是查ID 为query的人
    public static AuditMsgModel queryLog(int queryId, String queryName, int query) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String rsaSk =null;
        int personType = 0;
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
        String sql = "SELECT persontype, RSASK FROM RSAKeys where id=? AND name=?;";
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, queryId);
            statement.setString(2, queryName);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (resultSet.next()) {
                personType = resultSet.getInt(1);
                rsaSk = resultSet.getString(2);
            }
            else
            {
                return new AuditMsgModel(0, "Not found your Log in Database");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(personType == 1) //company
        {
            int queryColumn = 0;
            if(queryId == Block.Auditcompany1)
            {
                queryColumn = 1;
            } else if (queryId == Block.Auditcompany2) {
                queryColumn = 2;
            }
            else if (queryId == Block.Auditcompany3) {
                queryColumn = 3;
            }
            else
            {
                return new AuditMsgModel(0, "Not found your Log in Database");
            }
            return Block.doQuery(query,queryColumn,rsaSk);

        }
        else  //patient
        {
            if(queryId != query)
            {
                return new AuditMsgModel(-1, "You can only query your own Log!");
            }
            return Block.doQuery(query,4,rsaSk);
        }

    }


}







