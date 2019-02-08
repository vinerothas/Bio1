import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Pop {

    //order of customers for each vehicle
    int[][] customerOrder;
    //starting depot of each vehicle
    int[] startingDepots;
    // length of startingDepots
    int vehicles;


    float fitness = Float.MAX_VALUE;

    // Initialize a random pop
    public Pop(Random r, Bean bean) {
        int bound = bean.totalVehicles-bean.minVehicles+1;
        vehicles = r.nextInt(bound)+bean.minVehicles ;

        //generate a random order of customers
        customerOrder = new int[vehicles][];
        int[] customers = IntStream.range(0, bean.customers).toArray();
        Util.shuffleArray(customers);

        int cvl = bean.customers/vehicles; //customers per vehicle, lower bound
        int cvh = cvl+1; //customers per vehicle, higher bound
        int vh = bean.customers-cvl*vehicles; //number of higher bound vehicles
        int vl = vehicles-vh; //number of lower bound vehicles
        int cc = 0; //current customer to assign
        for(int i = 0; i<vehicles-1;i++){ //assign customers to all vehicles except the last
            customerOrder[i] = new int[cvl];
            for(int j = 0; j < cvl; j++){
                customerOrder[i][j] = customers[cc++];
            }
        }

        int lv = vehicles-1; //id of last vehicle
        int lvc = bean.customers-cc; //customers assigned to the last vehicle
        customerOrder[lv] = new int[lvc];
        for(int i = 0;i<lvc;i++){ //assign customers to the last vehicle
            customerOrder[lv][i] = customers[cc++];
        }

        startingDepots = new int[vehicles];
        for(int i = 0;i<vehicles;i++){ //make each vehicle start at the depot nearest to its first customer
            startingDepots[i] = bean.nearestDepot[customerOrder[i][0]];
        }

    }

    // Clone an existing pop
    public Pop(Pop pop) {
        this.vehicles = pop.vehicles;
        customerOrder = new int[vehicles][];
        for(int i=0; i<pop.customerOrder.length; i++) {
            customerOrder[i] = new int[pop.customerOrder[i].length];
            for (int j = 0; j < pop.customerOrder[i].length; j++) {
                customerOrder[i][j] = pop.customerOrder[i][j];
            }
        }
        this.startingDepots = pop.startingDepots.clone();
    }

    // SJEKKER IKKE OM FOR MANGE BILER FRA SAMME DEPOT
    public void calculateFitness(Bean bean) {
        fitness = 0;
        int demand = 0;
        for(int i = 0; i<vehicles;i++){
            //handle the first customer
            demand += bean.service_demand[customerOrder[i][0]];
            fitness += bean.depotCustomerDist[startingDepots[i]][customerOrder[i][0]];
            for(int j = 1;j<customerOrder[i].length;j++){ //handle the rest of the customers for the current vehicle
                demand += bean.service_demand[customerOrder[i][j]];
                fitness += bean.customerDist[customerOrder[i][j-1]][customerOrder[i][j]];
            }

            if(demand>bean.vehicle_load[startingDepots[i]]){ //invalid solution if demand exceeds capacity
                fitness = Float.MAX_VALUE;
                return;
            }

            int lc = customerOrder[i].length-1; //last customer
            fitness += bean.depotCustomerDist[bean.nearestDepot[lc]][customerOrder[i][lc]]; //return to nearest depot
            demand = 0;
        }

    }

    public String toString() {
        String s = "Fitness: " + fitness + "  Vehicles: " + vehicles + "  Genotype:\n";
        for (int[] ints : customerOrder) {
            s += Arrays.toString(ints)+"\n";
        }
        return s;
    }
}
