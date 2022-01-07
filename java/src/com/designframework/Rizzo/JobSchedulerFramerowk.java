package com.designframework.Rizzo;


import java.util.List;
import java.util.stream.Collectors;
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
        return job.flatMap(x -> x.execute());
    }

    private Stream<Pair<K, List<V>>> collect(Stream<Pair<K,V>> computed_stream){
        var map = computed_stream.collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())));
        return map.entrySet().stream().map(word -> new Pair(word.getKey(), word.getValue()));
    }

    /**
     * Method that must be overrided
     * @return
     */
    abstract void output(Stream<Pair<K, List<V>>> collected_stream);
}
