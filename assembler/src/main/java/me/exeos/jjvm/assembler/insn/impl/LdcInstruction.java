package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class LdcInstruction extends AbstractInstruction {

    public final Object value;

    public LdcInstruction(Object value) {
        super(OpCodes.LDC, 3);
        this.value = value;
    }

    @Override
    public byte[] getBytecode() {
        throw new IllegalStateException("This instruction needs to be Assembled.");
    }
}
