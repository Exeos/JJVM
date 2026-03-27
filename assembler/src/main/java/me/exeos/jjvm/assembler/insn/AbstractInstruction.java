package me.exeos.jjvm.assembler.insn;

public abstract class AbstractInstruction {

    public final byte opcode;
    public final int codeLength;

    public AbstractInstruction(byte opcode, int codeLength) {
        this.opcode = opcode;
        this.codeLength = codeLength;
    }

    public abstract byte[] getBytecode();
}
