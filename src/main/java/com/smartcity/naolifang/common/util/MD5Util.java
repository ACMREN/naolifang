package com.smartcity.naolifang.common.util;

import org.springframework.util.DigestUtils;

import java.util.Random;

public class MD5Util {
    private final static String basesCode = "0123456789abcdefghjkmnopqrstuvwxyz";

    public static String generateSalt() {
        StringBuilder sb = new StringBuilder(30);
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            sb.append(basesCode.charAt(random.nextInt(34)));
        }
        return sb.toString();
    }

    public static String passwordMd5Encode(String password, String salt) {
        String temp = password + salt;
        String dbPassword = DigestUtils.md5DigestAsHex(temp.getBytes());
        return dbPassword;
    }
}
