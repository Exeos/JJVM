package me.exeos.jjvm.vm.heap;

import java.util.HashMap;

public class Heap {
    
    private final HashMap<Long, HeapRef> heap = new HashMap<>();
    long heapIndex = 0;

    public <T> long putRef(byte type, T value) {
        heap.put(heapIndex++, new HeapRef<T>(type, value));

        return heapIndex - 1;
    }

    public <T> T getRefValue(byte type, long reference) {
        HeapRef ref = heap.get(reference);
        if (ref == null) {
            throw new NullPointerException("Heap reference doesn't exist");
        }

        if (ref.T_TYPE != type) {
            throw new RuntimeException("Heap Type mismatch");
        }

        return (T) ref.value;
    }
}
