package com.pagin.azul.pattern;

public class LeftPAtttern {

    public static void main(String[] args) {
        int rows = 5;
        for(int i = 5; i >= 1; --i) {
            for(int j = 1; j <= i; ++j) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }
}
