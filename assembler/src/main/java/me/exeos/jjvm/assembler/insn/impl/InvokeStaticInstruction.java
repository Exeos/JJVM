package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class InvokeStaticInstruction extends AbstractInstruction {

    public final short ownerCpIndex;
    public final short memberCpIndex;
    public final short descriptorCpIndex;

    public InvokeStaticInstruction(short ownerCpIndex, short memberCpIndex, short descriptorCpIndex) {
        super(OpCodes.INVOKE_STATIC);
        this.ownerCpIndex = ownerCpIndex;
        this.memberCpIndex = memberCpIndex;
        this.descriptorCpIndex = descriptorCpIndex;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, ByteHelper.int16ToBytes(ownerCpIndex), ByteHelper.int16ToBytes(memberCpIndex), ByteHelper.int16ToBytes(descriptorCpIndex));
    }
}
