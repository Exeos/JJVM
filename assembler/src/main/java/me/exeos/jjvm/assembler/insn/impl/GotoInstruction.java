package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.assembler.insn.special.Label;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class GotoInstruction extends AbstractInstruction {

    public final Label label;

    public GotoInstruction(Label label) {
        super(OpCodes.GOTO, 3);
        this.label = label;
    }

    @Override
    public byte[] getBytecode() {
        throw new IllegalStateException("This instruction needs to be Assembled.");
    }
}
