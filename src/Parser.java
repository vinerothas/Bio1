
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Parser {

    private ClassLoader classLoader = getClass().getClassLoader();

    public Parser() {


    }

    // TODO remove
    public String parse(String filename) {
        StringBuilder result = new StringBuilder("");

        File file = new File(classLoader.getResource(filename).getFile());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public void parseToBean(String filename, Bean bean) {
        File file = new File(classLoader.getResource(filename).getFile());

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
                parts = line.split("\\s+");
                bean.customer_x[i] = Integer.parseInt(parts[1]);
                bean.customer_y[i] = Integer.parseInt(parts[2]);
                bean.service_duration[i] = Integer.parseInt(parts[3]);
                bean.service_demand[i] = Integer.parseInt(parts[4]);

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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
