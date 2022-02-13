package com.Rizzo;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Registry of all the algoritms and its correspondig keys.
 */
public class KeyRegistry {

    private HashMap<Class<?>,String> registry;

    public KeyRegistry() {
        this.registry = new HashMap<>();
    }

    /**
     * Add a new key for the algorithm class c.
     * @param c Class of the algorithm
     * @param key key associated to the algorithm.
     */
    public void add(Class<?> c, String key){
        this.registry.put(c, key);
    }

    /**
     * Get the key associated to the class c.
     * @param c Class of the Algorithm.
     * @return
     */
    public Optional<String> get(Class<?> c){
        if(!registry.containsKey(c))
            return  Optional.empty();
        return Optional.of(this.registry.get(c));
    }

    /**
     * Get all Algorithm classes inside the registry.
     * @return
     */
    public Optional<Set<Class<?>>> getAllClasses(){
        if(registry.isEmpty())
            return  Optional.empty();
        return Optional.of(registry.keySet());
    }
}
