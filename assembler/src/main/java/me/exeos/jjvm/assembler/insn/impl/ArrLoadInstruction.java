package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.assembler.insn.AbstractInstruction;
import me.exeos.jjvm.shared.bytecode.OpCodes;

public class ArrLoadInstruction extends AbstractInstruction {

    private final byte arrType;

    public ArrLoadInstruction(byte arrType) {
        super(OpCodes.ARR_LOAD);
        this.arrType = arrType;
    }

    @Override
    public byte[] getBytecode() {
        return new byte[] { opcode, arrType };
    }
}
