package org.code_revue.knavery.tags;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * @author Mike Fanning
 */
public class ByteArrayUtils {

    public static int byteArrayToInt(byte[] bytes) {
        return bytes[3] & 0xFF | (bytes[2] & 0xFF) << 8 | (bytes[1] & 0xFF) << 16 | (bytes[0] & 0xFF) << 24;
    }

    public static String byteArrayToNumericString(byte[] bytes) {
        return (new BigInteger(bytes)).toString();
    }

    public static String byteArrayToString(byte[] bytes) {
        return new String(bytes);
    }

    public static String byteArrayToString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
        return new String(bytes, charsetName);
    }

    public static int byteToUnsignedInt(byte b) {
        return b & 0xff;
    }

    public static byte[] intArrayToByteArray(int[] data) {
        byte[] b = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            b[i] = (byte) data[i];
        }
        return b;
    }

    public static byte[] intToByteArray(int i) {
        return new byte[]{
                (byte) ((i >> 24) & 0xff),
                (byte) ((i >> 16) & 0xff),
                (byte) ((i >> 8) & 0xff),
                (byte) (i & 0xff)
        };
    }

    public static void numericStringToByteArray(String number, byte[] bytes) {
        byte[] b = (new BigInteger(number)).toByteArray();
        int lDiff = bytes.length - b.length;
        for (int i = bytes.length - 1; i >= 0; i--) {
            int bIndex = i - lDiff;
            if (bIndex < 0) {
                bytes[i] = 0;
            } else {
                bytes[i] = b[bIndex];
            }
        }
    }

}
