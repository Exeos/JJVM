package me.exeos.jjvm.vm.stack;

import me.exeos.jjvm.helpers.ByteHelper;
import me.exeos.jjvm.vm.TypeCheckFunction;
import me.exeos.jjvm.vm.Types;
import me.exeos.jjvm.vm.memory.ConstantPool;
import me.exeos.jjvm.vm.memory.Heap;

/**
 * Typed Stack
 * Popping always decreases stackEntries by 1 and stackData based on the length of the popped StackEntry
 * Pushing always increases stackEntries by 1 and stackData based on the length of the actual value stored
 * <p>
 * need to explain this better, so I won't forget what this shit does 🥀
 */
public class TypedStack {

    private final byte[] stackData;
    private final StackEntryMeta[] stackEntries;

    private int stackDataPointer;
    private int stackEntryPointer;

    public TypedStack(int maxStackSize, int maxStackEntries) {
        stackData = new byte[maxStackSize];
        stackEntries = new StackEntryMeta[maxStackEntries];

        stackDataPointer = 0;
        stackEntryPointer = 0;
    }

    public void push(byte value, byte type) {
        if (stackDataPointer >= stackData.length || stackEntryPointer >= stackEntries.length) {
            throw new RuntimeException("Stack Exhausted");
        }

        stackData[stackDataPointer] = value;
        stackEntries[stackEntryPointer] = new StackEntryMeta(type, stackDataPointer, 1);

        stackDataPointer++;
        stackEntryPointer++;
    }

    public void push(byte[] value, byte type) {
        if (stackDataPointer + value.length > stackData.length || stackEntryPointer >= stackEntries.length) {
            throw new RuntimeException("Stack Exhausted");
        }

        System.arraycopy(value, 0, stackData, stackDataPointer, value.length);
        stackEntries[stackEntryPointer] = new StackEntryMeta(type, stackDataPointer, value.length);

        stackDataPointer += value.length;
        stackEntryPointer++;
    }

    public byte[] pop() {
        if (stackDataPointer == 0 || stackEntryPointer == 0) {
            throw new RuntimeException("Can't pop from empty stack");
        }

        StackEntryMeta topEntry = stackEntries[stackEntryPointer - 1];

        byte[] entryData = new byte[topEntry.length()];
        System.arraycopy(stackData, topEntry.start(), entryData, 0, topEntry.length());

        stackDataPointer -= entryData.length;
        stackEntryPointer--;

        return entryData;
    }

    public short popI16() {
        return ByteHelper.bytesToInt16(pop());
    }

    public int popI32() {
        return ByteHelper.bytesToInt32(pop());
    }

    public long popI64() {
        return ByteHelper.bytesToInt64(pop());
    }

    public Object popJVMType(ConstantPool cp, Heap heap) {
        byte type = type();
        switch (type) {
            case Types.BOOL -> {
                return ByteHelper.byteToBoolean(pop()[0]);
            }
            case Types.INT_8 -> {
                return pop()[0];
            }
            case Types.INT_16 -> {
                return ByteHelper.bytesToInt16(pop());
            }
            case Types.INT_32 -> {
                return ByteHelper.bytesToInt32(pop());
            }
            case Types.INT_64 -> {
                return ByteHelper.bytesToInt64(pop());
            }
            case Types.CP_REF -> {
                short index = popI16();

                return cp.getConstant(index);
            }
            case Types.HEAP_REF, Types.S_ARRAY_REF -> {
                long heapRef = popI64();

                return heap.getRefValue(heapRef);
            }
            case Types.S_NULL_REF -> {
                return null;
            }
        }
        return null;
    }

    public byte type() {
        if (stackEntryPointer == 0) {
            throw new IllegalStateException("Can't peek on empty stack");
        }

        return stackEntries[stackEntryPointer - 1].type();
    }

    public void ensureType(TypeCheckFunction checkFunction) {
        if (!checkFunction.check(type())) {
            throw new IllegalStateException("Invalid stack type.");
        }
    }
}
