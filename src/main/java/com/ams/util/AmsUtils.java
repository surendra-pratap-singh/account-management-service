package com.ams.util;

import java.security.SecureRandom;

public class AmsUtils {

    public static Long generateUniqueId() {
        SecureRandom secureRandom = new SecureRandom();
        return Math.abs(secureRandom.nextLong());
    }

    public static String maskData(Long accountNo) {
        return String.valueOf(accountNo).replaceAll("\\d(?=\\d{4})", "*");
    }

}
