package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class PushI32Instruction extends AbstractInstruction {

    public final int value;

    public PushI32Instruction(int value) {
        super(OpCodes.PUSH_I32, 3);
        this.value = value;
    }

    @Override
    public byte[] getBytecode() {
        throw new IllegalStateException("This instruction needs to be Assembled.");
    }
}
