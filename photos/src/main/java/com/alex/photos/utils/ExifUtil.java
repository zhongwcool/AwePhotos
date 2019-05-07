package com.alex.photos.utils;

import java.nio.charset.StandardCharsets;

public class ExifUtil {
    private static String HEX_BASE = "0123456789ABCDEF";

    public static String encodeCN(String data) {
        byte[] bytes;
        bytes = data.getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        for (byte aByte : bytes) {
            sb.append(HEX_BASE.charAt((aByte & 0xf0) >> 4));
            sb.append(HEX_BASE.charAt((aByte & 0x0f)));
        }
        return sb.toString();
    }

    /**
     * 字符串转换成为16进制(无需Unicode编码)
     *
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = HEX_BASE.toCharArray();
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     *
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = HEX_BASE.indexOf(hexs[2 * i]) * 16;
            n += HEX_BASE.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String encodeStr(String data) {
        StringBuilder result = new StringBuilder();
        byte[] bytes;
        bytes = data.getBytes(StandardCharsets.UTF_8);
        for (byte aByte : bytes) {
            result.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * 判定是否为中文汉字
     *
     * @param data
     * @return
     */
    public static boolean isCN(String data) {
        boolean flag = false;
        String regex = "^[\u4e00-\u9fa5]*$";
        if (data.matches(regex)) {
            flag = true;
        }
        return flag;
    }

    public static String getHexResult(String targetStr) {
        StringBuilder hexStr = new StringBuilder();
        int len = targetStr.length();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                char tempStr = targetStr.charAt(i);
                String data = String.valueOf(tempStr);
                if (isCN(data)) {
                    hexStr.append(encodeCN(data));
                } else {
                    hexStr.append(encodeStr(data));
                }
            }
        }
        return hexStr.toString();
    }
}
