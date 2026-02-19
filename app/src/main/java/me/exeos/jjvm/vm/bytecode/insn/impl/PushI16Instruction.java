package me.exeos.jjvm.vm.bytecode.insn.impl;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.AbstractInstruction;

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
