package com.util;

public class StringUtil {
        // 检查空字符串
    public static boolean isEmpty(String str){
        if (str == null || "".equals(str.trim())){
            return true;
        }else {
            return false;
        }
    }
}
