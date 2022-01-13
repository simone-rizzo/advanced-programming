package com.gabriele;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class KeyRegistry {
    
    /***
     * The class {@code Class} inherits its {@code Class.equals} and {@code Class.hashCode}
     * methods from the {@code Object} class, thus the class comparison inside a 
     * {@code Map<Class<?>, Object>} will never work unless the compared references point to the same object.
     * In solution to this problem, a Wrapper class has been implemented to be used with
     * a {@code Map} object. The {@code ClassWrapper.equals} and {@code ClassWrapper.hashCode}
     * rely on the {@code Class.getName} to perform the comparison.
     */
    private class ClassWrapper {
        
        private final Class<?> clazz;
        
        public ClassWrapper(Class<?> clazz) {
            this.clazz = clazz;
        }
        
        @Override
        public boolean equals(Object o) {
            
            if (this == o) return true;
            
            var casted = (ClassWrapper) o;
            return casted.clazz.getName().equals(this.clazz.getName());
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89 * hash + Objects.hashCode(this.clazz.getName());
            return hash;
        }
    }
    

    private final HashMap<ClassWrapper, String> hashAlgorithms = new HashMap<>();

    public void add(Class<?> c, String key) {
        hashAlgorithms.put(new ClassWrapper(c), key);
    }

    public Optional<String> get(Class<?> c) {
        
        var temp = new ClassWrapper(c);
        if (hashAlgorithms.containsKey(temp)) {
            return Optional.of(hashAlgorithms.get(temp));
        }

        return Optional.empty();
    }


}
