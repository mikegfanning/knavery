package org.code_revue.knavery.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

/**
 * @author Mike Fanning
 */
@Service
public class StringConverterService {

    public static final BigInteger MAX_INT = new BigInteger(Integer.toString(Integer.MAX_VALUE));
    public static final BigInteger MIN_INT = new BigInteger(Integer.toString(Integer.MIN_VALUE));
    public static final BigInteger MAX_LONG = new BigInteger(Long.toString(Long.MAX_VALUE));
    public static final BigInteger MIN_LONG = new BigInteger(Long.toString(Long.MIN_VALUE));

    private final Pattern intPattern = Pattern.compile("-?\\d+");
    private final Pattern realPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private final Pattern ipAddressPattern = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");
    private final Pattern macAddressPattern = Pattern.compile("(\\p{XDigit}{1,2}:){5}\\p{XDigit}{1,2}");

    public boolean isInteger(String s) {

        if (!intPattern.matcher(s).matches()) {
            return false;
        }

        BigInteger num = new BigInteger(s);

        if ('-' == s.charAt(0)) {
            return num.compareTo(MIN_INT) >= 0;
        } else {
            return num.compareTo(MAX_INT) <= 0;
        }

    }

    public boolean isLong(String s) {

        if (!intPattern.matcher(s).matches()) {
            return false;
        }

        BigInteger num = new BigInteger(s);

        if ('-' == s.charAt(0)) {
            return num.compareTo(MIN_LONG) >= 0;
        } else {
            return num.compareTo(MAX_LONG) <= 0;
        }

    }

    public boolean isIpAddress(String s) {

        if (!ipAddressPattern.matcher(s).matches()) {
            return false;
        }

        String[] octets = s.split("\\.");
        for (String octet: octets) {
            if (Integer.parseInt(octet) > 255) {
                return false;
            }
        }

        return true;
    }

    public boolean isMacAddress(String s) {
        return macAddressPattern.matcher(s).matches();
    }

    public byte[] convertToByteArray(String s) {

        byte[] answer;

        if (isInteger(s)) {

            int i = Integer.parseInt(s);
            answer = new byte[4];
            answer[3] = (byte) (i & 0xff);
            answer[2] = (byte) ((i >> 8) & 0xff);
            answer[1] = (byte) ((i >> 16) & 0xff);
            answer[0] = (byte) ((i >> 24) & 0xff);

        } else if (isLong(s)) {

            long l = Long.parseLong(s);
            answer = ByteBuffer.allocate(8).putLong(l).array();

        } else if (isIpAddress(s)) {

            String[] octets = s.split("\\.");
            answer = new byte[4];
            for (int i = 0; i < octets.length; i++) {
                answer[i] = (byte) (Integer.parseInt(octets[i]) & 0xff);
            }

        } else if (isMacAddress(s)) {

            String[] octets = s.split(":");
            answer = new byte[6];
            for (int i = 0; i < octets.length; i++) {
                answer[i] = (byte) (Integer.parseInt(octets[i], 16) & 0xff);
            }

        } else {

            answer = s.getBytes();

        }

        return answer;

    }

}
