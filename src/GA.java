import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class GA {

    Pop[] population;
    int popSize = 100;
    float mutationRate;
    float crossoverRate;
    Bean bean;
    //enum mutation type
    //enum crossover type

    public GA(Bean bean) {
        this.bean = bean;
        population = new Pop[popSize];
        Random r = new Random(System.currentTimeMillis());
        int bound = bean.vehicles*bean.depots-1;

        for (int i = 0; i < popSize; i++) {
            int[] genotype = IntStream.range(0, bean.customers).toArray();
            int vehicles = r.nextInt(bound)+1 ;
            Util.shuffleArray(genotype);
            population[i] = new Pop(genotype,vehicles);
            population[i].calculateFitness(bean);
        }
        System.out.println(Arrays.toString(population));
    }

    private void run_generation() {

    }
}
