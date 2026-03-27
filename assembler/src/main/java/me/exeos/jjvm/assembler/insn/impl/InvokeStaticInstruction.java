package me.exeos.jjvm.assembler.insn.impl;

import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;

public class InvokeStaticInstruction extends AbstractInstruction {

    public final String owner;
    public final String member;
    public final String descriptor;

    public InvokeStaticInstruction(String owner, String member, String descriptor) {
        super(OpCodes.INVOKE_STATIC, 7);
        this.owner = owner;
        this.member = member;
        this.descriptor = descriptor;
    }

    @Override
    public byte[] getBytecode() {
        throw new IllegalStateException("This instruction needs to be Assembled.");
    }
}
