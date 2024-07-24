package com.audit;

import java.security.KeyPair;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DigitalKeyManager {
    private static String usl = "jdbc:mysql://auditlog.cd1jsfvwnh46.us-west-1.rds.amazonaws.com:3306/KeyManager";
    private static String  user = "admin";
    private static String password = "1vMHZp6RuPpfu2T5fuFk";
    private String ECDSApk;
    private String ECDSAsk;
    public String getECDSApk(){return this.ECDSApk;}
    public String getECDSAsk(){return this.ECDSAsk;}
    public DigitalKeyManager(int ID, String name, String PKorSK) {
        //1 means pk, 2 means sk
        int PorS;
        PKorSK = PKorSK.toUpperCase(Locale.ROOT);
        if(PKorSK.equals("pk"))
        {
            PorS = 1;
        }
        else if(PKorSK.equals("PK"))
        {
            PorS = 1;
        } else if (PKorSK.equals("sk"))
        {
            PorS = 2;
        } else if (PKorSK.equals("SK"))
        {
            PorS = 2;
        }
        else
        {
            return;
        }
        if(!getKeyfromDB(ID,name,PKorSK))
        {
            KeyPair keyPair = DigitalSign.genKeyPair();
            this.ECDSApk = PKencryption.pk2String(keyPair.getPublic());
            this.ECDSAsk = PKencryption.sk2String(keyPair.getPrivate());
            addintoDB(ID,name,this.ECDSApk,this.ECDSAsk);
        }
    }
    public boolean getKeyfromDB(int ID, String name, String PKorSK)
    {
        PKorSK = PKorSK.toUpperCase(Locale.ROOT);
        int PorS;
        String PorSkey = null;
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
        if(PKorSK.equals("pk"))
        {
            PorS = 1;
            sql = "SELECT ECDSApk FROM ECDSAkeys WHERE ID=? AND Name=?;";
        }
        else if(PKorSK.equals("PK"))
        {
            PorS = 1;
            sql = "SELECT ECDSApk FROM ECDSAKeys WHERE ID=? AND Name=?;";
        }
        else if(PKorSK.equals("sk"))
        {
            PorS = 2;
            sql = "SELECT ECDSAsk FROM ECDSAKeys WHERE ID=? AND Name=?;";
        }
        else if(PKorSK.equals("SK"))
        {
            PorS = 2;
            sql = "SELECT ECDSAsk FROM ECDSAKeys WHERE ID=? AND Name=?;";
        }
        else {
            return false;
        }
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1,ID);
            statement.setString(2,name);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if(resultSet.next())
            {
                PorSkey = resultSet.getString(1);
            }
            else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(PorS == 1)
        {
            this.ECDSApk = PorSkey;
            return true;
        } else if (PorS == 2)
        {
            this.ECDSAsk = PorSkey;
            return true;
        }
        return false;

    }
    private void addintoDB(int userID, String username, String userPK, String userSK){
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
        String sql = "INSERT INTO ECDSAKeys (id,name,ECDSApk,ECDSAsk) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.setInt(1,userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.setString(2,username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.setString(3,userPK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            statement.setString(4,userSK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            int is_update= statement.executeUpdate();
            //statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


