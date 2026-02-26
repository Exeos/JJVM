package me.exeos.jjvm.vm.bytecode.insn.impl;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.AbstractInstruction;

public class GetStaticInstruction extends AbstractInstruction {

    public final short ownerCpIndex;
    public final short memberCpIndex;
    public final short descriptorCpIndex;

    public GetStaticInstruction(short ownerCpIndex, short memberCpIndex, short descriptorCpIndex) {
        super(OpCodes.GET_STATIC);
        this.ownerCpIndex = ownerCpIndex;
        this.memberCpIndex = memberCpIndex;
        this.descriptorCpIndex = descriptorCpIndex;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, ByteHelper.int16ToBytes(ownerCpIndex), ByteHelper.int16ToBytes(memberCpIndex), ByteHelper.int16ToBytes(descriptorCpIndex));
    }
}
