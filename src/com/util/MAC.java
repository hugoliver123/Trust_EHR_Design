package com.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MAC {
    public static String readFileToString(String fileName) {
        byte[] encodedBytes = new byte[0];
        try {
            encodedBytes = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(encodedBytes);
    }
    public static String FileMAC(String inputstring)
    {
        byte[] key = "CSCI531CRYPTO".getBytes();
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SecretKeySpec keySpec = new SecretKeySpec(key,"HmacSHA256");
        try {
            mac.init(keySpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        mac.update(inputstring.getBytes(StandardCharsets.UTF_8));
        byte[] macvalue = mac.doFinal();
        return bytestohex(macvalue);
    }
    private static String bytestohex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
