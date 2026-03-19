package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class PushI64Instruction extends AbstractInstruction {

    public final short valueCpIndex;

    public PushI64Instruction(short valueCpIndex) {
        super(OpCodes.PUSH_I64);
        this.valueCpIndex = valueCpIndex;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, ByteHelper.int16ToBytes(valueCpIndex));
    }
}
