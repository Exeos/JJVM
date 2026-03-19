package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class LdcInstruction extends AbstractInstruction {

    private final short cpIndex;

    public LdcInstruction(short cpIndex) {
        super(OpCodes.LDC);
        this.cpIndex = cpIndex;
    }

    @Override
    public byte[] getBytecode() {
        return ByteHelper.concat(opcode, ByteHelper.int16ToBytes(cpIndex));
    }
}
