package com.audit;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


//using RSA
//RSA/ECB/PKCS1Padding
public class PKencryption {
    private static final String RSA_Algorithm = "RSA";
    private static final String specific_RSA_Algorithm = "RSA/ECB/PKCS1Padding";
    private static final int KEY_SIZE = 2048;
    public static KeyPair genKeyPair()
    {
        try {
            return KeyPairGenerator.getInstance(RSA_Algorithm).generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] RSAEncrypt(String RSApkvar, String message)
    {
        PublicKey RSApk = PKencryption.string2PK(RSApkvar);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(specific_RSA_Algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, RSApk);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] encryptedBytes = null;
        try {
             encryptedBytes = cipher.doFinal(message.getBytes());
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return encryptedBytes;
    }
    public static String RSADecrypt(String RSAskvar, byte[] ciphertext)
    {
        PrivateKey RSAsk = PKencryption.string2SK(RSAskvar);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, RSAsk);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] originalMessage = new byte[0];
        try {
            originalMessage = cipher.doFinal(ciphertext);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        String abcd = new String(originalMessage);
        return abcd;
    }
    public static String pk2String(PublicKey pkvar)
    {
        return Base64.getEncoder().encodeToString(pkvar.getEncoded());
    }
    public static String sk2String(PrivateKey skvar)
    {
        return Base64.getEncoder().encodeToString(skvar.getEncoded());
    }
    public static PublicKey string2PK(String pkstringvar)
    {
        byte[] pkbytes = Base64.getDecoder().decode(pkstringvar);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pkbytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
    public static PrivateKey string2SK(String skstringvar)
    {
        byte[] sk = Base64.getDecoder().decode(skstringvar);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(sk);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
    public static String byte2String(byte[] bytevar)
    {
        return Base64.getEncoder().encodeToString(bytevar);
    }
    public static byte[] string2Byte(String strvar)
    {
        return Base64.getDecoder().decode(strvar);
    }

}
