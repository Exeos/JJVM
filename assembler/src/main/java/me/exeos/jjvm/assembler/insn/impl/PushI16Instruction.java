package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class PushI16Instruction extends AbstractInstruction {

    public final short value;

    public PushI16Instruction(short value) {
        super(OpCodes.PUSH_I16);
        this.value = value;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, ByteHelper.int16ToBytes(value));
    }
}
