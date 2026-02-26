package me.exeos.jjvm.vm.memory;

import java.util.HashMap;
import java.util.function.Consumer;

public class Heap {
    
    private final HashMap<Long, TypedValue> heap = new HashMap<>();
    long heapIndex = 0;

    public <T> long createRef(byte type, T value) {
        heap.put(heapIndex++, new TypedValue<T>(type, value));

        return heapIndex - 1;
    }

    public <T> T getRefValue(byte type, long reference) {
        TypedValue ref = heap.get(reference);
        if (ref == null) {
            throw new NullPointerException("Heap reference doesn't exist");
        }

        if (ref.type != type) {
            throw new RuntimeException("Heap Type mismatch");
        }

        return (T) ref.value;
    }

    public Object getRefValue(long reference) {
        TypedValue ref = heap.get(reference);
        if (ref == null) {
            throw new NullPointerException("Heap reference doesn't exist");
        }

        return ref;
    }

    public <T> void mutateRef(byte type, long reference, Consumer<T> mutator) {
        T value = getRefValue(type, reference);
        mutator.accept(value);
    }
}
