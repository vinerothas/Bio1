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

//    static double mutationRateM = 0.8;
//    static double mutationRateS = 0.8;
//    static double mutationRateA = 0.07;
//    static double mutationRateR = 0.04;
//    static double crossoverRate = 0.001;

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
        try {
            for (int i = startIndex; i < endIndex; i++) {
                float chance = r.nextFloat();
                if (chance <= Param.crossoverRate) {
                    children[i] = Crosser.crossPMX(population[i], population[r.nextInt(population.length)], bean, r);
                } else  {
                    children[i] = new Pop(population[i]);
                    mutate(children[i]);
                }
                children[i].calculateFitness(bean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        latch.countDown();
    }

    public void mutate(Pop child){

        float chance = r.nextFloat();
        if(chance<=Param.mutationRate){
            //for (int i = 0; i < r.nextInt(10); i++) {
                if(chance<= Param.mutationRateM) {
                    Mutator.MutateM(child, r);
                }else if(chance<= Param.mutationRateC+Param.mutationRateM){
                    Mutator.MutateC(child,r,bean);
                }else if(chance<= Param.mutationRateC+Param.mutationRateM+Param.mutationRateS){
                    Mutator.MutateS(child,r);
                }else{
                    Mutator.MutateD(child,r,bean);
                }
               // chance = r.nextFloat();
            //}
        }

    }

}
