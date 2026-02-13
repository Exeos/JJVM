package me.exeos.jjvm.vm.bytecode.insn.impl;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.AbstractInstruction;

public class PushI32Instruction extends AbstractInstruction {

    public final int value;

    public PushI32Instruction(int value) {
        super(OpCodes.PUSH_I32);
        this.value = value;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, ByteHelper.int32ToBytes(value));
    }
}
