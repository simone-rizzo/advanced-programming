package com.Rizzo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)     // available for reflection
@Target(ElementType.METHOD)             // used only to annotate methods
public @interface Encrypt {
    
}