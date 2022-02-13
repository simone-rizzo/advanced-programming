package com.designframework.Rizzo.framework;

public class JobSchedulerContext<K,V> {
    private JobSchedulerStrategy strategy;

    public JobSchedulerContext(JobSchedulerStrategy<K,V> strategy) {
        this.strategy = strategy;
    }

    /**
     * Method that start the entire framework by concatenating
     * emit->compute->collect->output
     */
    public void start(){
        if (strategy == null) {
            throw new RuntimeException("The strategy can't be null");
        }
        strategy.output(strategy.collect(strategy.compute(strategy.emit())));
    }

    public void setStrategy(JobSchedulerStrategy<K,V> strategy){
        this.strategy=strategy;
    }
}
