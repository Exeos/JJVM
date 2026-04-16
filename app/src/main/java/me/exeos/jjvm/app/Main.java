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
        Label start = new Label();
        Label end = new Label();
        Label handler = new Label();
        Label handlerEnd = new Label();

        Label innerStart = new Label();
        Label innerEnd = new Label();
        Label innerHandler = new Label();
        Label innerHandlerEnd = new Label();

        AssemblerResult assemblerResult = Assembler.assemble(
                List.of(
                        new AbstractedExceptionBlock(innerStart, innerEnd, innerHandler, null),
                        new AbstractedExceptionBlock(start, end, handler, null)
                ),
                start,
                new GetStaticInstruction("java/lang/System", "out", "Ljava/io/PrintStream;"),
                new LdcInstruction("x"),
                new InvokeStaticInstruction("java/lang/Integer", "parseInt", "(Ljava/lang/String;)I"),
                new InvokeVirtualInstruction("java/io/PrintStream", "println", "(I)V"),
                end,
                new GotoInstruction(handlerEnd),
                handler,
                new GetStaticInstruction("java/lang/System", "out", "Ljava/io/PrintStream;"),
                new LdcInstruction("An Exception occurred"),
                new InvokeVirtualInstruction("java/io/PrintStream", "println", "(Ljava/lang/String;)V"),
                innerStart,
                new Instruction(OpCodes.THROW),
                innerEnd,
                new GotoInstruction(innerHandlerEnd),
                innerHandler,
                new InvokeVirtualInstruction("java/lang/Throwable", "printStackTrace", "()V"),
                innerHandlerEnd,
                handlerEnd
        );

        VM.exec(
                assemblerResult.bytecode(),
                20, 10, 0, void.class,
                assemblerResult.cp(),
                assemblerResult.exceptionBlocks()
        );
    }
}