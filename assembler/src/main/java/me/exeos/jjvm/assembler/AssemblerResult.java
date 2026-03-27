package me.exeos.jjvm.assembler;

import me.exeos.jjvm.shared.memory.TypedValue;

import java.util.List;

public record AssemblerResult(byte[] bytecode, List<TypedValue> cp) {}
