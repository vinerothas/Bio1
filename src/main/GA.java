package main;

import java.util.*;

public class GA {

    Pop[] population;
    int popSize;
    double mutationRateM = 0.8;
    double mutationRateS = 0.8;
    double mutationRateA = 0.07;
    double mutationRateR = 0.04;
    double crossoverRate;
    Bean bean;
    //enum mutation type
    //enum crossover type
    Random r = new Random(System.currentTimeMillis());

    public GA(Bean bean, int pops) {
        this.bean = bean;
        this.popSize = pops;
        population = new Pop[popSize];

        for (int i = 0; i < popSize; i++) {
            population[i] = new Pop(r, bean);
            population[i].calculateFitness(bean);
        }
        Arrays.sort(population, new SortPop());
        //System.out.println(Arrays.toString(population));
    }

    public void run_generation() {
        Pop[] children = new Pop[population.length];
        for(int i = 0; i<population.length;i++){
            children[i] = new Pop(population[i]);
            mutate(children[i]);
            children[i].calculateFitness(bean);
        }
        Arrays.sort(children, new SortPop());

//        mutationRateM -= mutationRateM*0.001;
//        mutationRateS -= mutationRateS*0.001;
//        mutationRateA -= mutationRateA*0.001;
//        mutationRateR -= mutationRateR*0.001;

        int sel = 0;
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
