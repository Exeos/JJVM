package me.exeos.jjvm.utils;

public class ConversionUntil {

    public static int bytesToInt32(byte[] bytes) {
        return bytesToInt32(bytes, 0);
    }

    public static int bytesToInt32(byte[] bytes, int offset) {
        return ((bytes[offset]     & 0xFF) << 24) |
                ((bytes[offset + 1] & 0xFF) << 16) |
                ((bytes[offset + 2] & 0xFF) <<  8) |
                ((bytes[offset + 3] & 0xFF));
    }

    public static long bytesToInt64(byte[] bytes) {
        return bytesToInt64(bytes, 0);
    }

    public static long bytesToInt64(byte[] bytes, int offset) {
        return ((bytes[offset]     & 0xFFL) << 56) |
                ((bytes[offset + 1] & 0xFFL) << 48) |
                ((bytes[offset + 2] & 0xFFL) << 40) |
                ((bytes[offset + 3] & 0xFFL) << 32) |
                ((bytes[offset + 4] & 0xFFL) << 24) |
                ((bytes[offset + 5] & 0xFFL) << 16) |
                ((bytes[offset + 6] & 0xFFL) <<  8) |
                ((bytes[offset + 7] & 0xFFL));
    }

    public static byte[] int64ToBytes(long value) {
        return new byte[] {
                (byte) (value >>> 56),
                (byte) (value >>> 48),
                (byte) (value >>> 40),
                (byte) (value >>> 32),
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) (value)
        };
    }
}
