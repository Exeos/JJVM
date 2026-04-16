package me.exeos.jjvm.vm.memory;

import me.exeos.jjvm.shared.memory.TypedValue;
import me.exeos.jjvm.shared.type.Types;

import java.util.HashMap;

public class Heap {

    private final HashMap<Long, TypedValue> heap = new HashMap<>();
    long heapIndex = 0;

    public <T> long createRef(byte type, T value) {
        if (type == Types.HEAP_REF || type == Types.CP_REF) {
            throw new RuntimeException("Illegal heap type: " + type);
        }
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

        return ref.value;
    }

    public byte getRefType(long reference) {
        return heap.get(reference).type;
    }
}
