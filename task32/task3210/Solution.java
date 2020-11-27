package com.javarush.task.task32.task3210;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/* 
Используем RandomAccessFile
*/

public class Solution {
    public static void main(String... args) throws Exception {
        try {
            RandomAccessFile raf = new RandomAccessFile(args[0], "rw");
            raf.seek(Integer.parseInt(args[1]));
            byte [] bytes = new byte[args[2].length()];
            raf.read(bytes, 0,args[2].length());
            String str = new String(bytes);
            String bool = (args[2].equals(str)) ? "true" : "false";
            raf.seek(raf.length());
            raf.write(bool.getBytes());
            raf.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
