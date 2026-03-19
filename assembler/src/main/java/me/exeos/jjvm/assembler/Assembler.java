package me.exeos.jjvm.assembler;

import me.exeos.jjvm.assembler.insn.AbstractInstruction;
import me.exeos.jjvm.assembler.insn.impl.GotoInstruction;
import me.exeos.jjvm.assembler.insn.special.Label;
import me.exeos.jjvm.shared.helpers.ByteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Assembler {


    public static byte[] assemble(AbstractInstruction... instructions) {
        ArrayList<Byte> code = new ArrayList<>();
        HashMap<Label, Short> labelPositionMap = new HashMap<>();
        short position = 0;

        for (AbstractInstruction instruction : instructions) {
            if (instruction instanceof Label label) {
                labelPositionMap.put(label, position);
            } else {
                position += (short) instruction.getBytecode().length;
            }
        }

        for (AbstractInstruction instruction : instructions) {
            if (instruction instanceof GotoInstruction gotoInstruction) {
                code.add(gotoInstruction.opcode);
                short addr = labelPositionMap.get(gotoInstruction.label);

                for (byte b : ByteHelper.int16ToBytes(addr)) {
                    code.add(b);
                }
                position++;
                continue;
            }

            for (byte b : instruction.getBytecode()) {
                code.add(b);
            }

            position++;
        }

        byte[] out = new byte[code.size()];
        for (int i = 0; i < code.size(); i++) {
            out[i] = code.get(i);
        }

        return out;
    }
}
