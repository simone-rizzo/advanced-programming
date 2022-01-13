package com.gabriele;
import com.Rizzo.Decrypt;
import com.Rizzo.Encrypt;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Much of the code of TestAlgs can be reused, in fact, 
 * TestAlgsPlus is nothing but a subtype of TestAlgs class, 
 * which override the `testCiphers` method adding controls on annotated methods.
 */
public class TestAlgsPlus extends TestAlgs {

    /**
     * *
     * Return the Method annotated with the class `annot`. If the method is not
     * found an empty Optional will be returned.
     *
     * @param <T> The generic type extending Java Annotations.
     * @param clazz The class which will be looked up to find the method.
     * @param annot The annotation `annotating` the looked method.
     * @return
     */
    public static <T extends Annotation> Optional<Method> classMethodWithAnnot(Class<?> clazz, Class<T> annot) {
        return Arrays.stream(clazz.getMethods()).filter((m) -> {

            var paramTypes = m.getParameterTypes();
            var annoted = m.getAnnotation(annot);

            return (paramTypes.length == 1) && (m.getAnnotations().length == 1)
                    && paramTypes[0].getSimpleName().equals("String")
                    && annoted != null;

        }).findFirst();
    }

    protected static void testCiphers(KeyRegistry registry, List<String> testSet, List<Class<?>> classes)
            throws IOException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, IllegalAccessException, InstantiationException {

        for (var clazz : classes) {

            /* 1. Does the class have a public constructor? */
            if (!classHasPublicConstructor(clazz)) {
                System.out.printf("[Warning] The class '%s' doesn't have a public constructor. Skipped.\n", clazz.getName());
                continue;
            }

            /* 2. Does the class contain a method starting with `enc` with one string param or an annotated method with @com.Rizzo.Encrypt? */
            var boxedEncMethod = classMethodStartingWith(clazz, "enc");
            var boxedAnnotEncMethod = classMethodWithAnnot(clazz, Encrypt.class);
            if (boxedEncMethod.isEmpty() && boxedAnnotEncMethod.isEmpty()) {
                System.out.printf("[Warning] The class '%s' doesn't have a method starting with 'enc' or annotated with @com.Rizzo.Encrypt. Skipped.\n", clazz.getName());
                continue;
            }

            /* 3. Does the class contain a method starting with `dec` with one string param or an annotated method with @com.Rizzo.Decrypt? */
            var boxedDecMethod = classMethodStartingWith(clazz, "dec");
            var boxedAnnotDecMethod = classMethodWithAnnot(clazz, Decrypt.class);
            if (boxedDecMethod.isEmpty() && boxedAnnotDecMethod.isEmpty()) {
                System.out.printf("[Warning] The class '%s' doesn't have a method starting with 'dec' or annotated with @com.Rizzo.Decrypt. Skipped.\n", clazz.getName());
                continue;
            }

            /* 
                Since all checks are passed, unbox the contained method for encryption/decryption
                and allocate a new instance of the current cipher.
             */
            var boxedKey = registry.get(clazz);

            if (boxedKey.isPresent()) {

                var cipher = clazz.getConstructors()[0].newInstance(boxedKey.get());
                /* Use ternary operator to select the Encryption/Decryption method got from the class. */
                var encMethod = (boxedEncMethod.isPresent()) ? boxedEncMethod.get() : boxedAnnotEncMethod.get();
                var decMethod = (boxedDecMethod.isPresent()) ? boxedDecMethod.get() : boxedAnnotDecMethod.get();

                testCipher(testSet, cipher, encMethod, decMethod);

            } else {
                System.out.printf("[Warning] Cannot test the class '%s' because a key wasn't found inside the KeyRegistry!\n", clazz.getName());
            }

        }

    }

}
