package com.audit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CalHash {
    public static String AlgorithmType = "SHA-256";
    public static String Hash(String message)
    {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(AlgorithmType);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(hex);
        }

        return stringBuilder.toString();
    }
}
