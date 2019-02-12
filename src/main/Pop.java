package main;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Pop implements Comparable{

    int[] customerOrder;//order of customers
    int[] vehicles; // order starting position (index) for each vehicle, constraint [i]<[i-1] and [i]=0
    int[] startDepot;
    boolean valid;


    double fitness = Float.MAX_VALUE;

    // Initialize a random pop
    public Pop(Random r, Bean bean) {
        int bound = bean.totalVehicles-bean.minVehicles+1;
        vehicles = new int[r.nextInt(bound)+bean.minVehicles];

        //generate a random order of customers
        customerOrder = IntStream.range(0, bean.customers).toArray();
        Util.shuffleArray(customerOrder);

        int cvl = bean.customers/vehicles.length; //customers per vehicle, lower bound
        int cvh = cvl+1; //customers per vehicle, higher bound
        int vh = bean.customers-cvl*vehicles.length; //number of higher bound vehicles
        for(int i = 0; i<vh;i++){ //assign customers to all higher bound vehicles
            vehicles[i] = cvh*i;
        }
        for(int i = vh; i<vehicles.length;i++){ //assign customers to all lower bound vehicles
            vehicles[i] = cvl*(i-vh)+(cvh*vh);
        }

        startDepot = new int[vehicles.length];
        for (int i = 0; i < startDepot.length; i++) {
            startDepot[i] = r.nextInt(bean.depots);
        }

    }

    // Clone an existing pop
    public Pop(Pop pop) {
        this.customerOrder = new int[pop.customerOrder.length];
        this.vehicles = new int[pop.vehicles.length];
        this.startDepot = new int[pop.startDepot.length];
        System.arraycopy(pop.customerOrder,0, this.customerOrder,0,customerOrder.length);
        System.arraycopy(pop.vehicles,0, this.vehicles,0,vehicles.length);
        System.arraycopy(pop.startDepot,0, this.startDepot,0,startDepot.length);
    }

    // Designer baby
    public Pop(int[] customerOrder, int[] vehicles, int[] startDepot) {
        this.customerOrder = new int[customerOrder.length];
        this.vehicles = new int[vehicles.length];
        this.startDepot = new int[startDepot.length];
        System.arraycopy(customerOrder,0, this.customerOrder,0,customerOrder.length);
        System.arraycopy(vehicles,0, this.vehicles,0,vehicles.length);
        System.arraycopy(startDepot,0, this.startDepot,0,startDepot.length);
    }

    public void calculateFitness(Bean bean) {
        fitness = 0;
        int demand = 0;
        valid = true;
        int[] startDepot = new int[bean.depots];
        int cc;
        int ci;
        for(int i = 0; i<vehicles.length-1;i++){
            ci = vehicles[i]; //current index in customer order
            cc = customerOrder[ci]; //current customer
            int sd = this.startDepot[i]; //starting depot
            startDepot[sd] += 1;
            demand += bean.service_demand[cc]; //handle the first customer
            fitness += bean.depotCustomerDist[sd][cc];

            for(int j = ci+1;j<vehicles[i+1];j++){ //handle the rest of the customers for the current vehicle
                demand += bean.service_demand[customerOrder[j]];
                fitness += bean.customerDist[customerOrder[j-1]][customerOrder[j]];
            }

            if(demand>bean.vehicle_load[sd]){ //invalid solution if demand exceeds capacity
                valid = false;
                fitness += (demand-bean.vehicle_load[sd])<< Param.capacityPenalty;
            }

            int lci = vehicles[i+1]-1; //last customer order index
            int lc = customerOrder[lci]; //last customer
            fitness += bean.depotCustomerDist[bean.nearestDepot[lc]][lc]; //return to nearest depot
            demand = 0;
        }

        //last vehicle
        ci = vehicles[vehicles.length-1]; //current index in customer order
        cc = customerOrder[ci]; //current customer
        int sd = this.startDepot[this.startDepot.length-1]; //starting depot
        startDepot[sd] += 1;
        demand += bean.service_demand[cc]; //handle the first customer
        fitness += bean.depotCustomerDist[sd][cc];

        for(int j = ci+1;j<customerOrder.length;j++){ //handle the rest of the customers for the last vehicle
            demand += bean.service_demand[customerOrder[j]];
            fitness += bean.customerDist[customerOrder[j-1]][customerOrder[j]];
        }

        if(demand>bean.vehicle_load[sd]){ //invalid solution if demand exceeds capacity
            valid = false;
            fitness += (demand-bean.vehicle_load[sd])<< Param.capacityPenalty;
        }

        int lc = customerOrder[customerOrder.length-1]; //last customer
        fitness += bean.depotCustomerDist[bean.nearestDepot[lc]][lc]; //return to nearest depot

        for (int i = 0; i < startDepot.length ; i++) {
            if(startDepot[i]>bean.vehicles){
                valid = false;
                fitness+=(startDepot[i]-bean.vehicles)<< Param.depotPenalty;
            }
        }


    }

    //sets routeDuration and vehicleLoad for a solution
    public void calculateRouteValues(Bean bean, Solution solution) {
        double[] distances = new double[vehicles.length];
        int[] demand = new int[vehicles.length];
        solution.vehicleLoad = new int[vehicles.length];

        int cc;
        int ci;
        for(int i = 0; i<vehicles.length-1;i++){
            ci = vehicles[i]; //current index in customer order
            cc = customerOrder[ci]; //current customer
            int sd = startDepot[i]; //starting depot
            demand[i] += bean.service_demand[cc]; //handle the first customer
            distances[i] = bean.depotCustomerDist[sd][cc];

            for(int j = ci+1;j<vehicles[i+1];j++){ //handle the rest of the customers for the current vehicle
                demand[i] += bean.service_demand[customerOrder[j]];
                distances[i] += bean.customerDist[customerOrder[j-1]][customerOrder[j]];
            }

            int lci = vehicles[i+1]-1; //last customer order index
            int lc = customerOrder[lci]; //last customer
            distances[i] += bean.depotCustomerDist[bean.nearestDepot[lc]][lc]; //return to nearest depot
            solution.vehicleLoad[i] = demand[i];

        }

        //last vehicle
        ci = vehicles[vehicles.length-1]; //current index in customer order
        cc = customerOrder[ci]; //current customer
        int sd = startDepot[startDepot.length-1]; //starting depot
        solution.vehicleLoad[solution.vehicleLoad.length-1] = bean.service_demand[cc]; //handle the first customer
        distances[distances.length-1] = bean.depotCustomerDist[sd][cc];

        for(int j = ci+1;j<customerOrder.length;j++){ //handle the rest of the customers for the last vehicle
            solution.vehicleLoad[solution.vehicleLoad.length-1] += bean.service_demand[customerOrder[j]];
            distances[distances.length-1] += bean.customerDist[customerOrder[j-1]][customerOrder[j]];
        }

        int lc = customerOrder[customerOrder.length-1]; //last customer
        distances[distances.length-1] += bean.depotCustomerDist[bean.nearestDepot[lc]][lc]; //return to nearest depot


        solution.routeDuration = distances;

    }

    public String toString() {
        String s = "Fitness: " + fitness + "  Vehicles: " + vehicles.length +"   Valid: "+valid+ "  Customer order:\n";
        s += Arrays.toString(customerOrder)+"\n";
        s += "Vehicles:\n"+ Arrays.toString(vehicles)+"\n";

        return s;
    }

    //p01 best pop
    public Pop(boolean check) {
        startDepot = new int[]{0,0,0,1,1,1,1,2,2,3,3};
        vehicles = new int[]{0,5,8,11,15,20,26,30,34,41,46};
        customerOrder = new int[]{40, 39, 18, 41, 43,12, 24, 13,3, 17, 46,10, 31, 0, 26,22, 6, 42, 23, 5,47, 7, 25, 30, 27, 21,45, 11, 4, 37,48, 29, 33, 8,9, 38, 32, 44, 14, 36, 16,28, 1, 15, 49, 20,19, 2, 35, 34,};
    }


    @Override
    public int compareTo(Object o) {
        return Double.compare(this.fitness,((Pop)(o)).fitness);
    }
}
