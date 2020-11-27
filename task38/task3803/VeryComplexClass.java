package com.javarush.task.task38.task3803;

/*
Runtime исключения (unchecked exception)
*/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VeryComplexClass {
    public static void main(String[] args) {

    }

    public void methodThrowsClassCastException() throws CloneNotSupportedException {
        List<Integer> list = new ArrayList<Integer>();
        ((LinkedList<Integer>) list).add(3);
    }

    public void methodThrowsNullPointerException() {
        int[] arr = null;
        int i = arr.length;

    }
}
