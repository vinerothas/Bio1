package main;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GA {

    Pop[] population;
    int popSize;
    int threads;
    long time = 0;
    ThreadPoolExecutor executor;
    Bean bean;
    Random r = new Random(System.currentTimeMillis());
    //Selector selector;
    GAthread[] gathreads;

    public GA(Bean bean, int pops, int threads) {
        this.bean = bean;
        this.popSize = pops;
        this.threads = threads;
        population = new Pop[popSize];
        //selector = new Selector(popSize,bean,r);

        for (int i = 0; i < popSize; i++) {
            population[i] = new Pop(r, bean);
            population[i].calculateFitness(bean, null);
        }
        Arrays.sort(population, new SortPop());
        //System.out.println(Arrays.toString(population));

        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        executor.setMaximumPoolSize(threads);
        executor.setCorePoolSize(threads);
        executor.setKeepAliveTime(60, TimeUnit.SECONDS);

        gathreads = new GAthread[threads];
        for (int i = 0; i < threads; i++) {
            int startIndex = popSize/threads*i;
            int endIndex = popSize/threads*(i+1);
            gathreads[i] = new GAthread(bean,population,null, startIndex,endIndex,null);
        }
    }

    public void run_generation() {
        Pop[] children = new Pop[popSize];

        CountDownLatch latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            gathreads[i].population = population;
            gathreads[i].children = children;
            gathreads[i].latch = latch;
            GAthread gat = gathreads[i];
            executor.submit(() -> {
                gat.run();
                return null;
            });
        }

        try {
            latch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
            return; // \_(0_o)_/
        }

        chooseNextGen2(children);
    }

    private void chooseNextGen2(Pop[] children){
        Arrays.sort(children, new SortPop());
        Pop[] nextGen = new Pop[popSize];


        int ci = 0;
        int pi = 0;

        if(children[0].fitness<population[0].fitness){
            nextGen[0] = children[ci++];
        }else{
            nextGen[0] = population[pi++];
        }

        for (int i = 1; i < popSize ; i++) {
            if(children[ci].fitness<population[pi].fitness && children[ci].fitness-nextGen[i-1].fitness > Param.fitnessComparison){
                nextGen[i] = children[ci++];
            }else{
                nextGen[i] = population[pi++];
            }
        }
        population = nextGen;
    }

    private void chooseNextGen(Pop[] children){
        Arrays.sort(children, new SortPop());
        Pop[] nextGen = new Pop[popSize];

        int ci = 0;
        int pi = 0;

        if(children[0].fitness<population[0].fitness){
            nextGen[0] = children[ci++];
        }else{
            nextGen[0] = population[pi++];
        }
        for (int i = 1; i < popSize ; i++) {
            if(children[ci].fitness<population[pi].fitness){
                nextGen[i] = children[ci++];
            }else{
                nextGen[i] = population[pi++];
            }
        }
        population = nextGen;
    }

    class SortPop implements Comparator<Pop> {
        public int compare(Pop a, Pop b) {
            if ( a.fitness > b.fitness ) return 1;
            else if ( a.fitness == b.fitness ) return 0;
            else return -1;
        }
    }
}
