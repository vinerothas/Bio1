import java.util.Random;

public class Mutator {

    public static void MutateG(Pop pop, Random r){
        int i = r.nextInt(pop.genotype.length);
        int j = r.nextInt(pop.genotype.length);
        int first = pop.genotype[i];
        pop.genotype[i] = pop.genotype[j];
        pop.genotype[j] = first;
    }

    public static void MutateV(Pop pop, Random r, Bean bean){
        boolean positive = r.nextBoolean();
        if(positive){
            if(pop.vehicles != bean.totalVehicles){
                pop.vehicles++;
            }
        }else{
            if(pop.vehicles != bean.minVehicles){
                pop.vehicles--;
            }
        }
    }
}
