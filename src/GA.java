import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;

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

        int bound = bean.totalVehicles-bean.minVehicles+1;

        for (int i = 0; i < popSize; i++) {
            int[] genotype = IntStream.range(0, bean.customers).toArray();
            //int[] genotype = {40,12,3,10,22,47,45,48,9,28,19,39,24,17,31,6,7,11,29,38,1,2,18,13};
            int vehicles = r.nextInt(bound)+bean.minVehicles ;
            Util.shuffleArray(genotype);
            population[i] = new Pop(genotype,vehicles);
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
                Mutator.MutateG(children[i],r);
            }
            chance = r.nextFloat();
            if(chance<=mutationRateV){
                Mutator.MutateV(children[i],r,bean);
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
