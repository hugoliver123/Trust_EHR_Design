package com.test.blockTest;

import com.audit.PKencryption;
import com.audit.RSAKeyManager;

public class TestRSA {
    public static void main(String[] args) {
        RSAKeyManager rsaKeyManager = new RSAKeyManager(2, "bob",2, "pK");
        rsaKeyManager.getKeyfromDB(2,"bob","Sk");
        String mes = "hahahhahah";
        byte [] bytesss = PKencryption.RSAEncrypt(rsaKeyManager.getRSApk(), mes); //加密后的byte 格式密文
        String cipher = PKencryption.byte2String(bytesss);      //密文转换成String存储在区块链中
        System.out.println(PKencryption.RSADecrypt(rsaKeyManager.getRSAsk(), PKencryption.string2Byte(cipher)));
        //解密后的是String格式
    }
}
