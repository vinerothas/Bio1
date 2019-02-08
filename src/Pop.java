import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Pop {

    //order of customers for each vehicle
    int[][] customerOrder;
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
        int cc = 0; //current customer to assign
        for(int i = 0; i<vh;i++){ //assign customers to all higher bound vehicles
            customerOrder[i] = new int[cvh];
            for(int j = 0; j < cvh; j++){
                customerOrder[i][j] = customers[cc++];
            }
        }
        for(int i = vh; i<vehicles;i++){ //assign customers to all lower bound vehicles
            customerOrder[i] = new int[cvl];
            for(int j = 0; j < cvl; j++){
                customerOrder[i][j] = customers[cc++];
            }
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
    }

    // SJEKKER IKKE OM FOR MANGE BILER FRA SAMME DEPOT
    public void calculateFitness(Bean bean) {
        fitness = 0;
        int demand = 0;
        for(int i = 0; i<vehicles;i++){

            int sd = bean.nearestDepot[customerOrder[i][0]]; //starting depot
            demand += bean.service_demand[customerOrder[i][0]]; //handle the first customer
            fitness += bean.depotCustomerDist[sd][customerOrder[i][0]];

            for(int j = 1;j<customerOrder[i].length;j++){ //handle the rest of the customers for the current vehicle
                demand += bean.service_demand[customerOrder[i][j]];
                fitness += bean.customerDist[customerOrder[i][j-1]][customerOrder[i][j]];
            }

            if(demand>bean.vehicle_load[sd]){ //invalid solution if demand exceeds capacity
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

    //p01 best pop
    public Pop(boolean check) {
        this.vehicles = 11;
        customerOrder = new int[vehicles][];
        customerOrder[0] = new int[]{40, 39, 18, 41, 43};
        customerOrder[1] = new int[]{12, 24, 13,};
        customerOrder[2] = new int[]{3, 17, 46};
        customerOrder[3] = new int[]{10, 31, 0, 26};
        customerOrder[4] = new int[]{22, 6, 42, 23, 5};
        customerOrder[5] = new int[]{47, 7, 25, 30, 27, 21};
        customerOrder[6] = new int[]{45, 11, 4, 37,};
        customerOrder[7] = new int[]{48, 29, 33, 8,};
        customerOrder[8] = new int[]{9, 38, 32, 44, 14, 36, 16};
        customerOrder[9] = new int[]{28, 1, 15, 49, 20};
        customerOrder[10] = new int[]{19, 2, 35, 34,};
    }
}
