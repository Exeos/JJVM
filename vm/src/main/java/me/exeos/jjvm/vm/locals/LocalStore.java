package me.exeos.jjvm.vm.locals;

import java.util.Arrays;

public class LocalStore {

    private final byte[][] locals;
    private final byte[] types;

    public LocalStore(int maxLocals) {
        locals = new byte[maxLocals][];
        types = new byte[maxLocals];
    }

    public void store(int index, byte type, byte[] value) {
        ensureIndexBounds(index);

        locals[index] = Arrays.copyOf(value, value.length);
        types[index] = type;
    }

    public byte[] load(int index) {
        ensureIndexBounds(index);

        byte[] value = locals[index];
        return Arrays.copyOf(value, value.length);
    }

    public byte type(int index) {
        ensureIndexBounds(index);

        return types[index];
    }

    private void ensureIndexBounds(int index) {
        if (index < 0 || index >= locals.length) {
            throw new IndexOutOfBoundsException("Local index " + index + " out of bounds (0.." + (locals.length - 1) + ")");
        }
    }
}
