package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class NewArrayInstruction extends AbstractInstruction {

    private final byte arrType;

    public NewArrayInstruction(byte arrType) {
        super(OpCodes.NEW_ARRAY);
        this.arrType = arrType;
    }

    @Override
    public byte[] getBytecode() {
        return new byte[] {
                opcode,
                arrType
        };
    }
}
