import java.util.Arrays;

public class Pop {

    //order of customers
    int[] genotype;
    //how many vehicles to use
    int vehicles;


    float fitness = Float.MAX_VALUE;

    public Pop(int[] genotype, int vehicles) {
        this.genotype = genotype;
        this.vehicles = vehicles;
    }

    public Pop(Pop pop) {
        this.genotype = pop.genotype.clone();
        this.vehicles = pop.vehicles;
    }

    // SJEKKER IKKE OM FOR MANGE BILER FRA SAMME DEPOT
    // BEREGN ALLE EUCLIDEAN DISTANCES PÅ FORHÅND I BEAN
    public float calculateFitness(Bean bean) {
        //int[][] routes = new int[vehicles][20];
        fitness = 0;
        int[] od = new int[vehicles]; // origin depot of each vehicle
        int[] vd = new int[vehicles]; //demand per vehicle
        for (int i = 0; i < vehicles; i++) {
            // move each vehicle to their first customer
            od[i] = bean.nearestDepot[i];
            fitness += bean.depotCustomerDist[od[i]][genotype[i]];
            vd[i] = bean.service_demand[i];
            //if(bean.service_demand[i]>bean.vehicle_load[od[i]]){
            //    fitness = Float.MAX_VALUE;
            //    return Float.MAX_VALUE;
            //}
        }

        int[] lc = new int[vehicles]; //last customer
        int cv = -1; //current vehicle
        for (int i = vehicles; i < genotype.length; i++) {
            // move each vehicle to their next customer until capacity reached
            int j;
            for (j = 0; j < vehicles; j++) {
                cv++;
                if (cv == vehicles) {
                    cv = 0;
                }
                if (vd[cv] > bean.vehicle_load[od[cv]]) {
                    continue; //current vehicle up to capacity
                }
                vd[cv] += bean.service_demand[i];
                if (vd[cv] > bean.vehicle_load[od[cv]]) {
                    fitness += bean.depotCustomerDist[bean.nearestDepot[lc[cv]]][lc[cv]];
                    continue; //current vehicle up to capacity and returning to depot
                }
                fitness += bean.customerDist[lc[cv]][genotype[i]];
                break; //customer successfully served
            }
            if (j == vehicles) {
                fitness = Float.MAX_VALUE;
                return Float.MAX_VALUE;
            }
        }
        return fitness;
    }

    public String toString() {
        return ("Fitness: " + fitness + "  Vehicles: " + vehicles + "  Genotype: " + Arrays.toString(genotype) + "\n");
    }
}
