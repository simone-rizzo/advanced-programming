package com.designframework.Rizzo;
import java.util.stream.Stream;

public abstract class AJob<K,V> {
    abstract Stream<Pair<K,V>> execute();
}
