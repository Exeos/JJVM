package me.exeos.jjvm.app;

import me.exeos.jjvm.assembler.Assembler;
import me.exeos.jjvm.assembler.AssemblerResult;
import me.exeos.jjvm.assembler.insn.impl.*;
import me.exeos.jjvm.assembler.insn.special.Label;
import me.exeos.jjvm.vm.VM;

public class Main {

    static void main(String[] args) {
        try {
            superSecret();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void superSecret() throws Exception {
        Label start = new Label();

        AssemblerResult assemblerResult = Assembler.assemble(
                start,
                new GetStaticInstruction("java/lang/System", "out", "Ljava/io/PrintStream;"),
                new LdcInstruction("Hello World!"),
                new InvokeVirtualInstruction("java/io/PrintStream", "println", "(Ljava/lang/String;)V"),
                new GotoInstruction(start)
        );

        VM.exec(
                assemblerResult.bytecode(),
                20, 10, 0, void.class,
                assemblerResult.cp()
        );
    }
}