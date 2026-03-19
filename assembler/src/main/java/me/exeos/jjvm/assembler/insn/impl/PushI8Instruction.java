package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class PushI8Instruction extends AbstractInstruction {

    public final byte value;

    public PushI8Instruction(byte value) {
        super(OpCodes.PUSH_I8);
        this.value = value;
    }

    @Override
    public byte[] getBytecode() {
        return new byte[] {
                opcode,
                value
        };
    }
}
