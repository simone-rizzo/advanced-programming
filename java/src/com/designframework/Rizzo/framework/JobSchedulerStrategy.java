package com.designframework.Rizzo.framework;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class JobSchedulerStrategy<K,V> {
    /**
     * Method that generate a Stream of AJob
     * @return
     */
    protected abstract Stream<AJob<K,V>> emit();

    /**
     * Execute all the AJob from the emit stream by launcing execute(), and then
     * concatenate all the resulting streams inside a unique big stream of Pair<K,V>.
     * @param jobs_tream Stream of Ajob obtained from the emit method.
     * @return return the concatenation of the execution of all the Jobs.
     */
    public Stream<Pair<K,V>> compute(Stream<AJob<K,V>> jobs_tream){
        return jobs_tream.flatMap(x -> x.execute());
    }

    /**
     * Takes as input the output of compute, and groups all the pairs with the same key in a single pair, having the same key
     * and the list of all values.
     * @param computed_stream Stream of Pair obtained from compute method
     * @return return a stream with keys and list of words with that key.
     */
    protected Stream<Pair<K, List<V>>> collect(Stream<Pair<K,V>> computed_stream){
        var map = computed_stream.collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())));
        return map.entrySet().stream().map(word -> new Pair(word.getKey(), word.getValue()));
    }

    /**
     * Show the result of collect.
     * @return
     */
    abstract protected void output(Stream<Pair<K, List<V>>> collected_stream);
}
