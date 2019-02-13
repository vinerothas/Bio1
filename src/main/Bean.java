package main;

import java.util.Arrays;

public class Bean {

    public int vehiclesPerDepot; //vehiclesPerDepot per depot
    public int customers;
    public int depots;
    public int totalVehicles;

    public int[] vehicle_duration;
    public int[] vehicle_load;

    public int[] customer_x;
    public int[] customer_y;
    public int[] service_duration;
    public int[] service_demand;

    public int[] depot_x;
    public int[] depot_y;

    public int max_x = -1000000;
    public int max_y = -1000000;
    public int min_x = 1000000;
    public int min_y = 1000000;

    public int[] nearestDepot; //the id of the nearest depot for each customer
    public int totalDemand = 0;
    public int minVehicles; // minimum number of vehiclesPerDepot to satisfy the demand
    public double[][] customerDist; // distance between each customer
    public double[][] depotCustomerDist; // distance between every depot-customer combination
    public int[] firstVehicleInDepot; // index in customer order of the first route for a given depot
    public int[] vehicleDepotBound; //the index after the last vehicle of a given depot
    public int[] depotOfRoute;

    public Bean(){

    }

    public void calculateNearestDepot() {
        nearestDepot = new int[customers];
        for (int i = 0; i < customers; i++) {
            int nearest = -1;
            double lowestDistance = Float.MAX_VALUE;
            for(int j = 0; j<depots;j++){
                //Euclidean distance
                double dist = Util.edist(depot_x[j],depot_y[j],customer_x[i],customer_y[i]);
                if (dist < lowestDistance){
                    lowestDistance = dist;
                    nearest = j;
                }
            }
            nearestDepot[i] = nearest;
        }
    }

    public void calculateDist(){
        customerDist = new double[customers][customers];
        for(int i = 0; i<customers;i++){
            for(int j = 0; j<customers;j++){
                customerDist[i][j] = Util.edist(customer_x[i],customer_y[i],customer_x[j],customer_y[j]);
            }
        }

        depotCustomerDist  = new double[depots][customers];
        for(int i = 0; i<depots;i++){
            for(int j = 0; j<customers;j++){
                depotCustomerDist[i][j] = Util.edist(depot_x[i],depot_y[i],customer_x[j],customer_y[j]);
            }
        }

        firstVehicleInDepot = new int[depots];
        vehicleDepotBound = new int[depots];
        for (int i = 0; i < depots ; i++) {
            firstVehicleInDepot[i] = i*vehiclesPerDepot;
            vehicleDepotBound[i]= (i+1)*vehiclesPerDepot;
        }
    }

    public void printBean() {
        System.out.println("vehiclesPerDepot per depot: "+ vehiclesPerDepot);
        System.out.println("number of customers: "+customers);
        System.out.println("number of depots: "+depots);
        System.out.println("Max vehicle route duration per depot:");
        printArray(vehicle_duration);
        System.out.println("Max vehicle load per depot:");
        printArray(vehicle_load);
        System.out.println("x-coordinates of each customer:");
        printArray(customer_x);
        System.out.println("y-coordinates of each customer:");
        printArray(customer_y);
        System.out.println("service duration of each customer:");
        printArray(service_duration);
        System.out.println("service demand of each customer:");
        printArray(service_demand);
        System.out.println("x-coordinates of each depot:");
        printArray(depot_x);
        System.out.println("y-coordinates of each depot:");
        printArray(depot_y);
        System.out.println("nearest depot for each customer:");
        printArray(nearestDepot);
        System.out.println("Customer to customer distances:");
        for(int i = 0; i< customers;i++){
            System.out.println(Arrays.toString(customerDist[i]));
        }
        System.out.println("Depot to customer distances:");
        for(int i = 0; i< depots;i++){
            System.out.println(Arrays.toString(depotCustomerDist[i]));
        }
        System.out.println("Total demand: "+totalDemand);
        System.out.println("Minimum number of vehiclesPerDepot: "+minVehicles);
    }

    private static void printArray(int[] anArray) {
        for (int i = 0; i < anArray.length; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(anArray[i]);
        }
        System.out.println();
    }
}
