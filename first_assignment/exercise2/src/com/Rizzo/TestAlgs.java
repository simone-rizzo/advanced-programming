package com.Rizzo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestAlgs {

    public static void main(String[] args) {
        String path = "";
        if(args.length>0){
            path = args[0];
            try {
                List<String[]> keys_list = Files.lines(Paths.get(path+ "/crypto/keys.list")).map(x-> x.split(" ")).collect(Collectors.toList());
                List<String> secret_list = Files.lines(Paths.get(path+ "/crypto/secret.list")).collect(Collectors.toList());
                KeyRegistry keyreg = getClassesRegister(path, keys_list);
                runAlgoTest(keyreg,secret_list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void runAlgoTest(KeyRegistry keyreg, List<String> secret_list){
        for (Class c:keyreg.getAllClasses()) {
            String classname = getClassName(c);
            if(!hasPubliConstructor(c)){
                System.out.printf("%s: [SKIPPED] it doesnt have a public constructor \n",classname);
                continue;
            }
            if(!existMethodStartWith(c,"enc") || !existMethodStartWith(c,"dec")){
                System.out.printf("%s: [SKIPPED] Enc/Dec methods not found \n",classname);
                continue;
            }
            runEncDecTest(c,keyreg,secret_list);
        }
    }

    public static String getClassName(Class c){
        String[] splitted = c.getName().split("\\.");
        String classname = splitted[splitted.length -1];
        return classname;
    }

    protected static void runEncDecTest(Class c, KeyRegistry keyreg, List<String> secret_list){
        try {
            String key = keyreg.get(c).get();
            Object obj = c.getConstructors()[0].newInstance(key);
            Method encm = getMethodStartWith(c,"enc").get();
            Method decm = getMethodStartWith(c,"dec").get();
            for(String scrwrd:secret_list){
                String encripted = (String) encm.invoke(obj,scrwrd);
                String decripted = (String) decm.invoke(obj,encripted);
                if (!decripted.contains(scrwrd)){
                    System.out.printf("%s: [KO] %s -> %s -> %s\n", c.getSimpleName(), scrwrd, encripted, decripted);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static boolean existMethodStartWith(Class c,String startwith){
        Optional<Method> mtd = getMethodStartWith(c,startwith);
        return !mtd.isEmpty();
    }
    private static Optional<Method> getMethodStartWith(Class c,String startwith){
        return Arrays.stream(c.getMethods()).filter(m->{
            var parameters = m.getParameterTypes();
            return m.getName().startsWith(startwith) && parameters.length == 1 && parameters[0].getSimpleName().equals("String");
        }).findFirst();
    }

    public static boolean hasPubliConstructor(Class c) {
        return Arrays.stream(c.getConstructors()).anyMatch((constructor) -> Modifier.isPublic(constructor.getModifiers()));
    }

    private static KeyRegistry getClassesRegister(String folderpath, List<String[]> keys_list){
        MyClassLoader classLoader = new MyClassLoader(folderpath);
        KeyRegistry keyreg = new KeyRegistry();
        for(String[] alg:keys_list){
            try {
                Class c = classLoader.findClass(alg[0]);
                keyreg.add(c,alg[1]);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return keyreg;
    }
}
