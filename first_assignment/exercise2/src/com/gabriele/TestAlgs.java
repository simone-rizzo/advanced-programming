package com.gabriele;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestAlgs {

    /**
     * Return true if the class contains at most one public constructor, false
     * otherwise.
     *
     * @param clazz
     * @return
     */
    public static boolean classHasPublicConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getConstructors()).anyMatch((cons) -> Modifier.isPublic(cons.getModifiers()));
    }

    /**
     * Return the method starting with `start` if it is contained inside the
     * class, otherwise return an empty optional.
     *
     * @param clazz
     * @param start
     * @return
     */
    public static Optional<Method> classMethodStartingWith(Class<?> clazz, String start) {
        return Arrays.stream(clazz.getMethods()).filter((m) -> {
            var paramTypes = m.getParameterTypes();
            return m.getName().startsWith(start) && paramTypes.length == 1 && paramTypes[0].getSimpleName().equals("String");
        }).findFirst();
    }

    /***
     * Entry point of the program shared across all sub-types.
     * @param args 
     */
    public final static void main(String[] args) {

        /* The program in order to work it does need the path to the folder containing the crypto package. */
        if (args.length != 1) {
            System.err.println("usage: java com.gabriele.TestAlgs \"path/to/crypto/parent\"");
            return;
        }

        /* Path containing the crypto package. */
        var cryptoParentPath = args[0];

        try {

            /* Create the com.gabriele.KeyRegistry by reading the `keys.list file` which contains the keys. */
            var keyRegistry = createKeyRegistry(cryptoParentPath);
            /* Read the strings used as test set for the ciphers contained in `secret.list` */
            var testSet = loadTestSet(cryptoParentPath);
            /* Load classes contained in `crypto.algos` package. */
            var classes = loadAlgosClass(cryptoParentPath);
            
            /* Run the test framework to test the loaded ciphers */
            testCiphers(keyRegistry, testSet, classes);
            
        } catch (IOException e) {
            System.err.println("[Error] Unable to open keys file. Exception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("[Error] Unable to load a class. Exception: " + e.getMessage());
        } catch ( IllegalArgumentException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            System.err.println("[Error] Got an exception when testing the classes: " + e.getMessage());
        }

    }

    /**
     * Create a new Registry Key by reading the classes contained inside keys
     * file.
     *
     * @param keysFilename The "keys.list" filename.
     * @return A registry key containing all the crypto algorithms
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static KeyRegistry createKeyRegistry(String cryptoParentPath)
            throws IOException, ClassNotFoundException {

        var keyRegistry = new KeyRegistry();
        var loader = new CipherClassLoader(cryptoParentPath);
        var keysFilename = cryptoParentPath + "/crypto/keys.list";
        var algos = Files.readAllLines(Path.of(keysFilename));

        for (var algo : algos) {

            /* Each line contains a class name and a key separated by a single space. */
            var splitted = algo.split(" ");
            var className = splitted[0];
            var key = splitted[1];

            /* Try to load the class contained in class name */
            var loadedClass = loader.loadClass(className);

            /* Add the class to the registry! */
            keyRegistry.add(loadedClass, key);
        }

        return keyRegistry;
    }

    /***
     * Load the words contained in the crypto/secret.list file
     * @param cryptoParentPath
     * @return
     * @throws IOException 
     */
    private static List<String> loadTestSet(String cryptoParentPath)
            throws IOException {
        return Files.readAllLines(Path.of(cryptoParentPath + "/crypto/secret.list"));
    }

    /***
     * Load from the crypto package all the classes contained in crypto.algos.
     * @param cryptoParentPath
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private static List<Class<?>> loadAlgosClass(String cryptoParentPath)
            throws IOException, ClassNotFoundException {

        var loader = new CipherClassLoader(cryptoParentPath);
        var keysFilename = cryptoParentPath + "/crypto/keys.list";
        var algos = Files.readAllLines(Path.of(keysFilename));
        var classes = new ArrayList<Class<?>>(algos.size());

        for (var algo : algos) {

            /* Each line contains a class name and a key separated by a single space. */
            var splitted = algo.split(" ");
            /* We do care only for the class name, ignoring the key... */
            var className = splitted[0];

            /* Try to load the class contained in class name */
            var loadedClass = loader.loadClass(className);
            classes.add(loadedClass);
        }

        return classes;
    }

    /***
     * The core of the test framework. From the com.gabriele.KeyRegistry takes the strings used
     * as keys for the cipher classes. Each cipher will be tested with each word contained
     * inside the test set. Each class is subject to three main checks that must be passed.
     * A sub-type extending the com.gabriele.TestAlgs can override this method to add other tests/conditions (see #com.gabriele.TestAlgsPlus).
     * @param registry
     * @param testSet
     * @param classes
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException 
     */
    protected static void testCiphers(KeyRegistry registry, List<String> testSet, List<Class<?>> classes)
            throws IOException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IllegalAccessException, InstantiationException {

        for (var clazz : classes) {

            /* 1. Does the class have a public constructor? */
            if (!classHasPublicConstructor(clazz)) {
                System.out.printf("[Warning] The class '%s' doesn't have a public constructor. Skipped.\n", clazz.getName());
                continue;
            }

            /* 2. Does the class contain a method starting with `enc` with one string param? */
            var boxedEncMethod = classMethodStartingWith(clazz, "enc");
            if (boxedEncMethod.isEmpty()) {
                System.out.printf("[Warning] The class '%s' doesn't have a method starting with 'enc'. Skipped.\n", clazz.getName());
                continue;
            }

            /* 3. Does the class contain a method starting with `dec` with one string param? */
            var boxedDecMethod = classMethodStartingWith(clazz, "dec");
            if (boxedDecMethod.isEmpty()) {
                System.out.printf("[Warning] The class '%s' doesn't have a method starting with 'dec'. Skipped.\n", clazz.getName());
                continue;
            }

            /* 
                Since all checks are passed, unbox the contained method for encryption/decryption
                and allocate a new instance of the current cipher.
             */
            var boxedKey = registry.get(clazz);

            if (boxedKey.isPresent()) {
                String a = boxedKey.get();
                var cipher = clazz.getConstructors()[0].newInstance(a);
                var encMethod = boxedEncMethod.get();
                var decMethod = boxedDecMethod.get();
                testCipher(testSet, cipher, encMethod, decMethod);
            } else {
                System.out.printf("[Warning] Cannot test the class '%s' because a key wasn't found inside the com.gabriele.KeyRegistry!\n", clazz.getName());
            }

        }

    }

    /***
     * The method that checks if the ciphers work as expected.
     * @param testSet
     * @param cipher
     * @param encMethod
     * @param decMethod
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IllegalAccessException 
     */
    protected final static void testCipher(List<String> testSet, Object cipher, Method encMethod, Method decMethod)
            throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {

        for (var secretWord : testSet) {

            /* Perform encryption/decryption method onto the current cipher instance. */
            var encWord = (String) encMethod.invoke(cipher, secretWord);
            var decWrd = (String) decMethod.invoke(cipher, encWord);

            /* If the decrypted word is not equal to the secret one then report an error. */
            if (!decWrd.contains(secretWord)) {
                System.out.printf("[%s] - KO: %s -> %s -> %s\n", cipher.getClass().getSimpleName(), secretWord, encWord, decWrd);
            }

        }
    }
}
