package me.exeos.jjvm.assembler;

import me.exeos.jjvm.assembler.cp.CpBuilder;
import me.exeos.jjvm.assembler.exception.AbstractedExceptionBlock;
import me.exeos.jjvm.assembler.insn.AbstractInstruction;
import me.exeos.jjvm.assembler.insn.impl.*;
import me.exeos.jjvm.assembler.insn.special.Label;
import me.exeos.jjvm.shared.exception.ExceptionBlock;
import me.exeos.jjvm.shared.helpers.ByteHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Assembler {

    public static AssemblerResult assemble(List<AbstractedExceptionBlock> abstractedExceptionBlocks, AbstractInstruction... instructions) {
        ArrayList<Byte> code = new ArrayList<>();
        CpBuilder cpBuilder = new CpBuilder();
        List<ExceptionBlock> exceptionBlocks = new ArrayList<>();


        HashMap<Label, Short> labelPositionMap = new HashMap<>();
        short position = 0;

        // map label positions
        for (AbstractInstruction instruction : instructions) {
            if (instruction instanceof Label label) {
                labelPositionMap.put(label, position);
            } else {
                int nextPos = position + instruction.codeLength;
                if (nextPos > Short.MAX_VALUE) {
                    throw new IllegalStateException("Code to large");
                }

                position = (short) nextPos;
            }
        }

        for (AbstractedExceptionBlock block : abstractedExceptionBlocks) {
            exceptionBlocks.add(new ExceptionBlock(labelPositionMap.get(block.start()), labelPositionMap.get(block.end()), labelPositionMap.get(block.handler()), block.type()));
        }

        // generate bytecode and create constant pool
        for (AbstractInstruction instruction : instructions) {
            switch (instruction) {
                case CastInstruction castInstruction -> {
                    code.add(castInstruction.opcode);

                    short cpIndex = cpBuilder.register(castInstruction.castTarget);

                    Collections.addAll(code, ArrayUtils.toObject(ByteHelper.int16ToBytes(cpIndex)));
                }
                case GetStaticInstruction getStaticInstruction -> {
                    code.add(getStaticInstruction.opcode);

                    short ownerCpIndex = cpBuilder.register(getStaticInstruction.owner);
                    short memberCpIndex = cpBuilder.register(getStaticInstruction.member);
                    short descriptorCpIndex = cpBuilder.register(getStaticInstruction.descriptor);

                    Collections.addAll(code, ArrayUtils.toObject(ByteHelper.concat(
                            ByteHelper.int16ToBytes(ownerCpIndex),
                            ByteHelper.int16ToBytes(memberCpIndex),
                            ByteHelper.int16ToBytes(descriptorCpIndex)
                    )));
                }
                case GotoInstruction gotoInstruction -> {
                    code.add(gotoInstruction.opcode);
                    Short addr = labelPositionMap.get(gotoInstruction.label);
                    if (addr == null) {
                        throw new IllegalStateException("Label not mapped. Label: " + gotoInstruction.label);
                    }

                    Collections.addAll(code, ArrayUtils.toObject(ByteHelper.int16ToBytes(addr)));
                }
                case InvokeStaticInstruction invokeStaticInstruction -> {
                    code.add(invokeStaticInstruction.opcode);

                    short ownerCpIndex = cpBuilder.register(invokeStaticInstruction.owner);
                    short memberCpIndex = cpBuilder.register(invokeStaticInstruction.member);
                    short descriptorCpIndex = cpBuilder.register(invokeStaticInstruction.descriptor);

                    Collections.addAll(code, ArrayUtils.toObject(ByteHelper.concat(
                            ByteHelper.int16ToBytes(ownerCpIndex),
                            ByteHelper.int16ToBytes(memberCpIndex),
                            ByteHelper.int16ToBytes(descriptorCpIndex)
                    )));
                }
                case InvokeVirtualInstruction invokeVirtualInstruction -> {
                    code.add(invokeVirtualInstruction.opcode);

                    short ownerCpIndex = cpBuilder.register(invokeVirtualInstruction.owner);
                    short memberCpIndex = cpBuilder.register(invokeVirtualInstruction.member);
                    short descriptorCpIndex = cpBuilder.register(invokeVirtualInstruction.descriptor);

                    Collections.addAll(code, ArrayUtils.toObject(ByteHelper.concat(
                            ByteHelper.int16ToBytes(ownerCpIndex),
                            ByteHelper.int16ToBytes(memberCpIndex),
                            ByteHelper.int16ToBytes(descriptorCpIndex)
                    )));
                }
                case LdcInstruction ldcInstruction -> {
                    code.add(ldcInstruction.opcode);

                    short cpIndex = cpBuilder.register(ldcInstruction.value);

                    Collections.addAll(code, ArrayUtils.toObject(ByteHelper.int16ToBytes(cpIndex)));
                }
                case PushI32Instruction pushI32Instruction -> {
                    code.add(pushI32Instruction.opcode);

                    short cpIndex = cpBuilder.register(pushI32Instruction.value);

                    Collections.addAll(code, ArrayUtils.toObject(ByteHelper.int16ToBytes(cpIndex)));
                }
                case PushI64Instruction pushI64Instruction -> {
                    code.add(pushI64Instruction.opcode);

                    short cpIndex = cpBuilder.register(pushI64Instruction.value);

                    Collections.addAll(code, ArrayUtils.toObject(ByteHelper.int16ToBytes(cpIndex)));
                }
                default -> Collections.addAll(code, ArrayUtils.toObject(instruction.getBytecode()));
            }
        }

        // convert from list to array
        byte[] out = new byte[code.size()];
        for (int i = 0; i < code.size(); i++) {
            out[i] = code.get(i);
        }

        return new AssemblerResult(out, cpBuilder.getConstants(), exceptionBlocks);
    }
}
