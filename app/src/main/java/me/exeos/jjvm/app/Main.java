package me.exeos.jjvm.app;

import me.exeos.jjvm.assembler.Assembler;
import me.exeos.jjvm.assembler.insn.impl.*;
import me.exeos.jjvm.assembler.insn.special.Label;
import me.exeos.jjvm.shared.bytecode.OpCodes;
import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.vm.VM;
import me.exeos.jjvm.vm.memory.TypedValue;
import me.exeos.jjvm.vm.type.Types;

public class Main {

    static void main(String[] args) {
//        try {
//            VM.exec(
//                    ByteHelper.concat(
//                            new PushI8Instruction((byte) 1).getBytecode(),
//                            new NewArrayInstruction(Types.OBJECT).getBytecode(),
//                            OpCodes.DUP,
//                            new PushI8Instruction((byte) 0).getBytecode(),
//                            new GetStaticInstruction((short) 0, (short) 1, (short) 2).getBytecode(),
//                            new ArrStoreInstruction(Types.OBJECT).getBytecode(),
//                            new PushI8Instruction((byte) 0).getBytecode(),
//                            new ArrLoadInstruction(Types.OBJECT).getBytecode(),
//                            new LocStoreInstruction((byte) 0).getBytecode(),
//                            new LocLoadInstruction((byte) 0).getBytecode(),
//                            new LdcInstruction((short) 3).getBytecode(),
//                            new InvokeVirtualInstruction((short) 4, (short) 5, (short) 6).getBytecode()
//                    ),
//                    40, 20, 1,
//                    new TypedValue<>(Types.OBJECT, "java/lang/System"),
//                    new TypedValue<>(Types.OBJECT, "out"),
//                    new TypedValue<>(Types.OBJECT, "Ljava/io/PrintStream;"),
//                    new TypedValue<>(Types.OBJECT, "Hello World"),
//                    new TypedValue<>(Types.OBJECT, "java/io/PrintStream"),
//                    new TypedValue<>(Types.OBJECT, "println"),
//                    new TypedValue<>(Types.OBJECT, "(Ljava/lang/String;)V")
//            );
//        } catch (RuntimeException e) {
//            System.out.println("VM exited with error: " + e);
//            e.printStackTrace();
//        }
        try {
            System.out.println(superSecret());
        } catch (Exception e) {
            System.out.println("VM exited with error: " + e);
            e.printStackTrace();
        }
    }

    private static int superSecret() throws Exception {
        Label start = new Label();

        return VM.exec(
                Assembler.assemble(
                        new GotoInstruction(start),
                        new Instruction(OpCodes.NOP),
                        start,
                        new PushI32Instruction((byte) 0),
                        new Instruction(OpCodes.RETURN)
                )
                ,
                8, 1, 0, Integer.class,
                new TypedValue<>(Types.INT_32, 0)
        );
    }
}