package com.javarush.task.task31.task3106;

import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipInputStream;


/* 
Разархивируем файл
*/
public class Solution {
    public static void main(String[] args) throws  IOException{
        Arrays.sort(args, 1, args.length);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        for (int i = 1; i < args.length; i++) {
            FileInputStream fileInputStream = new FileInputStream(args[i]);
            byte[] b = new byte[fileInputStream.available()];
            fileInputStream.read(b);
            byteArray.write(b);
            fileInputStream.close();
        }
        ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(byteArray.toByteArray()));
        FileOutputStream out = new FileOutputStream(new File(args[0]));
        if (in.getNextEntry() != null) {
            int len;
            byte[] buffer = new byte[2048];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
        }
        in.close();
    }
}
