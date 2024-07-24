package com.test.blockTest;

import com.audit.AuditLog;
import com.audit.Block;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestBlock {
    public static void main(String[] args) {
        for(int i = 0; i<10; i++){
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 使用SimpleDateFormat对象的format()方法将Date对象格式化为指定格式的字符串
            String formattedDate = dateFormat.format(date);

            AuditLog auditLog = new AuditLog(formattedDate,1, "192.168.0.1", "READ", "2-1.ehrd",
                    4, "coco");
            Block block = new Block(auditLog, 110, "police");

        }
    }
}
