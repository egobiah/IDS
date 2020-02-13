package server;

public interface Calculator_itf {
    public static int plus(int op_1, int op_2) {
        return op_1 + op_2;
    }

    public static int minus(int op_1, int op_2) {
        return op_1 - op_2;
    }

    public static int divide(int op_1, int op_2) {
        return op_1 / op_2;
    }

    public static int multiply(int op_1, int op_2) {
        return op_1 * op_2;
    }

}

