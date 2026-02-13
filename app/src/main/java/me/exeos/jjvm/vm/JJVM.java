package me.exeos.jjvm.vm;

import me.exeos.jjvm.utils.ConversionUntil;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.heap.Heap;
import me.exeos.jjvm.vm.stack.StackEntry;
import me.exeos.jjvm.vm.stack.StackTypes;
import me.exeos.jjvm.vm.stack.TypedStack;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class JJVM {

    /**
     * Execute Method. Frames out of this methods scope can't use its stack.
     */
    public static void exec(byte[] bytecode, int maxStackSize, int maxStackEntries) {
        Heap heap = new Heap();
        TypedStack stack = new TypedStack(maxStackSize, maxStackEntries);

        ByteArrayInputStream bytecodeStream = new ByteArrayInputStream(bytecode);
        while (bytecodeStream.available() > 0) {
            byte byteToInterpret = (byte) bytecodeStream.read();

            switch (byteToInterpret) {
                case OpCodes.NOP -> {}
                case OpCodes.POP -> stack.pop();
                case OpCodes.PUSH_I8 -> {
                    if (bytecodeStream.available() < 1) {
                        throw new RuntimeException("Expected 1 operand. Read: " + bytecodeStream.available());
                    }

                    stack.push((byte) bytecodeStream.read(), StackTypes.INT_8);
                }
                case OpCodes.PUSH_I32 -> {
                    // todo use constant pool for this
                    if (bytecodeStream.available() < 4) {
                        throw new RuntimeException("Expected 4 operands. Read: " + bytecodeStream.available());
                    }

                    try {
                        stack.push(bytecodeStream.readNBytes(4), StackTypes.INT_32);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read operands from bytecode stream");
                    }
                }
                case OpCodes.ACONST_NULL -> stack.push((byte) 0, StackTypes.NULL);
                case OpCodes.NEWARRAY -> {
                    if (bytecodeStream.available() < 1) {
                        throw new RuntimeException("Expected 1 operand. Read: " + bytecodeStream.available());
                    }

                    StackEntry<byte[]> countEntry = stack.popWide();
                    if (countEntry.type() < StackTypes.INT_8 || countEntry.type() > StackTypes.INT_32) {
                        throw new RuntimeException("Invalid type for operation: " + countEntry.type());
                    }

                    byte operand = (byte) bytecodeStream.read();
                    if (operand < Types.T_BOOLEAN || operand > Types.T_LONG) {
                        throw new RuntimeException("Invalid type for operation: " + operand);
                    }

                    int arrSize = ConversionUntil.bytesToInt32(countEntry.data());
                    long heapRef = heap.putRef(operand, new int[arrSize]);

                    stack.push(ConversionUntil.int64ToBytes(heapRef), StackTypes.ARRAY_REF);
                }
                case OpCodes.PRINT_SP -> {
                    StackEntry<byte[]> frame = stack.popWide();

                    if (frame.type() != StackTypes.INT_32 || frame.data().length != 4) {
                        throw new RuntimeException("Invalid stack frame");
                    }

                    System.out.println("value: " + ConversionUntil.bytesToInt32(frame.data()));
                }
                default -> throw new RuntimeException("Invalid OPCODE: " + byteToInterpret);
            }
        }
    }
}
