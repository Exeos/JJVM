package me.exeos.jjvm.vm.heap;

public class HeapRef<T> {

    public final byte T_TYPE;
    public T value;

    public HeapRef(byte type, T value) {
        T_TYPE = type;
        this.value = value;
    }
}
