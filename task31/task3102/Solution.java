package com.javarush.task.task31.task3102;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/* 
Находим все файлы
*/
public class Solution {
    public static List<String> getFileTree(String root) throws IOException {
        List<String> list = new ArrayList<>();
        ArrayDeque<File> line = new ArrayDeque<>();
        File file = new File(root);
        line.add(file);

        while (!line.isEmpty()) {
            File f = line.remove();
            if (f.isDirectory()) {
                line.addAll(Arrays.asList(f.listFiles()));
            } else {
                list.add(String.valueOf(f.getAbsoluteFile()));
            }
        }

        return list;

    }

    public static void main(String[] args) throws IOException {
     List<String> list = new ArrayList<>();
     list = getFileTree("G:\\lesson\\");
    }
}
