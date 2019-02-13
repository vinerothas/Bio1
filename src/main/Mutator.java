package main;

import java.util.Random;

public class Mutator {

    //move pop from one route r1 to another r2 if lower cost and within capacity
    public static void MutateM(Pop pop, Random r, Bean bean, boolean cap){
        int r1 = r.nextInt(pop.customerOrder.length);
        int r2 = r.nextInt(pop.customerOrder.length);
        while(pop.customerOrder[r1].length==0 || r1==r2){
            r1 = r.nextInt(pop.customerOrder.length);
        }

        int[] route = pop.customerOrder[r2];
        int routeDemand = routeDemand(bean,route);
        int c1 = r.nextInt(pop.customerOrder[r1].length);
        int customer = pop.customerOrder[r1][c1];
        int depot = bean.depotOfRoute[r2];
        int capacity = bean.vehicle_load[depot];
        int fc = c1; //first customer that was checked

        while(bean.service_demand[customer]+routeDemand>capacity && cap){
            c1++;
            if(c1==pop.customerOrder[r1].length){
                c1 = 0;
            }
            if(fc==c1){
                MutateM(pop,r,bean, false);
                return;
            }
            customer = pop.customerOrder[r1][c1];
        }

        double routeCost = routeCost(bean, depot, route);
        int insertionIndex = r.nextInt(pop.customerOrder[r2].length+1);

        if(cap) {
            for (int i = insertionIndex; i < route.length; i++) {
                if (insertionCost(customer, i, depot, route, bean) + routeCost < bean.vehicle_duration[depot]) {
                    pop.customerOrder[r2] = Crosser.insertIntoRoute(route, i, customer);
                    pop.customerOrder[r1] = Util.removeFromArray(pop.customerOrder[r1], c1);
                    return;
                }
            }
            for (int i = 0; i < insertionIndex; i++) {
                if (insertionCost(customer, i, depot, route, bean) + routeCost < bean.vehicle_duration[depot]) {
                    pop.customerOrder[r2] = Crosser.insertIntoRoute(route, i, customer);
                    pop.customerOrder[r1] = Util.removeFromArray(pop.customerOrder[r1], c1);
                    return;
                }
            }
        }else{
            pop.customerOrder[r2] = Crosser.insertIntoRoute(route, insertionIndex, customer);
            pop.customerOrder[r1] = Util.removeFromArray(pop.customerOrder[r1], c1);
            return;
        }
        MutateM(pop,r,bean,false);
    }

    public static void MutateS(Pop pop, Random r, Bean bean){
        int ri = r.nextInt(pop.customerOrder.length);
        while(pop.customerOrder[ri].length<2){
            ri = r.nextInt(pop.customerOrder.length);
        }
        int i1 = r.nextInt(pop.customerOrder[ri].length);
        int i2 = r.nextInt(pop.customerOrder[ri].length);
        while(i1==i2){
            i2 = r.nextInt(pop.customerOrder[ri].length);
        }

        int c1 = pop.customerOrder[ri][i1];
        int c2 = pop.customerOrder[ri][i2];

        double routeCost = routeCost(bean, bean.depotOfRoute[ri], pop.customerOrder[ri]);
        pop.customerOrder[ri][i2] = c1;
        pop.customerOrder[ri][i1] = c2;
        double newRouteCost = routeCost(bean, bean.depotOfRoute[ri], pop.customerOrder[ri]);
        if(newRouteCost>=routeCost){ //revert to better route
            pop.customerOrder[ri][i1] = c1;
            pop.customerOrder[ri][i2] = c2;
        }
    }

    public static void MutateI(Pop pop, Random r, Bean bean){
        int ri = r.nextInt(pop.customerOrder.length);
        while(pop.customerOrder[ri].length<2){
            ri = r.nextInt(pop.customerOrder.length);
        }

        double routeCost = routeCost(bean, bean.depotOfRoute[ri], pop.customerOrder[ri]);

        int[] newRoute = new int[pop.customerOrder[ri].length];

        for(int i = 0; i < pop.customerOrder[ri].length; i++) {
            newRoute[newRoute.length-1-i] = pop.customerOrder[ri][i];
        }

        if(routeCost(bean, bean.depotOfRoute[ri],newRoute)<routeCost){
            pop.customerOrder[ri] = newRoute;
        }
    }


    private static double insertionCost(int customer, int index, int depot, int[] route, Bean bean){
        if(index==0) {
            int c1 = route[0];
            return bean.depotCustomerDist[depot][customer] + bean.customerDist[customer][c1] - bean.depotCustomerDist[depot][c1];
        }else if(index==route.length){
            int lastIndex = route.length-1;
            int c1 = route[lastIndex];
            int endDepotNew = bean.nearestDepot[customer];
            int endDepotOld = bean.nearestDepot[c1];
            return bean.depotCustomerDist[endDepotNew][customer]+bean.customerDist[customer][c1]-bean.depotCustomerDist[endDepotOld][c1];
        }else{
            return bean.customerDist[customer][route[index-1]]+bean.customerDist[customer][route[index]]-bean.customerDist[route[index]][route[index-1]];
        }
    }


    private static double routeCost(Bean bean, int depot, int[] route){
        if(route.length==0)return 0;
        double length = 0;

        length+= bean.depotCustomerDist[depot][route[0]];

        for (int i = 1; i < route.length; i++) {
            length+= bean.customerDist[route[i-1]][route[i]];
        }

        length += bean.depotCustomerDist[bean.nearestDepot[route[route.length-1]]][route[route.length-1]];

        return length;
    }

    private static int routeDemand(Bean bean, int[] route){
        int demand = 0;


        for (int i = 0; i < route.length; i++) {
            demand+= bean.service_demand[route[i]];
        }


        return demand;
    }

}
