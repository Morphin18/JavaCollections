package com.javarush.task.task32.task3202;

import java.io.*;
import java.util.Arrays;

/* 
Читаем из потока
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        StringWriter writer = getAllDataFromInputStream(new FileInputStream("testFile.log"));
        System.out.println(writer.toString());
    }

    public static StringWriter getAllDataFromInputStream(InputStream is) throws IOException {
        StringWriter writer = new StringWriter();
        if (is != null) {
           try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
               while (reader.ready()) {
                   writer.write(reader.read());
               }
           } catch (IOException ignored) {

           }
       }
        return writer;
    }
}