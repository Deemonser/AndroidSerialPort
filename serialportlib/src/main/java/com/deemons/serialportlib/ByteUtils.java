package com.deemons.serialportlib;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;


public class ByteUtils {

    private static final String HEX = "0123456789ABCDEF";

    public static byte charToByte(char c) {
        return (byte) HEX.indexOf(c);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (ByteUtils.charToByte(hexChars[pos]) << 4 | ByteUtils.charToByte(
                hexChars[pos + 1]));
        }
        return d;
    }

    /*将bytes数组转化为16进制字符串*/
    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static long bytesToLong(byte[] b) {
        if (b.length != 4) {
            return -1;
        }
        return bytesToLong(b[0], b[1], b[2], b[3]);
    }

    public static long bytesToLong(byte b1, byte b2, byte b3, byte b4) {
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 8 * 2) | ((b4 & 0xFF) << 8 * 3);
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[4];
        result[0] = (byte) (l & 0xFF);
        result[1] = (byte) (l >> 8 & 0xFF);
        result[2] = (byte) (l >> 16 & 0xFF);
        result[3] = (byte) (l >> 24 & 0xFF);
        return result;
    }

    public static int bytesToInt(byte b1, byte b2) {
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8);
    }

    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    // char[]转byte[]
    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        return bb.array();
    }

    public static byte getByte(char c) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(1);
        cb.put(c);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        byte[] tmp = bb.array();
        return tmp[0];
    }

    // byte转char
    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        return cb.array();
    }

    // byte转char
    public static char getChar(byte bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        char[] tmp = cb.array();

        return tmp[0];
    }

    /**
     * 字符串转字节
     *
     * @param s 字符串
     * @return 字节
     */
    public static byte[] stringToBytes(String s) {
        return stringToBytes(s, 0);
    }

    /**
     * 字符串转字节
     *
     * @param s 字符串
     * @param length 字节长度，不够补0
     */
    public static byte[] stringToBytes(String s, int length) {
        byte[] src =
            ByteUtils.getBytes((s == null || s.length() == 0) ? new char[0] : s.toCharArray());

        if (length < src.length) {
            length = src.length;
        }

        byte[] bytes = new byte[length];
        for (int i = 0; i < src.length; i++) {
            bytes[i] = src[i];
        }
        return bytes;
    }

    public static byte[] copy(byte[] src, int srcStart, int length) {
        byte[] result = new byte[length];
        System.arraycopy(src, srcStart, result, 0, length);
        return result;
    }

    public static byte[] reverse(byte[] src) {
        for (int i = 0; i <= src.length / 2 - 1; i++) {
            byte temp1 = src[i];
            byte temp2 = src[src.length - i - 1];
            src[i] = temp2;
            src[src.length - i - 1] = temp1;
        }
        return src;
    }
}
