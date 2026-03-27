package me.exeos.jjvm.assembler.insn.special;

import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class Label extends AbstractInstruction {

    public Label() {
        super(Byte.MIN_VALUE, 0);
    }

    @Override
    public byte[] getBytecode() {
        return new byte[0];
    }
}
