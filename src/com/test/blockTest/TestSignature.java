package com.test.blockTest;

import com.audit.DigitalKeyManager;
import com.audit.DigitalSign;
import com.audit.RSAKeyManager;

public class TestSignature {
    public static void main(String[] args) {
        DigitalKeyManager digitalKeyManager = new DigitalKeyManager(3, "coco", "PK");
        digitalKeyManager.getKeyfromDB(3,"coco","SK");
        String msg = "我灰太狼一定会回来的";
        byte [] bytess = DigitalSign.digitalSign(digitalKeyManager.getECDSAsk(),msg); //给msg签名，返回一个byte
        String string = DigitalSign.byte2String(bytess);
        System.out.println(string); //将byte转换为string,写入文件
        boolean torf = DigitalSign.verify(digitalKeyManager.getECDSApk(), msg, DigitalSign.string2Byte(string));
        System.out.println(torf);
    }
}
