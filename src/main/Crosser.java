package main;

import java.util.Random;

public class Crosser {


    /*
    1. Randomly select p1, p2, ∈ P.
2. Randomly select a depot Φ ∈ Vd to undergo reproduction
3. Randomly select a route from each parent r1 ∈ p1Φ , r2 ∈ p2Φ
4. a) Remove all customers c ∈ r1 from p2
b) Remove all customers c ∈ r2 from p1
5. For each c ∈ r1
• Compute the insertion cost of c ∈ r1 into each location in p2Φ and store these
in an ordered list. Also for each insertion location, store in this list whether the
insertion maintained feasibility or infeasibility. (If insertion broke an existing
route into two routes, this would be infeasible.) location in p2Φ .
• Generate a random number k ∈ (0, 1).
• If k ≤ 0.8, choose the first feasible insertion location. (If there exists no feasible
insertion locations, create new route with cl as the only customer.)
Else if k > 0.8, choose the first entry in the list, regardless of feasibility.
6. Repeat for r2 and p1Φ taking place of r1 and p2Φ respectively in the above loop
(from step 5)
     */
    public static Pop cross(Pop p1, Pop p2, Bean bean, Random r){
        Pop offspring = new Pop(p2);
        int depot = r.nextInt(bean.depots);
        int rp0 = r.nextInt(bean.vehiclesPerDepot)+bean.firstVehicleInDepot[depot];
        int[] r0 = p1.customerOrder[rp0];

        for (int i = 0; i < r0.length ; i++) {
            removeCustomer(r0[i], offspring.customerOrder);
        }

        // r0 into offspring
        for (int i = 0; i < r0.length; i++) {
            int bestIndex = -1;
            int bestIndex2 = -1;
            double bestCost = Double.MAX_VALUE;
            int bestInfeasibleIndex = -1;
            int bestInfeasibleIndex2 = -1;
            double bestInfeasibleCost = Double.MAX_VALUE;
            int newRouteIndex = -1;

            for (int j = 0; j < bean.vehiclesPerDepot; j++) {
                int index = bean.firstVehicleInDepot[depot]+j;
                if(offspring.customerOrder[index].length == 0){
                    newRouteIndex = index;
                    continue; //new route only if no feasible locations
                }

                int c1= offspring.customerOrder[index][0];
                double cost = bean.depotCustomerDist[depot][r0[i]]+bean.customerDist[r0[i]][c1]-bean.depotCustomerDist[depot][c1];
                if(cost<bestCost || cost<bestInfeasibleCost){
                    boolean feasible = validRoute(bean,depot,offspring.customerOrder[index],0,r0[i]);
                    if(feasible && cost<bestCost){
                      bestCost = cost;
                      bestIndex = index;
                      bestIndex2 = 0;
                    }else if(!feasible && cost<bestInfeasibleCost){
                        bestInfeasibleCost = cost;
                        bestInfeasibleIndex = index;
                        bestInfeasibleIndex2 = 0;
                    }
                }

                for (int k = 1; k < offspring.customerOrder[index].length; k++) {
                    c1 = offspring.customerOrder[index][k-1];
                    int c2 = offspring.customerOrder[index][k];
                    cost = bean.customerDist[r0[i]][c1]+bean.customerDist[r0[i]][c2]-bean.customerDist[c1][c2];
                    if(cost<bestCost || cost<bestInfeasibleCost){
                        boolean feasible = validRoute(bean,depot,offspring.customerOrder[index],k,r0[i]);
                        if(feasible && cost<bestCost){
                            bestCost = cost;
                            bestIndex = index;
                            bestIndex2 = k;
                        }else if(!feasible && cost<bestInfeasibleCost){
                            bestInfeasibleCost = cost;
                            bestInfeasibleIndex = index;
                            bestInfeasibleIndex2 = k;
                        }
                    }
                }

                int lastIndex = offspring.customerOrder[index].length-1;
                c1 = offspring.customerOrder[index][lastIndex];
                int endDepotNew = bean.nearestDepot[r0[i]];
                int endDepotOld = bean.nearestDepot[c1];
                cost = bean.depotCustomerDist[endDepotNew][r0[i]]+bean.customerDist[r0[i]][c1]-bean.depotCustomerDist[endDepotOld][c1];
                if(cost<bestCost || cost<bestInfeasibleCost){
                    boolean feasible = validRoute(bean,depot,offspring.customerOrder[index],lastIndex,r0[i]);
                    if(feasible && cost<bestCost){
                        bestCost = cost;
                        bestIndex = index;
                        bestIndex2 = lastIndex;
                    }else if(!feasible && cost<bestInfeasibleCost){
                        bestInfeasibleCost = cost;
                        bestInfeasibleIndex = index;
                        bestInfeasibleIndex2 = lastIndex;
                    }
                }
            }

            float k = r.nextFloat();
            if(k<0.8){
                if(bestIndex !=-1){//best feasible location
                    offspring.customerOrder[bestIndex] = insertIntoRoute(offspring.customerOrder[bestIndex], bestIndex2, r0[i]);
                }else if(newRouteIndex!=-1){//or new route if all infeasible
                    offspring.customerOrder[newRouteIndex] = new int[]{r0[i]};
                }else{//or infeasible if no new route
                    offspring.customerOrder[bestInfeasibleIndex] = insertIntoRoute(offspring.customerOrder[bestInfeasibleIndex], bestInfeasibleIndex2, r0[i]);
                }
            }else{
                if(bestCost < bestInfeasibleCost){
                    offspring.customerOrder[bestIndex] = insertIntoRoute(offspring.customerOrder[bestIndex], bestIndex2, r0[i]);
                }else if(bestInfeasibleIndex!=-1){
                    offspring.customerOrder[bestInfeasibleIndex] = insertIntoRoute(offspring.customerOrder[bestInfeasibleIndex], bestInfeasibleIndex2, r0[i]);
                }else{
                    offspring.customerOrder[newRouteIndex] = new int[]{r0[i]};
                }
            }
        }

        return offspring;
    }

    static void removeCustomer(int customer, int[][] customerOrder){
        for (int j = 0; j < customerOrder.length; j++) {
            for (int k = 0; k < customerOrder[j].length; k++) {
                if(customerOrder[j][k]==customer){
                    customerOrder[j] = Util.removeFromArray(customerOrder[j],k);
                    return;
                }
            }
        }
    }

    static int[] insertIntoRoute(int[] oldRoute, int index, int customer){
        int[] newRoute = new int[oldRoute.length+1];
        for (int i = 0; i < index; i++) {
            newRoute[i] = oldRoute[i];
        }
        newRoute[index] = customer;
        for (int i = index; i < oldRoute.length; i++) {
            newRoute[i+1] = oldRoute[i];
        }
        return newRoute;
    }

    private static boolean validRoute(Bean bean, int depot, int[] route, int insertIndex, int customer){
        int demand = 0;
        double length = 0;

        if(insertIndex==0){
            demand+= bean.service_demand[customer];
            length+= bean.depotCustomerDist[depot][customer];

            demand+= bean.service_demand[route[0]];
            length+= bean.customerDist[customer][route[0]];

            for (int i = 1; i < route.length; i++) {
                demand+= bean.service_demand[route[i]];
                length+= bean.customerDist[route[i-1]][route[i]];
            }

            length += bean.depotCustomerDist[bean.nearestDepot[route[route.length-1]]][route[route.length-1]];

        }else if(insertIndex==route.length){

            demand+= bean.service_demand[route[0]];
            length+= bean.customerDist[customer][route[0]];

            for (int i = 1; i < route.length; i++) {
                demand+= bean.service_demand[route[i]];
                length+= bean.customerDist[route[i-1]][route[i]];
            }

            demand+= bean.service_demand[customer];
            length += bean.customerDist[route[route.length-1]][customer];

            length+= bean.depotCustomerDist[bean.nearestDepot[customer]][customer];
        }else{
            demand+= bean.service_demand[route[0]];
            length+= bean.customerDist[customer][route[0]];

            for (int i = 1; i < insertIndex; i++) {
                demand+= bean.service_demand[route[i]];
                length+= bean.customerDist[route[i-1]][route[i]];
            }

            demand += bean.service_demand[customer];
            length += bean.customerDist[route[insertIndex-1]][customer];

            demand += bean.service_demand[route[insertIndex]];
            length += bean.customerDist[route[insertIndex]][customer];

            for (int i = insertIndex+1; i < route.length; i++) {
                demand+= bean.service_demand[route[i]];
                length+= bean.customerDist[route[i-1]][route[i]];
            }

            length += bean.depotCustomerDist[bean.nearestDepot[route[route.length-1]]][route[route.length-1]];

        }

        if(length>bean.vehicle_duration[depot] || demand>bean.vehicle_load[depot]){
            return false;
        }
        return true;
    }

}
