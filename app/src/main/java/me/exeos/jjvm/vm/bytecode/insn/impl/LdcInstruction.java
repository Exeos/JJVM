package me.exeos.jjvm.vm.bytecode.insn.impl;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.AbstractInstruction;

public class LdcInstruction extends AbstractInstruction {

    private final byte type;
    private final short cpIndex;

    public LdcInstruction(byte type, short cpIndex) {
        super(OpCodes.LDC);
        this.type = type;
        this.cpIndex = cpIndex;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, type, ByteHelper.int16ToBytes(cpIndex));
    }
}
