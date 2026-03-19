package me.exeos.jjvm.assembler.insn;

public abstract class AbstractInstruction {

    public final byte opcode;

    public AbstractInstruction(byte opcode) {
        this.opcode = opcode;
    }

    public abstract byte[] getBytecode();
}
