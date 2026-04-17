package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.assembler.insn.AbstractInstruction;
import me.exeos.jjvm.shared.bytecode.OpCodes;

public class CastInstruction extends AbstractInstruction {

    public final String castTarget;

    public CastInstruction(String castTarget) {
        super(OpCodes.CAST, 3);
        this.castTarget = castTarget;
    }

    @Override
    public byte[] getBytecode() {
        throw new IllegalStateException("This instruction needs to be Assembled.");
    }
}
