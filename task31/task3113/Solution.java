package com.javarush.task.task31.task3113;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* 
Что внутри папки?
*/
public class Solution {
    private static List<Path> dirList = new ArrayList<>();
    private static List<Path> fileList = new ArrayList<>();
    private static int size;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Path path = Paths.get(reader.readLine());
        if (!Files.isDirectory(path)) {
            System.out.println(path.toAbsolutePath() + " - не папка.");
        }
        Files.walkFileTree(path, new MyFileVisitor());

        System.out.println("Всего папок - " + (dirList.size()-1));
        System.out.println("Всего файлов - " + fileList.size());
        System.out.println("Общий размер - " + size);
    }

    public static class MyFileVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            dirList.add(dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            fileList.add(file);
            size += Files.size(file);
            return FileVisitResult.CONTINUE;
        }
    }
}
