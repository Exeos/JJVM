package me.exeos.jjvm.app;

import me.exeos.jjvm.assembler.Assembler;
import me.exeos.jjvm.assembler.AssemblerResult;
import me.exeos.jjvm.assembler.exception.AbstractedExceptionBlock;
import me.exeos.jjvm.assembler.insn.impl.*;
import me.exeos.jjvm.assembler.insn.special.Label;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.vm.VM;

import java.util.List;

public class Main {

    static void main(String[] args) {
        try {
            superSecret();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void superSecret() throws Throwable {
        AssemblerResult assemblerResult = Assembler.assemble(
                List.of(
                ),
                new GetStaticInstruction("java/lang/System", "out", "Ljava/io/PrintStream;"),
                new LdcInstruction(3.0d),
                new Instruction(OpCodes.D2F),
                new InvokeVirtualInstruction("java/io/PrintStream", "println", "(F)V")
        );

        VM.exec(
                assemblerResult.bytecode(),
                20, 10, 0, void.class,
                assemblerResult.cp(),
                assemblerResult.exceptionBlocks()
        );
    }
}