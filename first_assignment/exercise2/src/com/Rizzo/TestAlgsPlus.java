package com.Rizzo;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TestAlgsPlus extends TestAlgs by enriching
 * the testing framework by using annotations.
 */
public class TestAlgsPlus extends TestAlgs {

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
            Optional<Method> opt_annot_ecn = getAnnotatedMethod(c,Encrypt.class);
            Optional<Method> opt_annot_dec = getAnnotatedMethod(c,Decrypt.class);
            if((opt_encm.isEmpty() && opt_annot_ecn.isEmpty()) || (opt_decm.isEmpty() && opt_annot_dec.isEmpty())){
                System.out.printf(">%s: [WARNING]  Encryption / Decryption methods not found \n",classname);
                continue;
            }
            String key = keyreg.get(c).get();
            Object obj = c.getConstructors()[0].newInstance(key);
            Method encm = opt_encm.isPresent()?opt_encm.get():opt_annot_ecn.get();
            Method decm = opt_decm.isPresent()?opt_decm.get():opt_annot_dec.get();
            runEncDecTest(c.getSimpleName(),obj, encm, decm,secret_list);
        }
    }

    /**
     * Return the annotated method if present otherwise it return Empty optional.
     * @param c Class in wich we want to get the first annotated method.
     * @param ann Annotation that we are searching for.
     * @return the first method annotated with ann.
     */
    static Optional<Method> getAnnotatedMethod(Class c, Class ann){
        Method[] class_methods = c.getMethods();
        for(Method m : class_methods){
            var parameterTypes = m.getParameterTypes();
            Annotation[] annotations = m.getAnnotations();
            if(annotations.length == 1 && annotations[0].annotationType().getSimpleName().equals(ann.getSimpleName()) && parameterTypes.length == 1 && parameterTypes[0].equals(String.class)){
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }
}
