package com.Rizzo;

import java.util.HashMap;

public class KeyRegistry {

    private HashMap<String,Class> registry;

    public KeyRegistry() {
        this.registry = new HashMap<>();
    }

    public void add(Class<?> c, String key){
        this.registry.put(key,c);
    }

    public void get(Class c){
        this.registry.containsValue(c);
        this.registry.keySet().
    }
}
