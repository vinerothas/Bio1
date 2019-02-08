import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GA {

    Pop[] population;
    int popSize = 200;
    float mutationRateG = 1;
    float mutationRateV = 1;
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
        //System.out.println(Arrays.toString(population));
    }

    public void run_generation() {
        Pop[] children = new Pop[population.length];
        for(int i = 0; i<population.length;i++){
            children[i] = new Pop(population[i]);
            float chance = r.nextFloat();
            if(chance<=mutationRateG){
                //Mutator.MutateG(children[i],r);
            }
            chance = r.nextFloat();
            if(chance<=mutationRateV){
                //Mutator.MutateV(children[i],r,bean);
            }
            children[i].calculateFitness(bean);
        }
        Arrays.sort(children, new SortPop());
        //population = children;
        Arrays.sort(population, new SortPop());
        Pop[] nextGen = new Pop[population.length];
        for(int i = 0; i<population.length/2;i++){
            nextGen[i*2] = population[i];
            nextGen[i*2+1] = children[i];
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
