package com.javarush.task.task32.task3211;

import sun.security.provider.MD5;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;

/* 
Целостность информации
*/

public class Solution {
    public static void main(String... args) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(new String("test string"));
        oos.flush();
        System.out.println(compareMD5(bos, "5a47d12a2e3f9fecf2d9ba1fd98152eb")); //true

    }

    public static boolean compareMD5(ByteArrayOutputStream byteArrayOutputStream, String md5) throws Exception {
       if (byteArrayOutputStream == null || md5 == null) return false;
       MessageDigest digest = MessageDigest.getInstance("MD5");
       digest.update(byteArrayOutputStream.toByteArray());
       byte[] digestByte = digest.digest();
       StringBuilder builder = new StringBuilder();
        for (int i = 0; i < digestByte.length; i++) {
            builder.append(String.format("%02x",digestByte[i]));
        }
        if (md5.equals(builder.toString())) return true;


        return false;
    }
}
