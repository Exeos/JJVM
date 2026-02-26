package me.exeos.jjvm.helpers;

import java.util.ArrayList;
import java.util.List;

public class DescriptorHelper {

    public static MethodDescriptor parseMethodDescriptor(String descriptor) {
        List<Class<?>> parameterDescriptor = new ArrayList<>();
        Class<?> returnType = null;

        boolean parameterContext = false;

        int i = 0;
        while (i < descriptor.length()) {
            switch (descriptor.charAt(i++)) {
                case '(' -> {
                    if (!parameterContext) {
                        parameterContext = true;
                    } else {
                        throw new RuntimeException("Failed to parse method descriptor. Can't open ParameterDescriptor when in ParameterDescriptor context.");
                    }
                }
                case ')' -> {
                    if (parameterContext) {
                        parameterContext = false;
                    } else {
                        throw new RuntimeException("Failed to parse method descriptor. Can't close ParameterDescriptor when not in ParameterDescriptor context.");
                    }
                }
                case 'L' -> {
                    int end = descriptor.indexOf(';', i);

                    Class<?> clazz = null;

                    try {
                        clazz = Class.forName(descriptor.substring(i, end).replace('/', '.'));
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class provided in descriptor not found.");
                        System.exit(1);
                    }

                    if (parameterContext) {
                        parameterDescriptor.add(clazz);
                    } else {
                        returnType = clazz;
                    }

                    i = end + 1;
                }
                case '[' -> {
                    int end = i;
                    while (descriptor.charAt(end) == '[') {
                        end++;
                    }
                    if (descriptor.charAt(end) == 'L') {
                        end = descriptor.indexOf(';', end);
                    }

                    Class<?> arrClazz = null;
                    try {
                        arrClazz = Class.forName(descriptor.substring(i - 1, end + 1).replace('/', '.'));
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class provided in descriptor not found.");
                        System.exit(1);
                    }

                    if (parameterContext) {
                        parameterDescriptor.add(arrClazz);
                    } else {
                        returnType = arrClazz;
                    }

                    i = end + 1;
                }
                case 'B' -> {
                    if (parameterContext) {
                        parameterDescriptor.add(byte.class);
                    } else {
                        returnType = byte.class;
                    }
                }
                case 'C' -> {
                    if (parameterContext) {
                        parameterDescriptor.add(char.class);
                    } else {
                        returnType = char.class;
                    }
                }
                case 'D' -> {
                    if (parameterContext) {
                        parameterDescriptor.add(double.class);
                    } else {
                        returnType = double.class;
                    }
                }
                case 'F' -> {
                    if (parameterContext) {
                        parameterDescriptor.add(float.class);
                    } else {
                        returnType = float.class;
                    }
                }
                case 'I' -> {
                    if (parameterContext) {
                        parameterDescriptor.add(int.class);
                    } else {
                        returnType = int.class;
                    }
                }
                case 'J' -> {
                    if (parameterContext) {
                        parameterDescriptor.add(long.class);
                    } else {
                        returnType = long.class;
                    }
                }
                case 'S' -> {
                    if (parameterContext) {
                        parameterDescriptor.add(short.class);
                    } else {
                        returnType = short.class;
                    }
                }
                case 'Z' -> {
                    if (parameterContext) {
                        parameterDescriptor.add(boolean.class);
                    } else {
                        returnType = boolean.class;
                    }
                }

                case 'V' -> {
                    if (parameterContext) {
                        throw new IllegalStateException("Failed to parse method descriptor. Invalid char: V when in parameter context.");
                    }

                    returnType = void.class;
                }
                default -> throw new IllegalStateException("Failed to parse method descriptor. Invalid char: " + descriptor.charAt(i - 1));
            }
        }

        return new MethodDescriptor(parameterDescriptor.toArray(new Class<?>[0]), returnType);
    }

    public record MethodDescriptor(Class<?>[] params, Class<?> returnType) {}
}
