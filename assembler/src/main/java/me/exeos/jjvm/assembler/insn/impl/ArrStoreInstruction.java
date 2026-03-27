package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class ArrStoreInstruction extends AbstractInstruction {

    private final byte arrType;

    public ArrStoreInstruction(byte arrType) {
        super(OpCodes.ARR_STORE, 2);
        this.arrType = arrType;
    }

    @Override
    public byte[] getBytecode() {
        return new byte[] { opcode, arrType };
    }
}
