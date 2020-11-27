package com.javarush.task.task36.task3605;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/* 
Использование TreeSet
*/

public class Solution {
    public static final int MAX_VALUE = 5;

    public static void main(String[] args) throws IOException {
        TreeSet<Character> set = new TreeSet<Character>();
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            while (reader.ready()) {
                char symbol = (char) reader.read();
                if (Character.isLetter(symbol)) {
                    set.add(Character.toLowerCase(symbol));
                }
            }
        }

            set.stream().limit(5).forEach(System.out::print);


    }
}
