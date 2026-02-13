package me.exeos.jjvm.vm.bytecode.insn.impl;

import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.AbstractInstruction;

public class PushI8Instruction extends AbstractInstruction {

    public final byte value;

    public PushI8Instruction(byte value) {
        super(OpCodes.PUSH_I32);
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
