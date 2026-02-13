package me.exeos.jjvm.utils;

public class ConversionUntil {

    public static int bytesToInt(byte[] bytes) {
        return bytesToInt(bytes, 0);
    }

    public static int bytesToInt(byte[] bytes, int offset) {
        return ((bytes[offset]     & 0xFF) << 24) |
                ((bytes[offset + 1] & 0xFF) << 16) |
                ((bytes[offset + 2] & 0xFF) <<  8) |
                ((bytes[offset + 3] & 0xFF));
    }
}
