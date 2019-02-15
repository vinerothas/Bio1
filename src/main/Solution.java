package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Solution {

    double totalLength;
    int[] startDepot;
    int[] vehicleNumber;
    double[] routeDuration;
    int[] vehicleLoad;
    int[] endDepot;
    int routes;
    int[][] customerOrder;
    boolean valid;

    public Solution(){}

    public Solution(Bean bean){
        startDepot = bean.depotOfRoute;
        vehicleNumber = new int[bean.totalVehicles];
        for (int i = 0; i < bean.depots ; i++) {
            for (int j = 0; j < bean.vehiclesPerDepot; j++) {
                vehicleNumber[i*bean.vehiclesPerDepot+j] = j+1;
            }
        }
    }

    public Solution(Bean bean, Pop pop){
        totalLength = pop.fitness;
        routes = bean.depotOfRoute.length;

        pop.calculateFitness(bean,this);
        valid = pop.valid;

        // TODO fix this block
        startDepot = new int[bean.depotOfRoute.length];
                System.arraycopy(bean.depotOfRoute,0,startDepot,0,bean.depotOfRoute.length);

        endDepot = new int[routes];
        customerOrder = new int[routes][];
        int[] vehiclesUsed = new int[bean.depots];
        vehicleNumber = new int[routes];
        for (int i = 0; i < routes; i++) {
            customerOrder[i] = new int[pop.customerOrder[i].length];
            if(pop.customerOrder[i].length==0){
                continue;
            }
            vehicleNumber[i] = ++vehiclesUsed[startDepot[i]];
            endDepot[i] = bean.nearestDepot[pop.customerOrder[i][pop.customerOrder[i].length-1]]+1;
            for (int j = 0; j < customerOrder[i].length; j++) {
                customerOrder[i][j]=pop.customerOrder[i][j]+1;
            }
        }
        for (int i = 0; i < startDepot.length; i++) {
            startDepot[i]++;
        }

//        if (!pop.valid){
//            System.out.println("INVALID POP USED AS SOLUTION:\n"+pop);
//        }
    }


    public void parse(String filename){
        File file = new File(getClass().getResource(filename).getFile());

        try {
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();

            totalLength = Double.parseDouble(line);

            routes = 0;
            while (scanner.hasNextLine()) {
                routes++;
                scanner.nextLine();
            }
            scanner.close();
            scanner = new Scanner(file);
            scanner.nextLine();

            startDepot = new int[routes];
            vehicleNumber = new int[routes];
            routeDuration = new double[routes];
            vehicleLoad = new int[routes];
            endDepot = new int[routes];
            customerOrder = new int[routes][];
            ArrayList<ArrayList<Integer>> co = new ArrayList<>();
            int i = 0;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] parts = line.split("\\s+");
                startDepot[i] = Integer.parseInt(parts[0]);
                vehicleNumber[i] = Integer.parseInt(parts[1]);
                routeDuration[i] = Double.parseDouble(parts[2].replace(',','.'));
                vehicleLoad[i] = Integer.parseInt(parts[3]);
                endDepot[i] = Integer.parseInt(parts[4]);
                co.add(new ArrayList<Integer>());
                for (int j = 5; j < parts.length; j++) {
                    co.get(i).add(Integer.parseInt(parts[j]));
                }
                i++;
            }
            for (int j = 0; j < co.size(); j++) {
                ArrayList<Integer> list = co.get(j);
                customerOrder[j] = new int[list.size()];
                for (int k = 0; k < list.size(); k++) {
                    customerOrder[j][k] = list.get(k);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String toString(){
        String s = "";
//        if (!valid){
//            s+="INVALID POP USED AS SOLUTION:\n";
//        }
        s+= String.format("%.2f", totalLength)+"\n";
        for (int i = 0; i < routes; i++) {
            if(customerOrder[i].length==0)continue;
            s+= startDepot[i] + "\t";
            s+= vehicleNumber[i] + "\t";
            s+= String.format("%.2f", routeDuration[i]) + "\t";
            s+= vehicleLoad[i] + "\t";
            s+= endDepot[i] + "\t";
            for (int j = 0; j < customerOrder[i].length; j++) {
                s+= customerOrder[i][j]+ " ";
            }
            s+= '\n';
        }
        return s;
    }

}

