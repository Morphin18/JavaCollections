package com.javarush.task.task31.task3105;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/* 
Добавление файла в архив
*/
public class Solution {


    public static void main(String[] args) throws IOException {
        // read and save zip file to zipBuffer;
        Map<String, ByteArrayOutputStream> map = new HashMap<>();

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(args[1]))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;
                while ((count = zipIn.read(buffer)) > -1) {
                    byteArrayOut.write(buffer, 0, count);
                }
                map.put(entry.getName(), byteArrayOut);
                zipIn.closeEntry();
            }


        } catch (IOException ignored) {

        }


        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(args[1]))) {

            String fileName = Paths.get(args[0]).getFileName().toString();
            for (Map.Entry<String, ByteArrayOutputStream> pair : map.entrySet()) {
                zipOut.putNextEntry(new ZipEntry(pair.getKey()));
                zipOut.write(pair.getValue().toByteArray());
                zipOut.closeEntry();
            }
            ZipEntry zipEntry = new ZipEntry("new\\" + fileName);
            zipOut.putNextEntry(zipEntry);
            Files.copy(Paths.get(args[0]),zipOut);
            zipOut.closeEntry();
        } catch (IOException ignored) {

        }

    }




}
