package com.ams.util;

public class AmsUtils {

    public static Long generateUniqueId(){
        return System.currentTimeMillis();
    }

    public static String maskData(Long accountNo){
        return String.valueOf(accountNo).replaceAll("\\d(?=\\d{4})", "*");
    }

}
