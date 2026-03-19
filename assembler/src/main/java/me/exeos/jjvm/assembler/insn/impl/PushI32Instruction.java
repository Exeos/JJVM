package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class PushI32Instruction extends AbstractInstruction {

    public final short valueCpIndex;

    public PushI32Instruction(short valueCpIndex) {
        super(OpCodes.PUSH_I32);
        this.valueCpIndex = valueCpIndex;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, ByteHelper.int16ToBytes(valueCpIndex));
    }
}
