package me.exeos.jjvm;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.Types;
import me.exeos.jjvm.vm.VM;
import me.exeos.jjvm.vm.bytecode.insn.impl.*;
import me.exeos.jjvm.vm.memory.TypedValue;

public class Main {

    static void main(String[] args) {
        try {
            VM.exec(
                    ByteHelper.concat(
                            new GetStaticInstruction((short) 0, (short) 1, (short) 2).getBytecode(),
                            new LdcInstruction((short) 3).getBytecode(),
                            new InvokeVirtualInstruction((short) 4, (short) 5, (short) 6).getBytecode()
                    ),
                    20, 20,
                    new TypedValue<>(Types.OBJECT, "java/lang/System"),
                    new TypedValue<>(Types.OBJECT, "out"),
                    new TypedValue<>(Types.OBJECT, "Ljava/io/PrintStream;"),
                    new TypedValue<>(Types.OBJECT, "Hello World"),
                    new TypedValue<>(Types.OBJECT, "java/io/PrintStream"),
                    new TypedValue<>(Types.OBJECT, "println"),
                    new TypedValue<>(Types.OBJECT, "(Ljava/lang/String;)V")
            );
        } catch (RuntimeException e) {
            System.out.println("VM exited with error: " + e);
            e.printStackTrace();
        }
    }
}
