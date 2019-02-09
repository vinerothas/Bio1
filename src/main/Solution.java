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

    public Solution(){

    }

    public Solution(Bean bean, Pop pop){
        totalLength = pop.fitness;
        routes = pop.vehicles;

        // TODO change to pop.startDepot if representation is changed
        startDepot = new int[routes];
        endDepot = new int[routes];
        customerOrder = new int[routes][];
        int[] vehiclesUsed = new int[bean.depots];
        vehicleNumber = new int[routes];
        for (int i = 0; i < routes; i++) {
            startDepot[i] = bean.nearestDepot[pop.customerOrder[i][0]]+1;
            vehicleNumber[i] = ++vehiclesUsed[startDepot[i]-1];
            endDepot[i] = bean.nearestDepot[pop.customerOrder[i][pop.customerOrder[i].length-1]]+1;
            customerOrder[i] = new int[pop.customerOrder[i].length];
            for (int j = 0; j < customerOrder[i].length; j++) {
                customerOrder[i][j]=pop.customerOrder[i][j]+1;
            }
        }

        pop.calculateRouteValues(bean,this);
        if (!pop.valid){
            System.out.println("INVALID POP USED AS SOLUTION:\n"+pop);
        }
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
        s+= String.format("%.2f", totalLength)+"\n";
        for (int i = 0; i < routes; i++) {
            s+= startDepot[i] + "   ";
            s+= vehicleNumber[i] + "   ";
            s+= String.format("%.2f", routeDuration[i]) + "   ";
            s+= vehicleLoad[i] + "   ";
            s+= endDepot[i] + "  ";
            for (int j = 0; j < customerOrder[i].length; j++) {
                s+= customerOrder[i][j]+ " ";
            }
            s+= '\n';
        }
        return s;
    }

}

