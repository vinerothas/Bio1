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
    double mutationRateM = 0.8;
    double mutationRateS = 0.8;
    double mutationRateA = 0.07;
    double mutationRateR = 0.04;
    double crossoverRate;
    int threads;
    long time = 0;
    ThreadPoolExecutor executor;

    Bean bean;
    //enum mutation type
    //enum crossover type
    Random r = new Random(System.currentTimeMillis());

    public GA(Bean bean, int pops, int threads) {
        this.bean = bean;
        this.popSize = pops;
        this.threads = threads;
        population = new Pop[popSize];

        for (int i = 0; i < popSize; i++) {
            population[i] = new Pop(r, bean);
            population[i].calculateFitness(bean);
        }
        Arrays.sort(population, new SortPop());
        //System.out.println(Arrays.toString(population));

        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        executor.setMaximumPoolSize(threads);
        executor.setCorePoolSize(threads);
        executor.setKeepAliveTime(60, TimeUnit.SECONDS);
    }

    public void run_generation() {
        Pop[] children = new Pop[popSize];

        //long timeBefore = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            int startIndex = popSize/threads*i;
            int endIndex = popSize/threads*(i+1);
            GAthread thread = new GAthread(bean,population,children, startIndex,endIndex,latch);
            executor.submit(() -> {
                thread.run();
                return null;
            });
        }

        try {
            latch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
            return; // I guess I'll die \_(0_o)_/
        }
        //time += System.currentTimeMillis()-timeBefore;

        Arrays.sort(children, new SortPop());
        population = Selector.select(children,population);
        Arrays.sort(population, new SortPop());
    }

    public void mutate(Pop child){
        double mutationRateM = this.mutationRateM;
        double mutationRateS = this.mutationRateS;
        double mutationRateA = this.mutationRateA;
        double mutationRateR = this.mutationRateR;

        while(mutationRateM>0){
            if(mutationRateM>=1){
                Mutator.MutateM(child,r);
            }else{
                float chance = r.nextFloat();
                if(chance<= mutationRateM) {
                    Mutator.MutateM(child, r);
                }
            }
            mutationRateM--;
        }

        while(mutationRateS>0){
            if(mutationRateS>=1){
                Mutator.MutateS(child,r);
            }else{
                float chance = r.nextFloat();
                if(chance<= mutationRateS){
                    Mutator.MutateS(child,r);
                }
            }
            mutationRateS--;
        }

        float chance = r.nextFloat();
        if(chance<= mutationRateA){
            Mutator.MutateA(child,r,bean);
        }
        chance = r.nextFloat();
        if(chance<= mutationRateR){
            Mutator.MutateR(child,r,bean);
        }
    }



    class SortPop implements Comparator<Pop> {
        public int compare(Pop a, Pop b) {
            if ( a.fitness > b.fitness ) return 1;
            else if ( a.fitness == b.fitness ) return 0;
            else return -1;
        }
    }
}
