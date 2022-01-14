package com.Rizzo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TestAlgs dinamically loads the java .class files at runtime
 * and then check their correctness trough reflection.
 */

public class TestAlgs {
    /**
     * Main function
     * @param args the first argument must be the root path of "crypto"
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if(args.length!=1) {
            System.out.println("Please insert the parent folder of crypto as parameter.");
            return;
        }
        String path = args[0];
        List<String[]> keys_list = Files.lines(Paths.get(path+ "/crypto/keys.list")).map(x-> x.split(" ")).collect(Collectors.toList());
        List<String> secret_list = Files.lines(Paths.get(path+ "/crypto/secret.list")).collect(Collectors.toList());
        KeyRegistry keyreg = getClassesRegister(path, keys_list);
        runAlgoTest(keyreg,secret_list);
    }

    /**
     * This test check the structure and logic correctness of an algorithm by
     * taking into consideration also the annotation of the methods
     * @param keyreg the registry with the Class,key couples.
     * @param secret_list list of words for the Encription Decription test.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public static void runAlgoTest(KeyRegistry keyreg, List<String> secret_list) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Class c:keyreg.getAllClasses().get()) {
            String classname = c.getSimpleName();

            if(!hasPubliConstructor(c)){
                System.out.printf(">%s: [WARNING]  it doesnt have a public constructor \n",classname);
                continue;
            }
            Optional<Method> opt_encm = getMethodStartWith(c,"enc");
            Optional<Method> opt_decm = getMethodStartWith(c,"dec");
            if(opt_encm.isEmpty() || opt_decm.isEmpty()){
                System.out.printf(">%s: [WARNING]  Encryption / Decryption methods not found \n",classname);
                continue;
            }
            String key = keyreg.get(c).get();
            Object obj = c.getConstructors()[0].newInstance(key);
            runEncDecTest(c.getSimpleName(),obj, opt_encm.get(), opt_decm.get(),secret_list);
        }
    }

    /**
     * Run the test on the crypto algorithm by applying encrypt and then
     * decrypt on the words contained in secret_list.
     * secret words.
     * @param classname
     * @param obj Instantiate object of the algorithm
     * @param encm Encryption method
     * @param decm Decryption method
     * @param secret_list List of words to be used as test.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected static void runEncDecTest(String classname,Object obj, Method encm,Method decm, List<String> secret_list) throws InvocationTargetException, IllegalAccessException {
        for(String scrwrd:secret_list){
            String encripted = (String) encm.invoke(obj,scrwrd);
            String decripted = (String) decm.invoke(obj,encripted);
            if (!decripted.contains(scrwrd)){
                System.out.printf(">%s: [KO] %s -> %s -> %s\n", classname, scrwrd, encripted, decripted);
            }
        }
    }

    /**
     * Return the method starting with `startstartwith` string if the class contains it,
     * otherwise return an empty optional.
     * @param c
     * @param startwith
     * @return
     */
    protected static Optional<Method> getMethodStartWith(Class c,String startwith){
        Method[] class_methods = c.getMethods();
        for(Method m:class_methods){
            var parametersTypes = m.getParameterTypes();
            if( m.getName().startsWith(startwith) && parametersTypes.length == 1 && parametersTypes[0].equals(String.class))
                return Optional.of(m);
        }
        return Optional.empty();
    }

    /**
     * Check if a class has a public constructor.
     * @param c The class in wich we want to check
     * @return
     */
    public static boolean hasPubliConstructor(Class c) {
        return Arrays.stream(c.getConstructors()).anyMatch((constructor) -> Modifier.isPublic(constructor.getModifiers()));
    }

    /**
     * By reading the classess inside the keylist it dinamically load them and return
     * the KeyRegistry object in wich each class has its correspongind key.
     * @param folderpath
     * @param keys_list
     * @return
     * @throws MalformedURLException
     */
    protected static KeyRegistry getClassesRegister(String folderpath, List<String[]> keys_list) throws MalformedURLException {
        //Instantiate an empy Keyregistry.
        KeyRegistry keyreg = new KeyRegistry();

        File file = new File(folderpath);
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};

        //Instance of the class loader
        ClassLoader loader = new URLClassLoader(urls);

        for(String[] alg:keys_list){
            try {
                Class c = loader.loadClass(alg[0]);
                //add inside the registry
                keyreg.add(c,alg[1]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return keyreg;
    }
}