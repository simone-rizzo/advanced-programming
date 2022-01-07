package com.designframework.Rizzo;

import javafx.util.Pair;

import java.util.stream.Stream;

public abstract class AJob<K,V> {
    abstract Stream<Pair<K,V>> execute();
}
