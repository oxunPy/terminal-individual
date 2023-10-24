package com.example.rest.utils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoder {
    public static String convert(String original) {
        try {
            StringBuilder hexString = new StringBuilder();
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            byte messageDigest[] = algorithm.digest(original.getBytes("UTF-8"));

            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", 0xFF & b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            System.out.println("Error converting user password \n" + ex);
        }

        return "";
    }

    public static void main(String[] args) {
        System.out.println(convert("1"));
    }
}


