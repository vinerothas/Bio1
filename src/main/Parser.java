package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Parser {


    public void parseToBean(int test, Bean bean) {
        String filename;
        if (test < 10) {
            filename = "/resources/data/p0"+test;
        }else{
            filename = "/resources/data/p"+test;
        }
        File file = new File(getClass().getResource(filename).getFile());

        try (Scanner scanner = new Scanner(file)) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");

            bean.vehicles = Integer.parseInt(parts[0]);
            bean.customers = Integer.parseInt(parts[1]);
            bean.depots = Integer.parseInt(parts[2]);


            bean.vehicle_duration = new int[bean.depots];
            bean.vehicle_load = new int[bean.depots];

            for (int i = 0; i < bean.depots; i++) {
                line = scanner.nextLine();
                parts = line.split("\\s+");
                bean.vehicle_duration[i] = Integer.parseInt(parts[0]);
                bean.vehicle_load[i] = Integer.parseInt(parts[1]);
            }

            bean.customer_x = new int[bean.customers];
            bean.customer_y = new int[bean.customers];
            bean.service_duration = new int[bean.customers];
            bean.service_demand = new int[bean.customers];

            for (int i = 0; i < bean.customers; i++) {
                line = scanner.nextLine();
                if(line.startsWith(" ")){
                    line = line.substring(1);
                }
                parts = line.split("\\s+");
                bean.customer_x[i] = Integer.parseInt(parts[1]);
                bean.customer_y[i] = Integer.parseInt(parts[2]);
                bean.service_duration[i] = Integer.parseInt(parts[3]);
                bean.service_demand[i] = Integer.parseInt(parts[4]);
                bean.totalDemand += bean.service_demand[i];

                if(bean.customer_x[i] > bean.max_x){
                    bean.max_x = bean.customer_x[i];
                }else if(bean.customer_x[i] < bean.min_x){
                    bean.min_x = bean.customer_x[i];
                }
                if(bean.customer_y[i] > bean.max_y){
                    bean.max_y = bean.customer_y[i];
                }else if(bean.customer_y[i] < bean.min_y){
                    bean.min_y = bean.customer_y[i];
                }
            }

            bean.depot_x = new int[bean.depots];
            bean.depot_y = new int[bean.depots];

            for (int i = 0; i < bean.depots; i++) {
                line = scanner.nextLine();
                parts = line.split("\\s+");
                bean.depot_x[i] = Integer.parseInt(parts[1]);
                bean.depot_y[i] = Integer.parseInt(parts[2]);

                if(bean.depot_x[i] > bean.max_x){
                    bean.max_x = bean.customer_x[i];
                }else if(bean.depot_x[i] < bean.min_x){
                    bean.min_x = bean.customer_x[i];
                }
                if(bean.depot_y[i] > bean.max_y){
                    bean.max_y = bean.customer_y[i];
                }else if(bean.depot_y[i] < bean.min_y){
                    bean.min_y = bean.customer_y[i];
                }
            }

            bean.totalVehicles = bean.depots*bean.vehicles;
            int minLoad = Integer.MAX_VALUE;
            for(int i = 0; i < bean.vehicle_load.length;i++){
                if(bean.vehicle_load[i]<minLoad){
                    minLoad=bean.vehicle_load[i];
                }
            }
            bean.minVehicles = (int)Math.ceil(bean.totalDemand/(float)minLoad);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
