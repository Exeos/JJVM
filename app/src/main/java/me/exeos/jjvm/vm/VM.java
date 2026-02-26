package me.exeos.jjvm.vm;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.helpers.DescriptorHelper;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.memory.ConstantPool;
import me.exeos.jjvm.vm.memory.Heap;
import me.exeos.jjvm.vm.memory.TypedValue;
import me.exeos.jjvm.vm.stack.TypedStack;

import java.io.ObjectStreamException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
                case OpCodes.GET_STATIC -> {
                    ensureAvailable(bytecode, pc, 6);

                    String owner = cp.getConstant(Types.OBJECT, ByteHelper.bytesToInt16(ByteHelper.concat(bytecode[pc++], bytecode[pc++])));
                    String member = cp.getConstant(Types.OBJECT, ByteHelper.bytesToInt16(ByteHelper.concat(bytecode[pc++], bytecode[pc++])));
                    String descriptor = cp.getConstant(Types.OBJECT, ByteHelper.bytesToInt16(ByteHelper.concat(bytecode[pc++], bytecode[pc++])));

                    Class<?>[] descriptorTypes = DescriptorHelper.parseDescriptor(descriptor);
                    if (descriptorTypes.length > 1) {
                        throw new RuntimeException("Field can't contain multiple Types in descriptor");
                    }

                    try {
                        Class<?> clazz = Class.forName(owner.replace("/", "."));
                        Field field = null;
                        for (Field clazzField : clazz.getFields()) {
                            if (clazzField.getName().equals(member) && clazzField.getType() == descriptorTypes[0]) {
                                field = clazzField;
                                break;
                            }
                        }

                        if (field == null) {
                            throw new RuntimeException("Field not found. Owner: " + owner + " Field: " + member + " Descriptor: " + descriptor);
                        }

                        if (field.getType() == boolean.class) {
                            stack.push(ByteHelper.boolToByte(field.getBoolean(null)), Types.BOOL);
                        } else if (field.getType() == byte.class) {
                            stack.push(field.getByte(null), Types.INT_8);
                        } else if (field.getType() == short.class) {
                            stack.push(ByteHelper.int16ToBytes(field.getShort(null)), Types.INT_16);
                        } else if (field.getType() == int.class) {
                            stack.push(ByteHelper.int32ToBytes(field.getInt(null)), Types.INT_32);
                        } else if (field.getType() == long.class) {
                            stack.push(ByteHelper.int64ToBytes(field.getLong(null)), Types.INT_64);
                        } else if (field.getType() == float.class) {
                            stack.push(ByteHelper.floatToBytes(field.getFloat(null)), Types.FLOAT);
                        } else if (field.getType() == double.class) {
                            stack.push(ByteHelper.doubleToBytes(field.getDouble(null)), Types.DOUBLE);
                        } else {
                            long heapRef = heap.createRef(Types.OBJECT, field.get(null));
                            stack.push(ByteHelper.int64ToBytes(heapRef), Types.HEAP_REF);
                        }
                    } catch (ClassNotFoundException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                case OpCodes.INVOKE_STATIC -> {
                    ensureAvailable(bytecode, pc, 6);

                    String owner = cp.getConstant(Types.OBJECT, ByteHelper.bytesToInt16(ByteHelper.concat(bytecode[pc++], bytecode[pc++])));
                    String member = cp.getConstant(Types.OBJECT, ByteHelper.bytesToInt16(ByteHelper.concat(bytecode[pc++], bytecode[pc++])));
                    String descriptor = cp.getConstant(Types.OBJECT, ByteHelper.bytesToInt16(ByteHelper.concat(bytecode[pc++], bytecode[pc++])));

                    DescriptorHelper.MethodDescriptor methodDescriptor = DescriptorHelper.parseMethodDescriptor(descriptor);

                    try {
                        Class<?> clazz = Class.forName(owner.replace('/', '.'));
                        Method method = clazz.getMethod(member, methodDescriptor.parameterDescriptor());
                        try {
                            Object[] params = new Object[methodDescriptor.parameterDescriptor().length];
                            for (int i = params.length - 1; i >= 0; i--) {
                                params[i] = stack.popJVMType(cp, heap);
                            }

                            method.invoke(null, params);
                        } catch (Exception e) {
                            System.out.println("Invoked method threw Exception. This currently does not affect the control flow!");
                            e.printStackTrace();
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        throw new RuntimeException("Can't find Class or Method to invoke");
                    }
                }
                case OpCodes.INVOKE_VIRTUAL -> {
                    ensureAvailable(bytecode, pc, 6);

                    String owner = cp.getConstant(Types.OBJECT, ByteHelper.bytesToInt16(ByteHelper.concat(bytecode[pc++], bytecode[pc++])));
                    String member = cp.getConstant(Types.OBJECT, ByteHelper.bytesToInt16(ByteHelper.concat(bytecode[pc++], bytecode[pc++])));
                    String descriptor = cp.getConstant(Types.OBJECT, ByteHelper.bytesToInt16(ByteHelper.concat(bytecode[pc++], bytecode[pc++])));

                    DescriptorHelper.MethodDescriptor methodDescriptor = DescriptorHelper.parseMethodDescriptor(descriptor);

                    try {
                        Class<?> clazz = Class.forName(owner.replace('/', '.'));
                        Method method = clazz.getMethod(member, methodDescriptor.parameterDescriptor());
                        try {
                            Object[] params = new Object[methodDescriptor.parameterDescriptor().length];
                            for (int i = params.length - 1; i >= 0; i--) {
                                params[i] = stack.popJVMType(cp, heap);
                            }

                            Object object = switch (stack.type()) {
                                case Types.HEAP_REF -> heap.getRefValue(Types.OBJECT, stack.popI64());
                                case Types.CP_REF -> cp.getConstant(Types.OBJECT, stack.popI16());
                                default -> throw new IllegalStateException("Invalid stack type for invoke_virtual. Type: " + stack.type());
                            };

                            method.invoke(object, params);
                        } catch (Exception e) {
                            System.out.println("Invoked method threw Exception. This currently does not affect the control flow!");
                            e.printStackTrace();
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        throw new RuntimeException("Can't find Class or Method to invoke");
                    }
                }
                case OpCodes.LDC -> {
                    ensureAvailable(bytecode, pc, 3);

                    short index = ByteHelper.bytesToInt16(ByteHelper.concat(
                            bytecode[pc++],
                            bytecode[pc++]
                    ));
                    byte type = cp.getConstantType(index);

                    switch (type) {
                        case Types.BOOL -> stack.push(ByteHelper.boolToByte(cp.getConstant(type, index)), type);
                        case Types.INT_8 -> stack.push(cp.getConstant(type, index), type);
                        case Types.INT_16 -> stack.push(ByteHelper.int16ToBytes(cp.getConstant(type, index)), type);
                        case Types.INT_32 -> stack.push(ByteHelper.int32ToBytes(cp.getConstant(type, index)), type);
                        case Types.INT_64 -> stack.push(ByteHelper.int64ToBytes(cp.getConstant(type, index)), type);
                        case Types.OBJECT -> stack.push(ByteHelper.int16ToBytes(index), Types.CP_REF);
                        default -> throw new IllegalStateException("Invalid Type: " + type);
                    }
                }
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
                    throw new RuntimeException("ARR_LOAD is not yet implemented.");
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
                        default -> throw new IllegalStateException("Invalid Type: " + arrType);
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
