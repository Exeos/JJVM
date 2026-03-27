package me.exeos.jjvm.vm.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.exeos.jjvm.shared.memory.TypedValue;

public class ConstantPool {

    private final ArrayList<TypedValue> constants;

    public ConstantPool(List<TypedValue> constants) {
        this.constants = new ArrayList<>(constants);
    }

    public <T> T getConstant(byte type, short index) {
        TypedValue ref = constants.get(index);

        if (ref.type != type) {
            throw new RuntimeException("CP Type mismatch");
        }

        return (T) ref.value;
    }

    public Object getConstant(short index) {
        TypedValue ref = constants.get(index);
        if (ref == null) {
            throw new NullPointerException("Constant doesn't exist");
        }

        return ref.value;
    }

    public byte getConstantType(short index) {
        return constants.get(index).type;
    }
}
