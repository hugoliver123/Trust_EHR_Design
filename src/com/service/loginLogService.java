package com.service;

import java.io.FileWriter;
import java.io.IOException;

public class loginLogService {
    public static void loginLogService(String str){
        String fileName = "/Users/yuanxuteng/Desktop/log/userLogin.csv";
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(str);
            writer.close();
            System.out.println("文本成功附加到文件 " + fileName);
        } catch (IOException e) {
            System.out.println("发生异常 " + e);
            e.printStackTrace();
        }
    }
}
