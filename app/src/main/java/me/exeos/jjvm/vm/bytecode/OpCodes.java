package me.exeos.jjvm.vm.bytecode;

public class OpCodes {

    public static final byte NOP = 0x0;
    public static final byte BIPUSH = 0x1;
    public static final byte PUSH = 0x2; // next 4 bytes = value to push
    public static final byte POP = 0x3;
    public static final byte ACONST_NULL = 0x4;
    public static final byte PRINT_SP = 0x5;
}
