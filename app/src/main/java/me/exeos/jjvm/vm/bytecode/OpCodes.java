package me.exeos.jjvm.vm.bytecode;

public class OpCodes {

    public static final byte NOP = 0x0;
    public static final byte POP = 0x1;

    public static final byte INVOKE_STATIC = 0x2;

    public static final byte LDC = 0x03;

    public static final byte PUSH_BOOL = 0x4;
    public static final byte PUSH_I8 = 0x5;
    public static final byte PUSH_I16 = 0x6;
    public static final byte PUSH_I32 = 0x7;
    public static final byte PUSH_I64 = 0x8;

    public static final byte NEW_ARRAY = 0x9;
    public static final byte ARR_LOAD = 0xA;
    public static final byte ARR_STORE = 0xB;

    public static final byte PUSH_NULL = 0xC;

    public static final byte PRINT_SP = 67;
}
