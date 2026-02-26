package me.exeos.jjvm;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.Types;
import me.exeos.jjvm.vm.VM;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.impl.*;
import me.exeos.jjvm.vm.memory.TypedValue;

public class Main {

    public static void main(String[] args) {
        try {
            VM.exec(
                    ByteHelper.concat(
                            new LdcInstruction(Types.OBJECT, (short) 0).getBytecode(),
                            new InvokeStaticInstruction((short) 1, (short) 2, (short) 3).getBytecode()
                    ),
                    20, 20,
                    new TypedValue<String>(Types.OBJECT, "Hello World"),
                    new TypedValue<String>(Types.OBJECT, "java/lang/IO"),
                    new TypedValue<String>(Types.OBJECT, "println"),
                    new TypedValue<String>(Types.OBJECT, "(Ljava/lang/Object;)V")
            );
        } catch (RuntimeException e) {
            System.out.println("VM exited with error: " + e);
            e.printStackTrace();
        }
    }

}
