package me.exeos.jjvm.vm.memory;

public class TypedValue<T> {

    public final byte type;
    public T value;

    public TypedValue(byte type, T value) {
        this.type = type;
        this.value = value;
    }
}
