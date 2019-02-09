package main;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class GAthread implements Runnable {

    Bean bean;
    Pop[] population;
    Pop[] children;
    int startIndex;
    int endIndex;
    CountDownLatch latch;
    Random r = new Random(System.currentTimeMillis());

    static double mutationRateM = 0.8;
    static double mutationRateS = 0.8;
    static double mutationRateA = 0.07;
    static double mutationRateR = 0.04;
    static double crossoverRate = 0.001;

    public GAthread(Bean bean, Pop[] population, Pop[] children, int startIndex, int endIndex, CountDownLatch latch){
        this.bean = bean;
        this.population = population;
        this.children = children;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.latch = latch;
    }

    @Override
    public void run() {
        for(int i = startIndex; i<endIndex;i++){
            float chance = r.nextFloat();
            if(chance<= crossoverRate){
                if(i==0) {
                    children[i] = Crosser.cross(population[i],population[i+1],bean,r);
                }else{
                    children[i] = Crosser.cross(population[i],population[i-1],bean,r);
                }
            }else{
                children[i] = new Pop(population[i]);
                mutate(children[i]);
            }
            children[i].calculateFitness(bean);
        }
        latch.countDown();
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

}
