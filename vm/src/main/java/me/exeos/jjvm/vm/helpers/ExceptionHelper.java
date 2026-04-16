package me.exeos.jjvm.vm.helpers;

import me.exeos.jjvm.shared.exception.ExceptionBlock;
import me.exeos.jjvm.shared.helpers.ByteHelper;
import me.exeos.jjvm.shared.type.Types;
import me.exeos.jjvm.vm.memory.Heap;
import me.exeos.jjvm.vm.stack.TypedStack;

import java.util.List;

public class ExceptionHelper {

    /**
     * Resolves the exception handler for {@code exception} at {@code causerBytecodeIndex}.
     * If a handler is found, clears {@code stack}, pushes the exception reference, and returns
     * the handler PC to jump to. If no handler matches, rethrows {@code exception}.
     *
     * @param exception           the thrown exception
     * @param causerBytecodeIndex bytecode index of the instruction that caused the throw
     * @param exceptionBlocks     exception table entries (in table order)
     * @param stack               operand stack for the current frame
     * @param heap                heap used to store the exception object reference
     * @return handler PC to jump to
     * @throws Throwable if no matching handler exists
     */
    public static int handle(Throwable exception, short causerBytecodeIndex, List<ExceptionBlock> exceptionBlocks, TypedStack stack, Heap heap) throws Throwable {
        ExceptionBlock exceptionBlock = null;

        for (ExceptionBlock blockToCheck : exceptionBlocks) {
            if (causerBytecodeIndex >= blockToCheck.start() && causerBytecodeIndex < blockToCheck.end()) {
                if (blockToCheck.type() == null) {
                    exceptionBlock = blockToCheck;
                    break;
                }

                try {
                    Class<?> catchClass = Class.forName(blockToCheck.type().replace("/", "."));
                    if (catchClass.isInstance(exception)) {
                        exceptionBlock = blockToCheck;
                        break;
                    }
                } catch (ClassNotFoundException classNotFoundException) {
                    throw new RuntimeException("Exception type's class not found. Type=" + blockToCheck.type(), classNotFoundException);
                }
            }
        }

        if (exceptionBlock == null) {
            throw exception;
        }

        stack.clear();
        stack.push(ByteHelper.int64ToBytes(heap.createRef(Types.OBJECT, exception)), Types.HEAP_REF);

        return exceptionBlock.handler();
    }
}
