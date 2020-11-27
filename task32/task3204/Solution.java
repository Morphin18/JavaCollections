package com.javarush.task.task32.task3204;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/* 
Генератор паролей
*/
public class Solution {
    public static void main(String[] args) {
        ByteArrayOutputStream password = getPassword();
        System.out.println(password.toString());
    }

    public static ByteArrayOutputStream getPassword() {
        ByteArrayOutputStream pass = new ByteArrayOutputStream();
        StringBuilder builder = new StringBuilder();
        while (!builder.toString().matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}")) {
            int randLower = (int) ((Math.random() * (122 - 97)) + 97);
            int randUpper = (int) ((Math.random() * (90 - 65)) + 65);
            int num = (int) ((Math.random() * (57 - 48)) + 48);
            builder.append((char) randLower);
            builder.append((char) randUpper);
            builder.append((char) num);
        }
        try {
            pass.write(builder.substring(0,8).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pass;
    }
}