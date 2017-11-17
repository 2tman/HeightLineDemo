//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package iandroid.club.heightlinedemo.utils;

import java.io.UnsupportedEncodingException;

public final class EncodingUtils {
    public static String getString(byte[] data, int offset, int length, String charset) {


        try {
            return new String(data, offset, length, charset);
        } catch (UnsupportedEncodingException var5) {
            return new String(data, offset, length);
        }
    }

    public static String getString(byte[] data, String charset) {

        return getString(data, 0, data.length, charset);
    }

    public static byte[] getBytes(String data, String charset) {


        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException var3) {
            return data.getBytes();
        }
    }

    public static byte[] getAsciiBytes(String data) {


        try {
            return data.getBytes(Consts.ASCII.name());
        } catch (UnsupportedEncodingException var2) {
            throw new Error("ASCII not supported");
        }
    }

    public static String getAsciiString(byte[] data, int offset, int length) {


        try {
            return new String(data, offset, length, Consts.ASCII.name());
        } catch (UnsupportedEncodingException var4) {
            throw new Error("ASCII not supported");
        }
    }

    public static String getAsciiString(byte[] data) {

        return getAsciiString(data, 0, data.length);
    }

    private EncodingUtils() {
    }
}
