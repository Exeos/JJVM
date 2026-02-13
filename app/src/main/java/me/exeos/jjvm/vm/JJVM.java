package me.exeos.jjvm.vm;

import me.exeos.jjvm.utils.ConversionUntil;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.stack.StackEntry;
import me.exeos.jjvm.vm.stack.StackTypes;
import me.exeos.jjvm.vm.stack.TypedStack;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JJVM {

    /**
     * Execute Method. Frames out of this methods scope can't use its stack.
     */
    public static void exec(byte[] bytecode, int maxStackSize, int maxStackEntries) {
        TypedStack newStack = new TypedStack(maxStackSize, maxStackEntries);

        ByteArrayInputStream bytecodeStream = new ByteArrayInputStream(bytecode);
        while (bytecodeStream.available() > 0) {
            byte byteToInterpret = (byte) bytecodeStream.read();

            switch (byteToInterpret) {
                case OpCodes.NOP -> {}
                case OpCodes.BIPUSH -> {
                    if (bytecodeStream.available() < 1) {
                        throw new RuntimeException("Expected 1 operand. Read: " + bytecodeStream.available());
                    }

                    newStack.push((byte) bytecodeStream.read(), StackTypes.VALUE);
                }
                case OpCodes.PUSH -> {
                    if (bytecodeStream.available() < 4) {
                        throw new RuntimeException("Expected 4 operands. Read: " + bytecodeStream.available());
                    }

                    try {
                        newStack.push(bytecodeStream.readNBytes(4), StackTypes.VALUE);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read operands from bytecode stream");
                    }
                }
                case OpCodes.POP -> newStack.pop();
                case OpCodes.ACONST_NULL -> newStack.push((byte) 0, StackTypes.NULL);
                case OpCodes.PRINT_SP -> {
                    StackEntry<byte[]> frame = newStack.popWide();

                    if (frame.type() != StackTypes.VALUE || frame.data().length != 4) {
                        throw new RuntimeException("Invalid stack frame");
                    }

                    System.out.println("value: " + ConversionUntil.bytesToInt(frame.data()));
                }
                default -> throw new RuntimeException("Invalid OPCODE: " + byteToInterpret);
            }
        }
    }
}
