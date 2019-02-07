import java.util.Arrays;

public class Pop {

    //order of customers
    int[] genotype;
    //how many vehicles to use
    int vehicles;


    float fitness = -1;

    public Pop(int[] genotype,int vehicles){
        this.genotype = genotype;
        this.vehicles = vehicles;
    }

    // SJEKKER IKKE OM FOR MANGE BILER FRA SAMME DEPOT
    // BEREGN ALLE EUCLIDEAN DISTANCES PÅ FORHÅND I BEAN
    public float calculateFitness(Bean bean){
        fitness = 0;
        int[] od = new int[vehicles]; // origin depot of each vehicle
        int[] vd = new int[vehicles]; //demand per vehicle
        for(int i = 0;i<vehicles;i++){
            // move each vehicle to their first customer
            od[i] = bean.nearestDepot[i];
            fitness += Util.edist(bean.customer_x[i],bean.customer_y[i],bean.depot_x[od[i]],bean.depot_y[od[i]]);
            vd[i] = bean.service_demand[i];
            //if(bean.service_demand[i]>bean.vehicle_load[od[i]]){
            //    fitness = -1;
            //    return -1;
            //}
        }

        int[] lc = new int[vehicles]; //last customer
        int cv = -1; //current vehicle
        for(int i = vehicles;i<genotype.length;i++){
            // move each vehicle to their next customer until capacity reached
            int j;
            for(j = 0; j<vehicles;j++){
                cv++;
                if(cv==vehicles){
                    cv = 0;
                }
                if(vd[cv] > bean.vehicle_load[od[cv]]){
                    continue; //current vehicle up to capacity
                }
                vd[cv] += bean.service_demand[i];
                if(vd[cv] > bean.vehicle_load[od[cv]]){
                    fitness += Util.edist(bean.customer_x[lc[cv]],bean.customer_y[lc[cv]],bean.depot_x[bean.nearestDepot[lc[cv]]],bean.depot_y[bean.nearestDepot[lc[cv]]]);
                    continue; //current vehicle up to capacity and returning to depot
                }
                fitness += Util.edist(bean.customer_x[lc[cv]],bean.customer_y[lc[cv]],bean.customer_x[i],bean.customer_y[i]);
                break; //customer successfully served
            }
            if(j == vehicles) {
                fitness = -1;
                return -1;
            }
        }
        return fitness;
    }

    // SJEKKER IKKE OM FOR MANGE BILER FRA SAMME DEPOT
    // BEREGN ALLE EUCLIDEAN DISTANCES PÅ FORHÅND I BEAN
//    public float calculateFitnessV1(Bean bean){
//        int demand = 0;
//        fitness = 0;
//        int lc = -1; // last customer
//        int c; //current customer id
//        int nd; // nearest depot
//        int origin_depot = -1;
//        for(int i = 0; i<genotype.length;i++){
//            c = genotype[i];
//            demand += bean.service_demand[c];
//            if(origin_depot != -1 && demand > bean.vehicle_load[origin_depot]){
//                //next customer has too high demand - return to nearest depot
//                nd = bean.nearestDepot[lc];
//                fitness += Util.edist(bean.customer_x[lc],bean.customer_y[lc],bean.depot_x[nd],bean.depot_y[nd]);
//                lc = -1;
//                demand = bean.service_demand[c];
//            }
//            if(lc==-1) {
//                //first customer for a vehicle coming straight from a depot
//                nd = bean.nearestDepot[c];
//                origin_depot = nd;
//                fitness += Util.edist(bean.customer_x[c],bean.customer_y[c],bean.depot_x[nd],bean.depot_y[nd]);
//            }else{
//                //drive between customers
//                fitness += Util.edist(bean.customer_x[c],bean.customer_y[c],bean.customer_x[lc],bean.customer_y[lc]);
//            }
//            lc = c;
//        }
//        //handle last return
//        nd = bean.nearestDepot[lc];
//        fitness += Util.edist(bean.customer_x[lc],bean.customer_y[lc],bean.depot_x[nd],bean.depot_y[nd]);
//        return fitness;
//    }

    public String toString() {
        return("Fitness: "+fitness+"  Vehicles: "+vehicles+"  Genotype: " + Arrays.toString(genotype)+"\n");
    }
}
