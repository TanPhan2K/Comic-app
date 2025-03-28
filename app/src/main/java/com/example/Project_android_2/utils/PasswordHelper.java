package com.example.Project_android_2.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHelper {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] passwordBytes = password.getBytes();
            byte[] hashedBytes = md.digest(passwordBytes);

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
