import java.util.*;

public class GA {

    Pop[] population;
    int popSize;
    double mutationRateM = 1;
    double mutationRateS = 1;
    double mutationRateA = 0.07;
    double mutationRateR = 0.1;
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
            float chance = r.nextFloat();
            if(chance<= mutationRateM){
                Mutator.MutateM(children[i],r);
            }
            chance = r.nextFloat();
            if(chance<= mutationRateS){
                Mutator.MutateS(children[i],r);
            }
            chance = r.nextFloat();
            if(chance<= mutationRateA){
                Mutator.MutateA(children[i],r,bean);
            }
            chance = r.nextFloat();
            if(chance<= mutationRateR){
                Mutator.MutateR(children[i],r,bean);
            }
            children[i].calculateFitness(bean);
        }
        Arrays.sort(children, new SortPop());

        int sel = 0;
        population = Selector.select(children,population);
        Arrays.sort(population, new SortPop());
    }



    class SortPop implements Comparator<Pop> {
        public int compare(Pop a, Pop b) {
            if ( a.fitness > b.fitness ) return 1;
            else if ( a.fitness == b.fitness ) return 0;
            else return -1;
        }
    }
}
