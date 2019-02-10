package main;

import java.util.ArrayList;
import java.util.Random;

public class Crosser {

    public static Pop cross(Pop mom, Pop dad, Bean bean, Random r){
        boolean[] served = new boolean[bean.customers];
        ArrayList<int[]> list = new ArrayList<int[]>();

        Pop a;
        if(mom.vehicles>dad.vehicles){//dad's bigger
            a = mom;
            mom = dad;
            dad = a;
        }

        // add routes present in both parents, not necessarily an optimal property
//        for (int i = 0; i < mom.vehicles; i++) {
//            for (int j = 0; j < dad.vehicles; j++) {
//                if(Arrays.equals(mom.customerOrder[i],dad.customerOrder[j])){
//                    list.add(mom.customerOrder[i]);
//                    for (int k = 0; k < mom.customerOrder[i].length; k++) {
//                        served[mom.customerOrder[i][k]] = true;
//                    }
//                    break;
//                }
//            }
//        }

        boolean momServe = true;
        boolean dadServe = true;
        int limit = mom.vehicles;
        for (int i = 0; i < limit ; i++) { //take routes for non served customers alternating between mom and dad
            for (int j = 0; j < mom.customerOrder[i].length; j++) {
                if(served[mom.customerOrder[i][j]]){
                    momServe = false;
                    break;
                }
            }
            if(momServe){
                list.add(mom.customerOrder[i]);
                for (int j = 0; j < mom.customerOrder[i].length; j++) {
                    served[mom.customerOrder[i][j]] = true;
                }
                a = mom;
                mom = dad;
                dad = a;
                continue;
            }
            momServe = true;
            for (int j = 0; j < dad.customerOrder[i].length; j++) {
                if(served[dad.customerOrder[i][j]]){
                    dadServe = false;
                    break;
                }
            }
            if(dadServe){
                list.add(dad.customerOrder[i]);
                for (int j = 0; j < dad.customerOrder[i].length; j++) {
                    served[dad.customerOrder[i][j]] = true;
                }
                a = mom;
                mom = dad;
                dad = a;
                continue;
            }
            dadServe = true;
        }

        if(mom.vehicles>dad.vehicles){
            a = mom;
            mom = dad;
            dad = a;
        }


        for (int i = mom.vehicles; i < dad.vehicles ; i++) {
            for (int j = 0; j < dad.customerOrder[i].length; j++) {
                if(served[dad.customerOrder[i][j]]){
                    dadServe = false;
                    break;
                }
            }
            if(dadServe){
                list.add(dad.customerOrder[i]);
                for (int j = 0; j < dad.customerOrder[i].length; j++) {
                    served[dad.customerOrder[i][j]] = true;
                }
                a = mom;
                mom = dad;
                dad = a;
                continue;
            }
            dadServe = true;
        }

        int parentVehicles = Math.round((dad.vehicles+mom.vehicles)/(float)2);

        while(list.size()>parentVehicles){
            int i = r.nextInt(list.size());
            int j = r.nextInt(list.size());
            while(i==j){
                j = r.nextInt(list.size());
            }
            int[] a1 = list.get(i);
            int[] a2 = list.remove(j);
            if(i != list.size() && a1==list.get(i)){
                list.remove(i);
            }else{
                list.remove(i-1);
            }

            int[] a3 = new int[a1.length+a2.length];
            int k;
            for (k=0; k < a1.length; k++) {
                a3[k] = a1[k];
            }
            for (int l = 0; l < a2.length ; l++) {
                a3[k+l] = a2[l];
            }
            list.add(a3);
        }


        for (int i = 0; i < served.length; i++) {
            if(!served[i]){
                if(list.size()==parentVehicles){
                    int index = r.nextInt(list.size());
                    int[] a1 = list.remove(index);
                    int[] a2 = new int[a1.length+1];
                    for (int j = 0; j < a1.length; j++) {
                        a2[j] = a1[j];
                    }
                    a2[a1.length] = i;
                    list.add(a2);
                }else{
                    list.add(new int[]{i});
                }
                served[i] = true;
            }
        }

        int[][] customerOrder = new int[list.size()][];
        for (int i = 0; i < customerOrder.length; i++) {
            customerOrder[i] = list.get(i);
        }

        return new Pop(customerOrder);
    }

}
