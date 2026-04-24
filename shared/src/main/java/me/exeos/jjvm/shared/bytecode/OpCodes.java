package me.exeos.jjvm.shared.bytecode;

public class OpCodes {

    public static final byte NOP = 0x0;
    public static final byte POP = 0x1;
    public static final byte DUP = 0x2;

    public static final byte GET_STATIC = 0x3;

    public static final byte LDC = 0x04;

    public static final byte INVOKE_STATIC = 0x5;
    public static final byte INVOKE_VIRTUAL = 0x6;

    public static final byte PUSH_BOOL = 0x7;
    public static final byte PUSH_I8 = 0x8;
    public static final byte PUSH_I16 = 0x9;
    public static final byte PUSH_I32 = 0xA;
    public static final byte PUSH_I64 = 0xB;

    public static final byte NEW_ARRAY = 0xC;
    public static final byte ARR_LOAD = 0xD;
    public static final byte ARR_STORE = 0xE;
    public static final byte ARR_LENGTH = 0x13;

    public static final byte PUSH_NULL = 0xF;

    public static final byte LOC_LOAD = 0x10;
    public static final byte LOC_STORE = 0x11;

    public static final byte GOTO = 0x14;

    public static final byte RETURN = 0x12;

    public static final byte THROW = 0x15;

    public static final byte CAST = 0x16;
    public static final byte D2F = 0x17;
    public static final byte D2I32 = 0x18;
    public static final byte D2I64 = 0x19;
    public static final byte F2D = 0x1A;
    public static final byte F2I32 = 0x1B;
    public static final byte F2I64 = 0x1C;
}
