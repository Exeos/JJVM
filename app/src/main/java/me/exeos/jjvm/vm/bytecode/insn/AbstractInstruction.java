package me.exeos.jjvm.vm.bytecode.insn;

public abstract class AbstractInstruction {

    public final int opcode;

    public AbstractInstruction(int opcode) {
        this.opcode = opcode;
    }

    public abstract byte[] getBytecode();
}
