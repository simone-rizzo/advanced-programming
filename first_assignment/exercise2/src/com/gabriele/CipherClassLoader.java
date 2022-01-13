package com.gabriele;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author gabryon
 */
public final class CipherClassLoader extends ClassLoader {

    private final String cryptoParent;

    public CipherClassLoader(String cryptoParent) {
        this.cryptoParent = cryptoParent;
    }

    @Override
    public Class findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassFromFile(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName) {

        var path = cryptoParent + "/" + fileName.replace('.', File.separatorChar) + ".class";

        try {

            var inputStream = new FileInputStream(path);

            byte[] buffer;
            var byteStream = new ByteArrayOutputStream();
            int nextValue;

            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }

            buffer = byteStream.toByteArray();

            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}