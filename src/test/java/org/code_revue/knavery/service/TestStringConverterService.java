package org.code_revue.knavery.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author Mike Fanning
 */
public class TestStringConverterService {

    private StringConverterService stringConverterService = new StringConverterService();

    @Test
    public void matchIntegers() {
        
        String[] validInts = new String[]{
                "-1",
                "1",
                "0",
                "100",
                "-100",
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(Integer.MIN_VALUE)
        };

        for (String valid: validInts) {
            assertTrue("Integer \"" + valid + "\" should be valid", stringConverterService.isInteger(valid));
        }

        String[] invalidInts = new String[]{
                "",
                "wkjlj",
                "0x001",
                "--398298",
                "100.",
                "5.4",
                ".4",
                "+3988",
                StringConverterService.MAX_INT.add(BigInteger.ONE).toString(),
                StringConverterService.MIN_INT.subtract(BigInteger.ONE).toString()
        };

        for (String invalid: invalidInts) {
            assertFalse("Integer \"\" should not be valid", stringConverterService.isInteger(invalid));
        }

    }
    
    @Test
    public void matchLongs() {

        String[] validLongs = new String[]{
                "-1",
                "1",
                "0",
                "100",
                "-100",
                Integer.toString(Integer.MAX_VALUE),
                StringConverterService.MAX_INT.add(BigInteger.ONE).toString(),
                Integer.toString(Integer.MIN_VALUE),
                StringConverterService.MIN_INT.subtract(BigInteger.ONE).toString(),
                Long.toString(Long.MAX_VALUE),
                Long.toString(Long.MIN_VALUE)
        };

        for (String valid: validLongs) {
            assertTrue("Long \"" + valid + "\" should be valid", stringConverterService.isLong(valid));
        }

        String[] invalidLongs = new String[]{
                "",
                "wkjlj",
                "0x001",
                "--398298",
                "100.",
                "5.4",
                ".4",
                "+3988",
                StringConverterService.MAX_LONG.add(BigInteger.ONE).toString(),
                StringConverterService.MIN_LONG.subtract(BigInteger.ONE).toString()
        };

        for (String invalid: invalidLongs) {
            assertFalse("Long \"" + invalid + "\" should NOT be valid", stringConverterService.isLong(invalid));
        }

    }

    @Test
    public void matchIpAddress() {

        String[] validIpAddresses = new String[] {
                "0.0.0.0",
                "1.1.1.1",
                "10.0.0.1",
                "192.168.1.7",
                "192.168.1.100",
                "255.255.255.255"
        };

        for (String valid: validIpAddresses) {
            assertTrue("IP Address \"" + valid + "\" should be valid", stringConverterService.isIpAddress(valid));
        }

        String[] invalidIpAddresses = new String[] {
                "a.b.c.d",
                "sdlkfjlkj",
                "1111.1.1.1",
                "256.0.0.1",
                "1.256.1.1",
                "1.1.256.1",
                "1.1.1.256",
                "256.256.256.256",
                "1.1.1.1.1",
                ".1.1.1.1",
                "1.1.1.1.",
                ""
        };

        for (String invalid: invalidIpAddresses) {
            assertFalse("IP Address \"" + invalid + "\" should not be valid",
                    stringConverterService.isIpAddress(invalid));
        }
    }

    @Test
    public void matchMacAddress() {

        String[] validMacAddresses = new String[] {
                "00:00:00:00:00:00",
                "ff:ff:ff:ff:ff:ff",
                "0:0:0:0:0:0",
                "f:f:f:f:f:f"
        };

        for (String valid: validMacAddresses) {
            assertTrue("MAC Address \"" + valid + "\" should be valid", stringConverterService.isMacAddress(valid));
            String validUpper = valid.toUpperCase();
            assertTrue("MAC Address \"" + validUpper + "\" should be valid",
                    stringConverterService.isMacAddress(validUpper));
        }

        String[] invalidMacAddresses = new String[] {
                "gg:gg:gg:gg:gg:gg",
                "00.00.00.00.00.00",
                "ff.ff.ff.ff.ff.ff",
                "sdlfkjslkj",
                "0:0:0:0:0",
                "f:f:f:f:f",
                "000:00:00:00:00:00",
                "1ff:ff:ff:ff:ff:ff",
                "00:000:00:00:00:00",
                "00:00:000:00:00:00",
                "00:00:00:000:00:00",
                "00:00:00:00:000:00",
                "00:00:00:00:00:000"
        };

        for (String invalid: invalidMacAddresses) {
            assertFalse("MAC Address \"" + invalid + "\" should NOT be valid",
                    stringConverterService.isMacAddress(invalid));
            String invalidUpper = invalid.toUpperCase();
            assertFalse("MAC Address \"" + invalidUpper + "\" should NOT be valid",
                    stringConverterService.isMacAddress(invalidUpper));
        }

    }

    @Test
    public void convertData() {

        Tuple2<String, byte[]>[] mappings = new Tuple2[] {
                new Tuple2("192.168.1.7", new byte[] { (byte) 192, (byte) 168, 1, 7 }),
                new Tuple2("255.255.255.255", new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 }),
                new Tuple2("0.0.0.0", new byte[] { 0, 0, 0, 0 }),
                new Tuple2("1", new byte[] { 0, 0, 0, 1 }),
                new Tuple2("0", new byte[] { 0, 0, 0, 0 }),
                new Tuple2("255", new byte[] { 0, 0, 0, (byte) 255 }),
                new Tuple2("-1", new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 }),
                new Tuple2("-2147483648", new byte[] { (byte) 128, 0, 0, 0 }),
                new Tuple2("2147483647", new byte[] { 127, (byte) 255, (byte) 255, (byte) 255 }),
                new Tuple2("2147483648", new byte[] { 0, 0, 0, 0, (byte) 128, 0, 0, 0}),
                new Tuple2("", new byte[] {}),
                new Tuple2("abcdefg", new byte[] { 97, 98, 99, 100, 101, 102, 103 }),
                new Tuple2("2e:11:8a:49:0a:33", new byte[] { 0x2e, 0x11, (byte) 0x8a, 0x49, 0x0a, 0x33 })
        };

        for (Tuple2<String, byte[]> mapping: mappings) {
            // Use T2 first because it is the expected value
            assertArrayEquals("Error converting \"" + mapping.getT1() + "\"",
                    mapping.getT2(), stringConverterService.convertToByteArray(mapping.getT1()));
        }

    }

    private static class Tuple2<T1, T2> {

        private final T1 t1;
        private final T2 t2;

        public Tuple2(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        public T1 getT1() {
            return t1;
        }

        public T2 getT2() {
            return t2;
        }

    }

}
