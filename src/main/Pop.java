package main;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Pop {

    int[][] customerOrder; //order of customers for each vehicle,
    // the array contains first all vehiclesPerDepot starting from the first depot (one vehicle per row), then all from the second etc
    private final int capacityPenalty = 11; //(left shift value) fitness penalty for too high demand in a route
    boolean valid;


    double fitness = Float.MAX_VALUE;

    // Initialize a random pop
    public Pop(Random r, Bean bean) {

        //generate a random order of customers
        customerOrder = new int[bean.totalVehicles][];
        int[] customers = IntStream.range(0, bean.customers).toArray();
        Util.shuffleArray(customers);
        int[] duration = new int[bean.totalVehicles];

        int[] assignment = new int[bean.customers]; //which customer to which route
        int[] numberOfAssignments = new int[bean.totalVehicles]; //lengths of rows in customerOrder
        int[] lastAssignment = new int[bean.totalVehicles];
        Arrays.fill(lastAssignment,-1);

        for (int i = 0; i < customers.length; i++) {
            int nd = bean.nearestDepot[customers[i]]; //nearest depot
            int fv = bean.firstVehicleInDepot[nd]; //first index in depot
            int bound = bean.vehicleDepotBound[nd];
            int lowestIndex = -1;
            int lowestDuration = Integer.MAX_VALUE;
            for (int j = fv; j < bound; j++) {
                if(duration[j]<lowestDuration){
                    lowestDuration = duration[j];
                    lowestIndex = j;
                }
            }
            numberOfAssignments[lowestIndex]++;
            assignment[i] = lowestIndex;
            if(lastAssignment[lowestIndex]!=-1) {
                duration[lowestIndex] += bean.customerDist[lastAssignment[lowestIndex]][i];
            }else{
                duration[lowestIndex] += bean.depotCustomerDist[nd][i];
            }
            lastAssignment[lowestIndex] = customers[i];
        }

        for (int i = 0; i < bean.totalVehicles; i++) {
            customerOrder[i] = new int[numberOfAssignments[i]];
        }
        Arrays.fill(lastAssignment, 0);
        for (int i = 0; i < assignment.length; i++) {
            customerOrder[assignment[i]][lastAssignment[assignment[i]]++] = customers[i];
        }


    }

    // Clone an existing pop
    public Pop(Pop pop) {
        customerOrder = new int[pop.customerOrder.length][];
        for(int i=0; i<pop.customerOrder.length; i++) {
            customerOrder[i] = new int[pop.customerOrder[i].length];
            System.arraycopy(pop.customerOrder[i],0,customerOrder[i],0,pop.customerOrder[i].length);
        }
    }

    public void calculateFitness(Bean bean, Solution solution) {
        fitness = 0;
        int demand[] = new int[bean.totalVehicles];
        double routeLength[] = new double[bean.totalVehicles];
        valid = true;
        int numberOfCustomers = 0;
        for(int i = 0; i<bean.depots;i++){
            for (int j = bean.firstVehicleInDepot[i]; j <bean.vehicleDepotBound[i] ; j++) {
                if(customerOrder[j].length==0)continue;
                numberOfCustomers += customerOrder[j].length;
                demand[j] += bean.service_demand[customerOrder[j][0]]; //handle the first customer
                routeLength[j] += bean.depotCustomerDist[i][customerOrder[j][0]];
                for (int k = 1; k < customerOrder[j].length; k++) {//handle the rest of the customers for the current vehicle
                    demand[j] += bean.service_demand[customerOrder[j][k]];
                    routeLength[j] += bean.customerDist[customerOrder[j][k-1]][customerOrder[j][k]];
                }
                int lci = customerOrder[j].length-1; //last customer order index
                int lc = customerOrder[j][lci]; //last customer
                routeLength[j] += bean.depotCustomerDist[bean.nearestDepot[lc]][lc]; //return to nearest depot

                fitness+= routeLength[j];
                if(demand[j]>bean.vehicle_load[i]){ //invalid solution if demand exceeds capacity
                    fitness += (demand[j]-bean.vehicle_load[i])<<Param.capacityPenalty;
                    valid = false;
                }
                if(routeLength[j]>bean.vehicle_duration[i]){
                    fitness += (int)Math.ceil(routeLength[j]-bean.vehicle_duration[i])<<Param.lengthPenalty;
                    valid = false;
                }

            }

        }
        if(solution != null){
            solution.routeDuration = routeLength;
            solution.vehicleLoad = demand;
            int routes = 0;
            for (int i = 0; i < customerOrder.length ; i++) {
                if(customerOrder[i].length != 0){
                    routes++;
                }
            }
            solution.routes = routes;
            if(numberOfCustomers<bean.customers){
                valid = false;
            }
            solution.valid = valid;
        }

    }

    public String toString() {
        String s = "Fitness: " + fitness +"   Valid: "+valid+ "  Genotype:\n";
        for (int[] ints : customerOrder) {
            s += Arrays.toString(ints)+"\n";
        }
        return s;
    }

    //p01 best pop
    public Pop(boolean check) {
        customerOrder = new int[16][];
        customerOrder[0] = new int[]{40, 39, 18, 41, 43};
        customerOrder[1] = new int[]{12, 24, 13,};
        customerOrder[2] = new int[]{3, 17, 46};
        customerOrder[3] = new int[]{};
        customerOrder[4] = new int[]{10, 31, 0, 26};
        customerOrder[5] = new int[]{22, 6, 42, 23, 5};
        customerOrder[6] = new int[]{47, 7, 25, 30, 27, 21};
        customerOrder[7] = new int[]{45, 11, 4, 37,};
        customerOrder[8] = new int[]{48, 29, 33, 8,};
        customerOrder[9] = new int[]{9, 38, 32, 44, 14, 36, 16};
        customerOrder[10] = new int[]{};
        customerOrder[11] = new int[]{};
        customerOrder[12] = new int[]{28, 1, 15, 49, 20};
        customerOrder[13] = new int[]{19, 2, 35, 34,};
        customerOrder[14] = new int[]{};
        customerOrder[15] = new int[]{};
    }

    public boolean rightNumberOfCustomers(Bean bean){
        int number = 0;
        for (int i = 0; i < customerOrder.length ; i++) {
            number+= customerOrder[i].length;
        }
        return number==bean.customers;
    }

}
