package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class PushI64Instruction extends AbstractInstruction {

    public final long value;

    public PushI64Instruction(long value) {
        super(OpCodes.PUSH_I64, 3);
        this.value = value;
    }

    @Override
    public byte[] getBytecode() {
        throw new IllegalStateException("This instruction needs to be Assembled.");
    }
}
