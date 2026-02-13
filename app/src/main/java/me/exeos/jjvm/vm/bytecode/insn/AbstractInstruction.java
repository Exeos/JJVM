package me.exeos.jjvm.vm.bytecode.insn;

public abstract class AbstractInstruction {

    public final byte opcode;

    public AbstractInstruction(byte opcode) {
        this.opcode = opcode;
    }

    public abstract byte[] getBytecode();
}
