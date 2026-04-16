package me.exeos.jjvm.shared.exception;

public record ExceptionBlock(short start, short end, short handler, String type) {
}
