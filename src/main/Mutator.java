package main;

import java.util.Random;

public class Mutator {

    // swamp two customers
    public static void MutateS(Pop pop, Random r){
        int i = r.nextInt(pop.customerOrder.length);
        int j = r.nextInt(pop.customerOrder.length);
        while(i==j){
            j = r.nextInt(pop.customerOrder.length);
        }
        int first = pop.customerOrder[i];
        pop.customerOrder[i] = pop.customerOrder[j];
        pop.customerOrder[j] = first;
    }

    // randomize starting depot
    public static void MutateD(Pop pop, Random r, Bean bean){
        int i = r.nextInt(pop.startDepot.length);
        pop.startDepot[i] = r.nextInt(bean.depots);
    }

    //move the starting position of a vehicle
    public static void MutateM(Pop pop, Random r){
        boolean forward = r.nextBoolean();
        int v = r.nextInt(pop.vehicles.length-1)+1;
        if(forward) {
            while (pop.vehicles[v]==pop.customerOrder.length-1 || (v!=pop.vehicles.length-1 && pop.vehicles[v] + 1 == pop.vehicles[v+1])){
                v  = r.nextInt(pop.vehicles.length-1)+1;
            }
            pop.vehicles[v]++;
        }else{
            while (pop.vehicles[v] - 1 == pop.vehicles[v-1]){
                v  = r.nextInt(pop.vehicles.length-1)+1;
            }
            pop.vehicles[v]--;
        }
    }

    //change the number of vehicles
    public static void MutateC(Pop pop, Random r, Bean bean){
        boolean add = r.nextBoolean();
        if(add && pop.vehicles.length == bean.totalVehicles){
            add = false;
        }else if(!add && pop.vehicles.length == bean.minVehicles){
            add = true;
        }

        if(add){
            //pick an index, if route of vehicle i >= 2, split in two by adding a car in the middle
            // v0 = 0, v1=2, v2=5
            // v = 0 -> v0 = 0, v1=1, v2=2, v3=5
            // v = 1 -> v0 = 0, v1=2, v2=3, v3=5
            int v = r.nextInt(pop.vehicles.length);
            while(true){
                if(v==pop.vehicles.length-1){
                    if(pop.customerOrder.length-pop.vehicles[v]>=2){
                        break;
                    }else{
                        v = r.nextInt(pop.vehicles.length - 1);
                        continue;
                    }
                }
                if(pop.vehicles[v+1]-pop.vehicles[v]>1){
                    break;
                }else{
                    v = r.nextInt(pop.vehicles.length);
                }
            }
            int[] vehicles = new int[pop.vehicles.length+1];
            int[] startDepot = new int[pop.vehicles.length+1];
            for (int i = 0; i < v+1; i++) {
                vehicles[i] = pop.vehicles[i];
                startDepot[i] = pop.startDepot[i];
            }
            if(v!=pop.vehicles.length-1) {
                vehicles[v+1] = (pop.vehicles[v+1] - pop.vehicles[v])/2 + pop.vehicles[v];
            }else{
                vehicles[v+1] = (pop.customerOrder.length - pop.vehicles[v])/2 + pop.vehicles[v];
            }
            startDepot[v+1] = r.nextInt(bean.depots);
            for (int i = v+1; i < pop.vehicles.length; i++) {
                vehicles[i+1] = pop.vehicles[i];
                startDepot[i] = pop.startDepot[i];
            }
            pop.vehicles = vehicles;
            pop.startDepot = startDepot;
        }else{
            //pick an index not including 0
            // v0 = 0, v1=2, v2=5
            // v = 1 -> v0=0, v1=5
            int v = r.nextInt(pop.vehicles.length-1)+1;
            int[] vehicles = new int[pop.vehicles.length-1];
            int[] startDepot = new int[pop.vehicles.length-1];
            for (int i = 0; i < v; i++) {
                vehicles[i] = pop.vehicles[i];
                startDepot[i] = pop.startDepot[i];
            }
            for (int i = v+1; i < pop.vehicles.length; i++) {
                vehicles[i-1] = pop.vehicles[i];
                startDepot[i-1] = pop.startDepot[i];
            }
            pop.vehicles = vehicles;
            pop.startDepot = startDepot;
        }
    }

}
