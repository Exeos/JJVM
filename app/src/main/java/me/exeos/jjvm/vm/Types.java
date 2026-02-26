package me.exeos.jjvm.vm;

public class Types {

    public static final byte BOOL = 0x1;
    public static final byte INT_8 = 0x3;
    public static final byte INT_16 = 0x4;
    public static final byte INT_32 = 0x5;
    public static final byte INT_64 = 0x6;
    public static final byte DOUBLE = 0x7;
    public static final byte FLOAT = 0x8;

    public static final byte OBJECT = 0x9;

    public static final byte HEAP_REF = 0xA;
    public static final byte CP_REF = 0xB;

    /* Stack only types */
    public static final byte S_NULL_REF = 0xC;
    public static final byte S_ARRAY_REF = 0xD; // TODO: GET RID OF THIS AND REPLACE WITH HEAP_REF
}
