package me.exeos.jjvm;

import me.exeos.jjvm.vm.JJVM;
import me.exeos.jjvm.vm.bytecode.OpCodes;
import me.exeos.jjvm.vm.bytecode.insn.impl.PushInstruction;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        try {
            JJVM.exec(concat(new PushInstruction(67).getBytecode(), OpCodes.POP), 20, 20);
        } catch (RuntimeException e) {
            System.out.println("VM exited with error: " + e);
        }
    }

    public static byte[] concat(Object... parts) {
        Objects.requireNonNull(parts, "parts");

        int size = 0;
        for (Object p : parts) {
            if (p instanceof byte[] a) {
                size += a.length;
            } else if (p instanceof Byte) {
                size += 1;
            } else {
                throw new IllegalArgumentException(
                        "Expected byte[] or Byte, got: " + (p == null ? "null" : p.getClass().getName()));
            }
        }

        byte[] out = new byte[size];
        int pos = 0;
        for (Object p : parts) {
            if (p instanceof byte[] a) {
                System.arraycopy(a, 0, out, pos, a.length);
                pos += a.length;
            } else { // Byte
                out[pos++] = (Byte) p;
            }
        }

        return out;
    }
}
