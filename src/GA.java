import java.util.*;

public class GA {

    Pop[] population;
    int popSize = 300;
    float mutationRateM = 1;
    float mutationRateS = 1;
    float crossoverRate;
    Bean bean;
    //enum mutation type
    //enum crossover type
    Random r = new Random(System.currentTimeMillis());

    public GA(Bean bean) {
        this.bean = bean;
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
                Mutator.MutateM(children[i],r);
                Mutator.MutateM(children[i],r);
            }
            chance = r.nextFloat();
            if(chance<= mutationRateS){
                Mutator.MutateS(children[i],r);
                Mutator.MutateS(children[i],r);
                Mutator.MutateS(children[i],r);
            }
            children[i].calculateFitness(bean);
        }
        Arrays.sort(children, new SortPop());

        int sel = 0;
        population = Selector.select(sel,children,population);
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
