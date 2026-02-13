package me.exeos.jjvm.vm.bytecode.insn.impl;

import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.AbstractInstruction;

public class PushInstruction extends AbstractInstruction {

    public final int value;

    public PushInstruction(int value) {
        super(OpCodes.PUSH_I32);
        this.value = value;
    }

    @Override
    public byte[] getBytecode() {
        return new byte[] {
                opcode,
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) (value)
        };
    }
}
