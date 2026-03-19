package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class Instruction extends AbstractInstruction {

    public Instruction(byte opcode) {
        super(opcode);
    }

    @Override
    public byte[] getBytecode() {
        return new byte[] { opcode };
    }
}
