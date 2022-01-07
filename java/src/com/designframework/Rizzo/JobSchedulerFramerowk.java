package com.designframework.Rizzo;

import javafx.util.Pair;

import java.util.List;
import java.util.stream.Stream;

public abstract class JobSchedulerFramerowk<K,V> {
    /**
     * Method that must be overrided
     * @return
     */
    abstract Stream<AJob> emit();

    public void main(){
        output(collect(compute(emit())));
    }

    private Stream<Pair<K,V>> compute(Stream<AJob> job){
        return null;
    }
    private Stream<Pair<K, List<V>>> collect(Stream<Pair<K,V>> computed_stream){
        return null;
    }

    /**
     * Method that must be overrided
     * @return
     */
    abstract void output(Stream<Pair<K, List<V>>> collected_stream);
}
