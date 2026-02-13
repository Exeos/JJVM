package me.exeos.jjvm.vm;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.heap.Heap;
import me.exeos.jjvm.vm.stack.TypedStack;

public class VM {

    /**
     * Execute Method. Frames out of this methods scope can't use its stack.
     */
    public static void exec(byte[] bytecode, int maxStackSize, int maxStackEntries) {
        Heap heap = new Heap();
        TypedStack stack = new TypedStack(maxStackSize, maxStackEntries);

        int pc = 0;
        while (pc < bytecode.length) {
            int byteToInterpret = bytecode[pc++] & 0xFF;

            switch (byteToInterpret) {
                case OpCodes.NOP -> {}
                case OpCodes.POP -> stack.pop();
                case OpCodes.PUSH_I8 -> {
                    ensureAvailable(bytecode, pc, 1);
                    stack.push(bytecode[pc++], Types.INT_8);
                }
                case OpCodes.PUSH_I32 -> {
                    ensureAvailable(bytecode, pc, 4);

                    stack.push(extractOperands(bytecode, pc, 4), Types.INT_32);
                    pc += 4;
                }
                case OpCodes.PUSH_NULL -> stack.push((byte) 0, Types.S_NULL_REF);
                case OpCodes.NEW_ARRAY -> {
                    ensureAvailable(bytecode, pc, 1);

                    byte arrType = bytecode[pc++];
                    if (arrType < Types.INT_8 || arrType > Types.INT_32) {
                        throw new RuntimeException("Invalid operand: " + arrType);
                    }

                    byte stackEntryType = stack.peekType();
                    if (stackEntryType < Types.INT_8 || stackEntryType > Types.INT_32) {
                        throw new RuntimeException("Invalid type for operation: " + stackEntryType);
                    }

                    long heapRef = heap.createRef(arrType, new int[ByteHelper.bytesToInt32Flexible(stack.pop())]);

                    stack.push(ByteHelper.int64ToBytes(heapRef), Types.S_ARRAY_REF);
                }
                // DEBUG INSN
                case OpCodes.PRINT_SP -> {
                    if (stack.peekType() != Types.INT_32) {
                        throw new RuntimeException("Invalid stack frame");
                    }

                    System.out.println("value: " + stack.popI32());
                }
                default -> throw new RuntimeException("Invalid OPCODE: " + byteToInterpret);
            }
        }
    }

    private static void ensureAvailable(byte[] bytecode, int pc, int len) {
        if (bytecode.length - pc < len) {
            throw new RuntimeException("Unexpected end of Bytecode. Required " + len + " bytes at PC: " + pc);
        }
    }

    private static byte[] extractOperands(byte[] bytecode, int pc, int operands) {
        byte[] operandArr = new byte[operands];
        System.arraycopy(bytecode, pc, operandArr, 0, operands);

        return operandArr;
    }
}
