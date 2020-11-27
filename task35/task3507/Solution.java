package com.javarush.task.task35.task3507;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.HashSet;
import java.util.Set;

/* 
ClassLoader - что это такое?
*/

public class Solution {
    public static void main(String[] args) {
        Set<? extends Animal> allAnimals = getAllAnimals(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + Solution.class.getPackage().getName().replaceAll("[.]", "/") + "/data");
        System.out.println(allAnimals);
    }

    public static Set<? extends Animal> getAllAnimals(String pathToAnimals) {
        Set<Animal> animals = new HashSet<>();
        java.lang.ClassLoader loader = new java.lang.ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                try {
                    Path file = Paths.get(name);
                    byte[] b = Files.readAllBytes(file);
                    return defineClass(null, b, 0, b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return super.findClass(name);
            }

        };
        try {
            String decodePath = URLDecoder.decode(pathToAnimals, "UTF-8");
            File[] classes = new File(decodePath).listFiles();
            for (File file : classes) {
                Class classFromFile = loader.loadClass(file.getAbsolutePath());
                if (Animal.class.isAssignableFrom(classFromFile)) {
                    Constructor[] constructors = classFromFile.getConstructors();
                    for (Constructor constructor : constructors) {
                        if (constructor.getParameterCount() == 0) {
                            animals.add((Animal) constructor.newInstance());
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedEncodingException | InvocationTargetException e) {
            System.out.println(e.getCause().toString());
        }
        return animals;
    }
}
