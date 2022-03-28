package com.pagin.azul.pattern;

public class PyramidPattern {
    public static void main(String args[]) {

        int rows = 5, k = 0;
        for (int i = 1; i <= rows; ++i, k = 0) {
            for (int space = 1; space <= rows - 1; ++space) {
                System.out.print("  ");
            }
            while (k != 2 * i - 1) {
                System.out.print("* ");
                ++k;
            }
            System.out.println();
        }
    }

}
