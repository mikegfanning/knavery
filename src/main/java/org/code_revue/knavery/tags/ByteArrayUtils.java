package org.code_revue.knavery.tags;

import java.io.UnsupportedEncodingException;

/**
 * @author Mike Fanning
 */
public class ByteArrayUtils {

    public static int byteArrayToInt(byte[] bytes) {
        return bytes[3] & 0xFF | (bytes[2] & 0xFF) << 8 | (bytes[1] & 0xFF) << 16 | (bytes[0] & 0xFF) << 24;
    }

    public static String byteArrayToString(byte[] bytes) {
        return new String(bytes);
    }

    public static String byteArrayToString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
        return new String(bytes, charsetName);
    }

}
