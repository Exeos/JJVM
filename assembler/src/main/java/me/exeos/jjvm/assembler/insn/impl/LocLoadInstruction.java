package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class LocLoadInstruction extends AbstractInstruction {

    private final byte index;

    public LocLoadInstruction(byte index) {
        super(OpCodes.LOC_LOAD, 2);
        this.index = index;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, index);
    }
}
