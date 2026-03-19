package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class LocStoreInstruction extends AbstractInstruction {

    private final byte index;

    public LocStoreInstruction(byte index) {
        super(OpCodes.LOC_STORE);
        this.index = index;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, index);
    }
}
