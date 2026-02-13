package me.exeos.jjvm.vm.bytecode;

public class OpCodes {

    public static final byte NOP = 0x0;
    public static final byte POP = 0x1;

    public static final byte PUSH_I1 = 0x2;
    public static final byte PUSH_I8 = 0x3;
    public static final byte PUSH_I16 = 0x4;
    public static final byte PUSH_I32 = 0x5;
    public static final byte PUSH_I64 = 0x6;

    public static final byte NEWARRAY = 0x7;
    public static final byte ACONST_NULL = 0x8;

    public static final byte PRINT_SP = 67;
}
