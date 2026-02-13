package me.exeos.jjvm.vm.bytecode.insn.impl;

import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.AbstractInstruction;

public class NewArrayInstruction extends AbstractInstruction {

    private final byte type;

    public NewArrayInstruction(byte type) {
        super(OpCodes.NEW_ARRAY);
        this.type = type;
    }

    @Override
    public byte[] getBytecode() {
        return new byte[] {
                opcode,
                type
        };
    }
}
