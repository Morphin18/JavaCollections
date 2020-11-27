package com.javarush.task.task40.task4011;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/* 
Свойства URL
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        decodeURLString("https://www.amrood.com/index.htm?language=en#j2se");
    }

    public static void decodeURLString(String s) throws MalformedURLException {
       try {
           URL url = new URL(s);
           System.out.println("URL is " + url.toString());
           System.out.println("Protocol is " + url.getProtocol());
           System.out.println("Authority is " + url.getAuthority());
           System.out.println("File is " + url.getFile());
           System.out.println("Host is " + url.getHost());
           System.out.println("Path is " + url.getPath());
           System.out.println("Port is " + url.getPort());
           System.out.println("Default port is " + url.getDefaultPort());
           System.out.println("Query is " + url.getQuery());
           System.out.println("Ref is" + url.getRef());
       }catch (MalformedURLException e) {
           System.out.println("Parameter " + s + " is not a valid URL.");
       }

    }
}

