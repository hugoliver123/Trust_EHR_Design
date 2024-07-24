package com.audit;

import java.awt.image.Kernel;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DigitalSign {
    //key gen, digital sign, verify
    public static KeyPair genKeyPair()
    {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        ECGenParameterSpec ecgpSpec = new ECGenParameterSpec("secp384r1");
        try {
            kpg.initialize(ecgpSpec);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        return kpg.generateKeyPair();
    }
    public static byte[] digitalSign(String skvar, String message)
    {
        PrivateKey sk = string2SK(skvar);
        Signature signature = null;
        try {
            signature = Signature.getInstance("SHA384withECDSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            signature.initSign(sk);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] minbyte  = new byte[0];
        try {
            minbyte = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try {
            signature.update(minbyte);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }

        try {
            return signature.sign();
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean verify(String pkvar, String mess, byte[] signature)
    {
        PublicKey pk = string2PK(pkvar);
        Signature signature1 = null;
        try {
            signature1 = Signature.getInstance("SHA384withECDSA");
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
        try {
            signature1.initVerify(pk);
        } catch (InvalidKeyException e) {
            return false;
        }
        byte[] message = new byte[0];
        try {
            message = mess.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        try {
            signature1.update(message);
        } catch (SignatureException e) {
            return false;
        }
        try {
            return signature1.verify(signature);
        } catch (SignatureException e) {
            return false;
        }
    }

    //format converting
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
            keyFactory = KeyFactory.getInstance("EC");
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
            keyFactory = KeyFactory.getInstance("EC");
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
