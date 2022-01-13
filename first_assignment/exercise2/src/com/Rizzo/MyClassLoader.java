package com.Rizzo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MyClassLoader  extends ClassLoader{
    String parentpath;

    public MyClassLoader(String parentpath){
        this.parentpath=parentpath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String[] splitted = name.split("\\.");
        String classname = splitted[splitted.length -1];
        File f = new File(parentpath+ "/crypto/algos/" +classname+".class");
        try {
            byte[] fileContent = Files.readAllBytes(f.toPath());
            String fname = f.getName();
            return defineClass(name,fileContent,0,fileContent.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
