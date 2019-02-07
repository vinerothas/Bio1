public class Bean {

    //vehicles per depot
    public int vehicles;
    public int customers;
    public int depots;

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

    //the id of the nearest depot for each customer
    public int[] nearestDepot;

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
