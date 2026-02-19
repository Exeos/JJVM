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
                            new PushI8Instruction((byte) 4).getBytecode(),
                            new NewArrayInstruction(Types.BOOL).getBytecode(),
                            new PushI8Instruction((byte) 0).getBytecode(),
                            new PushI8Instruction((byte) 1).getBytecode(),
                            new ArrStoreInstruction(Types.BOOL).getBytecode()
                    ),
                    20, 20
            );
        } catch (RuntimeException e) {
            System.out.println("VM exited with error: " + e);
        }
    }

}
