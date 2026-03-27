package me.exeos.jjvm.assembler.cp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.exeos.jjvm.shared.memory.TypedValue;
import me.exeos.jjvm.shared.type.Types;

public class CpBuilder {

    private final HashMap<CpKey, Short> cpReg = new HashMap<>();
    private short cpIndex = 0;

    public short register(Object value) {
        CpKey key = switch (value) {
            case String strVal -> new CpKey(Types.OBJECT, strVal);

            case Integer intVal -> new CpKey(Types.INT_32, intVal);
            case Long longVal -> new CpKey(Types.INT_64, longVal);

            case Float floatVal -> new CpKey(Types.FLOAT, floatVal);
            case Double doubleVal -> new CpKey(Types.DOUBLE, doubleVal);

            default -> throw new IllegalStateException("Unsupported CP type: " + value.getClass());
        };

        return cpReg.computeIfAbsent(key, _ -> cpIndex++);
    }

    public List<TypedValue> getConstants() {
        if (cpReg.isEmpty()) {
            return List.of();
        }

        ArrayList<TypedValue> constants = new ArrayList<>(cpIndex);
        for (int i = 0; i < cpIndex; i++) {
            constants.add(null);
        }

        for (Map.Entry<CpKey, Short> e : cpReg.entrySet()) {
            CpKey key = e.getKey();
            int idx = e.getValue();

            constants.set(idx, new TypedValue(key.type(), key.value()));
        }

        return constants;
    }
}
