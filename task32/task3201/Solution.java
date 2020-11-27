package com.javarush.task.task32.task3201;

import java.io.IOException;
import java.io.RandomAccessFile;

/* 
Запись в существующий файл
*/
public class Solution {
    public static void main(String... args) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(args[0], "rw");
        if (raf.length() > Integer.parseInt(args[1])) {
            raf.seek(Integer.parseInt(args[1]));
            raf.write(args[2].getBytes());
        } else {
            raf.seek(raf.length());
            raf.write(args[2].getBytes());
        }
        raf.close();
    }
}
