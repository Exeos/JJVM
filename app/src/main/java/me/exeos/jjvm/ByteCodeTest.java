package me.exeos.jjvm;

public class ByteCodeTest {

    // i = 1; L = 16;  target = 18
    public static void main(String[] args) {
        int i = 2;
        int end = i;
        String x = "([I)V";
        System.out.println(x.substring(i-1, end + 1));
    }
}
