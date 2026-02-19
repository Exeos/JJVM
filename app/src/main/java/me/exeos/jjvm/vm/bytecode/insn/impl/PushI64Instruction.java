package me.exeos.jjvm.vm.bytecode.insn.impl;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.AbstractInstruction;

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
