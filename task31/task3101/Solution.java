package com.javarush.task.task31.task3101;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/* 
Проход по дереву файлов
*/
public class Solution {
    public static void main(String[] args)  {
        TreeMap<String, String> map = new TreeMap<>();
        List<String> buffer = new ArrayList<>();
        File folder = new File(args[0]);
        File reName = new File(args[1]);
        File newName = new File(reName.getParent() + "/allFilesContent.txt");
        if (FileUtils.isExist(reName)) {
            FileUtils.renameFile(reName, newName);
        }

        for (File file : folder.listFiles()) {
            map.putAll(findFile(file));
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newName)))) {
            for (Map.Entry<String, String> pair : map.entrySet()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pair.getValue())));
                while (reader.ready()) {
                    writer.write(reader.read());
                }
                reader.close();
                writer.write("\n");
            }
            writer.flush();
        }catch (IOException ignored) {}

    }

    public static TreeMap<String, String> findFile(File file) {
        TreeMap<String, String> map = new TreeMap<>();
        if (file.isFile() && file.length() <= 50) {
            map.put(file.getName(), file.getPath());
        } else if (file.isDirectory()) {
            for (File fileDirectory : file.listFiles()) {
                map.putAll(findFile(fileDirectory));
            }
        }
        return map;
    }
}
