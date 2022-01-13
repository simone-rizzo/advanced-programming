package com.Rizzo;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class KeyRegistry {

    private HashMap<Class,String> registry;

    public KeyRegistry() {
        this.registry = new HashMap<>();
    }

    public void add(Class<?> c, String key){
        this.registry.put(c, key);
    }

    public Optional<String> get(Class c){
        return Optional.of(this.registry.get(c));
    }
    public Set<Class> getAllClasses(){
        return registry.keySet();
    }
}
