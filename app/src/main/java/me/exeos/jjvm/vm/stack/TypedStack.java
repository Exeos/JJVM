package me.exeos.jjvm.vm.stack;

/**
 * Typed Stack
 * Popping always decreases stackEntries by 1 and stackData based on the length of the popped StackEntry
 * Pushing always increases stackEntries by 1 and stackData based on the length of the actual value stored
 *
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
        if (stackDataPointer + value.length >= stackData.length || stackEntryPointer >= stackEntries.length) {
            throw new RuntimeException("Stack Exhausted");
        }

        System.arraycopy(value, 0, stackData, stackDataPointer, value.length);
        stackEntries[stackEntryPointer] = new StackEntryMeta(type, stackDataPointer, value.length);

        stackDataPointer += value.length;
        stackEntryPointer++;
    }

    public StackEntry<Byte> pop() {
        if (stackDataPointer == 0 || stackEntryPointer == 0) {
            throw new RuntimeException("Can't pop from empty stack");
        }

        StackEntryMeta topEntry = stackEntries[stackEntryPointer - 1];

        stackDataPointer -= topEntry.length;
        stackEntryPointer--;

        return new StackEntry<>(stackData[topEntry.start], topEntry.type);
    }

    public StackEntry<byte[]> popWide() {
        if (stackDataPointer == 0 || stackEntryPointer == 0) {
            throw new RuntimeException("Can't pop from empty stack");
        }

        StackEntryMeta topEntry = stackEntries[stackEntryPointer - 1];
        if (stackData.length < topEntry.length) {
            throw new RuntimeException("Stack to small for operation");
        }

        byte[] entryData = new byte[topEntry.length];
        System.arraycopy(stackData, topEntry.start, entryData, 0, topEntry.length);

        return new StackEntry<>(entryData, topEntry.type);
    }

    private static class StackEntryMeta {
        public final byte type;
        public int start;
        public int length;

        public StackEntryMeta(byte type, int start, int length) {
            this.type = type;
            this.start = start;
            this.length = length;
        }
    }
}
