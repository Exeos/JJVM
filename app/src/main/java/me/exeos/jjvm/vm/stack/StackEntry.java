package me.exeos.jjvm.vm.stack;

public record StackEntry<T>(T data, byte type) {}
