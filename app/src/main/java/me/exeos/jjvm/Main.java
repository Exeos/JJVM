package me.exeos.jjvm;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.VM;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.impl.PushI32Instruction;

public class Main {

    public static void main(String[] args) {
        try {
            VM.exec(ByteHelper.concat(new PushI32Instruction(67).getBytecode(), OpCodes.POP), 20, 20);
        } catch (RuntimeException e) {
            System.out.println("VM exited with error: " + e);
        }
    }

}
