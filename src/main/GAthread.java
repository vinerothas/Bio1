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

    public GAthread(Bean bean, Pop[] population, Pop[] children, int startIndex, int endIndex, CountDownLatch latch) {
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
            for (int i = startIndex; i < endIndex; i += 2) {
                tournamentSelection(i);
                children[i].calculateFitness(bean, null);
                children[i+1].calculateFitness(bean, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

    public void tournamentSelection(int index) {
        int bestIndex;
        int secondBestIndex;

        int i1 = r.nextInt(population.length);
        int i2 = r.nextInt(population.length);
        if (population[i1].fitness > population[i2].fitness) {
            bestIndex = i1;
            secondBestIndex = i2;
        } else {
            bestIndex = i2;
            secondBestIndex = i1;
        }

        for (int i = 0; i < Param.tournamentSize - 2; i++) {
            i1 = r.nextInt(population.length);
            if (population[i1].fitness > population[secondBestIndex].fitness) {
                if (population[i1].fitness > population[bestIndex].fitness) {
                    secondBestIndex = bestIndex;
                    bestIndex = i;
                } else {
                    secondBestIndex = i;
                }
            }
        }

        float chance = r.nextFloat();
        if (chance <= Param.crossoverRate) {
            children[index] = Crosser.cross(population[bestIndex], population[secondBestIndex], bean, r);
            children[index + 1] = Crosser.cross(population[secondBestIndex], population[bestIndex], bean, r);
        }else{
            children[index] = new Pop(population[bestIndex]);
            children[index+1] = new Pop(population[secondBestIndex]);
            mutate(children[index]);
            mutate(children[index+1]);
        }
    }

    public void mutate(Pop child) {
        float chance = r.nextFloat();
        if (chance <= Param.mutationRateM) {
            chance = r.nextFloat();
            if(chance < Param.noisyOperatorChance) {
                Mutator.MutateM(child, r, bean, false);
            }else{
                Mutator.MutateM(child, r, bean, true);
            }
        } else if (chance >= 1-Param.mutationRateS) {
            if(chance < Param.noisyOperatorChance) {
                Mutator.MutateS(child, r, bean, false);
            }else{
                Mutator.MutateS(child, r, bean, true);
            }
        } else {
            Mutator.MutateI(child, r,bean);
        }

    }

}
