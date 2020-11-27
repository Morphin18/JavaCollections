package com.javarush.task.task40.task4006;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;

/* 
Отправка GET-запроса через сокет
*/

public class Solution {
    public static void main(String[] args) throws Exception {
        getSite(new URL("https://javarush.ru/api/1.0/rest/me"));
    }

    public static void getSite(URL url) {

        try (Socket socket = new Socket(url.getHost(), 80);
             PrintStream writer = new PrintStream(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println("GET " + url.getPath());
            writer.flush();

            String responseLine = "";

            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}