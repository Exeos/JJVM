package me.exeos.jjvm.helpers;

import java.util.Objects;

public class ByteHelper {

    public static boolean byteToBoolean(byte value) {
        return value != 0;
    }

    public static short bytesToInt16(byte[] bytes) {
        return (short)
                (((bytes[0] & 0xFF) << 8) |
                ((bytes[1] & 0xFF)));
    }

    public static int bytesToInt32(byte[] bytes) {
        return ((bytes[0]  & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) <<  8) |
                ((bytes[3] & 0xFF));
    }

    public static int bytesToInt32Flexible(byte[] bytes) {
        switch (bytes.length) {
            case 1 -> {
                return bytes[0];
            }
            case 2 -> {
                return bytesToInt16(bytes);
            }
            case 4 -> {
                return bytesToInt32(bytes);
            }
            default -> throw new IllegalArgumentException("Bytes provided fit no Integer size");
        }
    }

    public static long bytesToInt64(byte[] bytes) {
        return ((bytes[0]  & 0xFFL) << 56) |
                ((bytes[1] & 0xFFL) << 48) |
                ((bytes[2] & 0xFFL) << 40) |
                ((bytes[3] & 0xFFL) << 32) |
                ((bytes[4] & 0xFFL) << 24) |
                ((bytes[5] & 0xFFL) << 16) |
                ((bytes[6] & 0xFFL) <<  8) |
                ((bytes[7] & 0xFFL));
    }

    public static float bytesToFloat(byte[] bytes) {
        int bits = ((bytes[0] & 0xFF) << 24)
                | ((bytes[1] & 0xFF) << 16)
                | ((bytes[2] & 0xFF) << 8)
                |  (bytes[3] & 0xFF);
        return Float.intBitsToFloat(bits);
    }

    public static double bytesToDouble(byte[] bytes) {
        long bits = ((long) (bytes[0] & 0xFF) << 56)
                | ((long) (bytes[1] & 0xFF) << 48)
                | ((long) (bytes[2] & 0xFF) << 40)
                | ((long) (bytes[3] & 0xFF) << 32)
                | ((long) (bytes[4] & 0xFF) << 24)
                | ((long) (bytes[5] & 0xFF) << 16)
                | ((long) (bytes[6] & 0xFF) << 8)
                |  (long) (bytes[7] & 0xFF);
        return Double.longBitsToDouble(bits);
    }

    public static byte boolToByte(boolean value) {
        return (byte) (value ? 1 : 0);
    }

    public static byte[] int16ToBytes(short value) {
        return new byte[] {
                (byte) (value >>> 8),
                (byte) (value)
        };
    }

    public static byte[] int32ToBytes(int value) {
        return new byte[] {
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) (value)
        };
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

    public static byte[] floatToBytes(float value) {
        int bits = Float.floatToIntBits(value);
        return new byte[] {
                (byte) (bits >> 24),
                (byte) (bits >> 16),
                (byte) (bits >> 8),
                (byte) (bits)
        };
    }

    public static byte[] doubleToBytes(double value) {
        long bits = Double.doubleToLongBits(value);
        return new byte[] {
                (byte) (bits >> 56),
                (byte) (bits >> 48),
                (byte) (bits >> 40),
                (byte) (bits >> 32),
                (byte) (bits >> 24),
                (byte) (bits >> 16),
                (byte) (bits >> 8),
                (byte) (bits)
        };
    }

    public static byte[] concat(Object... parts) {
        Objects.requireNonNull(parts, "parts");

        int size = 0;
        for (Object p : parts) {
            if (p instanceof byte[] a) {
                size += a.length;
            } else if (p instanceof Byte) {
                size += 1;
            } else {
                throw new IllegalArgumentException(
                        "Expected byte[] or Byte, got: " + (p == null ? "null" : p.getClass().getName()));
            }
        }

        byte[] out = new byte[size];
        int pos = 0;
        for (Object p : parts) {
            if (p instanceof byte[] a) {
                System.arraycopy(a, 0, out, pos, a.length);
                pos += a.length;
            } else { // Byte
                out[pos++] = (Byte) p;
            }
        }

        return out;
    }
}
