package org.code_revue.knavery.tags;

/**
 * @author Mike Fanning
 */
public class ByteArrayUtils {

    public static int byteToUnsignedInt(byte b) {
        return b & 0xff;
    }

}
