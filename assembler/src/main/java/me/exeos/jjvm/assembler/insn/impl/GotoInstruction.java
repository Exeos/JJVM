package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.assembler.insn.special.Label;
import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class GotoInstruction extends AbstractInstruction {

    public final Label label;

    public GotoInstruction(Label label) {
        super(OpCodes.GOTO);
        this.label = label;
    }

    @Override
    public byte[] getBytecode() {
        // assembler ignores this. bytecode is generated dynamically respecting labels
        return ByteHelper.concat(opcode, ByteHelper.int16ToBytes((short) 0));
    }
}
