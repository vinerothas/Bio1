import java.util.Arrays;

public class Bean {

    public int vehicles; //vehicles per depot
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
    public int minVehicles; // minimum number of vehicles to satisfy the demand
    public float[][] customerDist; // distance between each customer
    public float[][] depotCustomerDist; // distance between every depot-customer combination

    public void calculateNearestDepot() {
        nearestDepot = new int[customers];
        for (int i = 0; i < customers; i++) {
            int nearest = -1;
            float lowestDistance = Float.MAX_VALUE;
            for(int j = 0; j<depots;j++){
                //Euclidean distance
                float dist = Util.edist(depot_x[j],depot_y[j],customer_x[i],customer_y[i]);
                if (dist < lowestDistance){
                    lowestDistance = dist;
                    nearest = j;
                }
            }
            nearestDepot[i] = nearest;
        }
    }

    public void calculateDist(){
        customerDist = new float[customers][customers];
        for(int i = 0; i<customers;i++){
            for(int j = 0; j<customers;j++){
                customerDist[i][j] = Util.edist(customer_x[i],customer_y[i],customer_x[j],customer_y[j]);
            }
        }

        depotCustomerDist  = new float[depots][customers];
        for(int i = 0; i<depots;i++){
            for(int j = 0; j<customers;j++){
                depotCustomerDist[i][j] = Util.edist(depot_x[i],depot_y[i],customer_x[j],customer_y[j]);
            }
        }
    }

    public void printBean() {
        System.out.println(vehicles);
        System.out.println(customers);
        System.out.println(depots);
        printArray(vehicle_duration);
        printArray(vehicle_load);
        printArray(customer_x);
        printArray(customer_y);
        printArray(service_duration);
        printArray(service_demand);
        printArray(depot_x);
        printArray(depot_y);
        printArray(nearestDepot);
        System.out.println("Customer to customer distances:");
        for(int i = 0; i< customers;i++){
            System.out.println(Arrays.toString(customerDist[i]));
        }
        System.out.println("Depot to customer distances:");
        for(int i = 0; i< depots;i++){
            System.out.println(Arrays.toString(depotCustomerDist[i]));
        }
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
