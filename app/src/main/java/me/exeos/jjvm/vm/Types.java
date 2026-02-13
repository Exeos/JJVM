package me.exeos.jjvm.vm;

public class Types {

    public static final byte INT_1 = 0x1;
    public static final byte INT_4 = 0x2;
    public static final byte INT_8 = 0x3;
    public static final byte INT_32 = 0x4;
    public static final byte INT_64 = 0x5;

    /* Stack only types */
    public static final byte S_NULL_REF = 0x6;
    public static final byte S_ARRAY_REF = 0x7;
}
