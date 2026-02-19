package me.exeos.jjvm.vm;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.memory.ConstantPool;
import me.exeos.jjvm.vm.memory.Heap;
import me.exeos.jjvm.vm.memory.TypedValue;
import me.exeos.jjvm.vm.stack.TypedStack;

public class VM {

    /**
     * Execute Method. Frames out of this methods scope can't use its stack.
     */
    public static Object exec(byte[] bytecode, int maxStackSize, int maxStackEntries, TypedValue... constants) {
        ConstantPool cp = new ConstantPool(constants);
        Heap heap = new Heap();
        TypedStack stack = new TypedStack(maxStackSize, maxStackEntries);

        int pc = 0;
        while (pc < bytecode.length) {
            int byteToInterpret = bytecode[pc++] & 0xFF;

            switch (byteToInterpret) {
                case OpCodes.NOP -> {}
                case OpCodes.POP -> stack.pop();
                case OpCodes.PUSH_BOOL -> {
                    ensureAvailable(bytecode, pc, 1);
                    stack.push(bytecode[pc++], Types.BOOL);
                }
                case OpCodes.PUSH_I8 -> {
                    ensureAvailable(bytecode, pc, 1);
                    stack.push(bytecode[pc++], Types.INT_8);
                }
                case OpCodes.PUSH_I16 -> {
                    ensureAvailable(bytecode, pc, 2);

                    stack.push(extractOperands(bytecode, pc, 2), Types.INT_16);
                    pc += 2;
                }
                case OpCodes.PUSH_I32 -> {
                    ensureAvailable(bytecode, pc, 2);

                    short valueCpIndex = ByteHelper.bytesToInt16(extractOperands(bytecode, pc, 2));
                    int value = cp.getConstant(Types.INT_32, valueCpIndex);

                    stack.push(ByteHelper.int32ToBytes(value), Types.INT_32);
                    pc += 2;
                }
                case OpCodes.PUSH_I64 -> {
                    ensureAvailable(bytecode, pc, 2);

                    short valueCpIndex = ByteHelper.bytesToInt16(extractOperands(bytecode, pc, 2));
                    long value = cp.getConstant(Types.INT_64, valueCpIndex);

                    stack.push(ByteHelper.int64ToBytes(value), Types.INT_64);
                    pc += 2;
                }
                case OpCodes.PUSH_NULL -> stack.push((byte) 0, Types.S_NULL_REF);
                case OpCodes.NEW_ARRAY -> {
                    ensureAvailable(bytecode, pc, 1);
                    ensureOperandType(bytecode, pc, type -> type >= Types.BOOL && type <= Types.OBJECT);

                    byte arrType = bytecode[pc++];

                    // ensure stack type for array size
                    stack.ensureType(type -> type >= Types.INT_8 && type <= Types.INT_32);

                    long heapRef = switch (arrType) {
                        case Types.BOOL -> heap.createRef(arrType, new boolean[ByteHelper.bytesToInt32Flexible(stack.pop())]);
                        case Types.INT_8 -> heap.createRef(arrType, new byte[ByteHelper.bytesToInt32Flexible(stack.pop())]);
                        case Types.INT_16 -> heap.createRef(arrType, new short[ByteHelper.bytesToInt32Flexible(stack.pop())]);
                        case Types.INT_32 -> heap.createRef(arrType, new int[ByteHelper.bytesToInt32Flexible(stack.pop())]);
                        case Types.INT_64 -> heap.createRef(arrType, new long[ByteHelper.bytesToInt32Flexible(stack.pop())]);
                        case Types.OBJECT -> heap.createRef(arrType, new Object[ByteHelper.bytesToInt32Flexible(stack.pop())]);
                        default -> throw new IllegalStateException("Invalid Type: " + arrType);
                    };

                    stack.push(ByteHelper.int64ToBytes(heapRef), Types.S_ARRAY_REF);
                }
                case OpCodes.ARR_LOAD -> {

                }
                case OpCodes.ARR_STORE -> {
                    // get arr type from operand
                    ensureAvailable(bytecode, pc, 1);
                    ensureOperandType(bytecode, pc, type -> type >= Types.BOOL && type <= Types.OBJECT);
                    byte arrType = bytecode[pc++];

                    // get value to store from stack
                    byte[] value = stack.pop();

                    // get index to store at from stack
                    stack.ensureType(type -> type >= Types.INT_8 && type <= Types.INT_32);
                    int index = ByteHelper.bytesToInt32Flexible(stack.pop());

                    // get array ref to store to from stack
                    stack.ensureType(type -> type == Types.S_ARRAY_REF);
                    long heapRef = stack.popI64();

                    // store value
                    switch (arrType) {
                        case Types.BOOL -> {
                            boolean[] arr = heap.getRefValue(arrType, heapRef);
                            arr[index] = value[0] != 0;
                        }
                        case Types.INT_8 -> {
                            byte[] arr = heap.getRefValue(arrType, heapRef);
                            arr[index] = value[0];
                        }
                        case Types.INT_16 -> {
                            short[] arr = heap.getRefValue(arrType, heapRef);
                            arr[index] = ByteHelper.bytesToInt16(value);
                        }
                        case Types.INT_32 -> {
                            int[] arr = heap.getRefValue(arrType, heapRef);
                            arr[index] = ByteHelper.bytesToInt32(value);
                        }
                        case Types.INT_64 -> {
                            long[] arr = heap.getRefValue(arrType, heapRef);
                            arr[index] = ByteHelper.bytesToInt64(value);
                        }
                        case Types.OBJECT -> {
                            Object[] arr = heap.getRefValue(arrType, heapRef);
                            arr[index] = heap.getRefValue(Types.OBJECT, ByteHelper.bytesToInt64(value));
                        }
                        default -> {
                            throw new IllegalStateException("Invalid Type: " + arrType);
                        }
                    }
                }
                // DEBUG INSN
                case OpCodes.PRINT_SP -> {
                    stack.ensureType(type -> type == Types.INT_32);
                    System.out.println("value: " + stack.popI32());
                }
                default -> throw new IllegalStateException("Invalid OPCODE: " + byteToInterpret);
            }
        }

        return null;
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

    private static void ensureOperandType(byte[] bytecode, int pc, TypeCheckFunction checkFunction) {
        if (!checkFunction.check(bytecode[pc])) {
            throw new RuntimeException("Invalid type operand.");
        }
    }
}
