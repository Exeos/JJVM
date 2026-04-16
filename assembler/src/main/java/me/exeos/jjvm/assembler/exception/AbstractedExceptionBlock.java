package me.exeos.jjvm.assembler.exception;

import me.exeos.jjvm.assembler.insn.special.Label;

public record AbstractedExceptionBlock(Label start, Label end, Label handler, String type) {
}
