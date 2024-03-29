package main;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Set;


public class ScatterPlot extends Application {

    @Override
    public void start(Stage stage) {
        Parser parser = new Parser();
        Bean bean = new Bean();
        parser.parseToBean(8, bean);
        bean.calculateNearestDepot();
        bean.calculateDist();
        //bean.printBean();
        GA ga = new GA(bean,2000, 4);

        stage.setTitle("Scatter Chart Sample");
        final NumberAxis xAxis = new NumberAxis(bean.min_x - 10, bean.max_x + 10, 1);
        final NumberAxis yAxis = new NumberAxis(bean.min_y - 10, bean.max_y + 10, 1);
        final ScatterChart<Number, Number> sc = new
                ScatterChart<Number, Number>(xAxis, yAxis);
        xAxis.setLabel("X");
        yAxis.setLabel("Y");
        sc.setTitle("Customers plot");

        for (int i = 0; i < bean.depots; i++) {
            XYChart.Series seriesDepot = new XYChart.Series();
            seriesDepot.setName("Depot " + i);
            seriesDepot.getData().add(new XYChart.Data(bean.depot_x[i], bean.depot_y[i]));
            sc.getData().add(seriesDepot);

            XYChart.Series series = new XYChart.Series();
            series.setName("Depot Group " + i);
            for (int j = 0; j < bean.customers; j++) {
                if (bean.nearestDepot[j] == i) {
                    series.getData().add(new XYChart.Data(bean.customer_x[j], bean.customer_y[j]));
                }
            }
            sc.getData().add(series);
        }


        Scene scene = new Scene(sc, 500, 400);
        stage.setScene(scene);
        stage.show();

        String[] colors = {"red","#D0B518", "blue","green","#ff8d01","#ff00ab","#00fcff", "#c201ff", "#757274", "#0d0d0d"};
        for (int i = 0; i < bean.depots; i++) {
            int index = i*2;
            Set<Node> nodes = sc.lookupAll(".series" + index);
            for (Node n : nodes) {
                String s = "-fx-background-color: " + colors[i] + ", black;\n"
                        + "    -fx-background-insets: 0, 2;\n"
                        + "    -fx-background-radius: 5px;\n"
                        + "    -fx-padding: 5px;";
                n.setStyle(s);
            }
            index++;
            nodes = sc.lookupAll(".series" + index);
            for (Node n : nodes) {
                String s = "-fx-background-color: " + colors[i] + ", " + colors[i] + ";\n"
                        + "    -fx-background-insets: 0, 2;\n"
                        + "    -fx-background-radius: 5px;\n"
                        + "    -fx-padding: 5px;";
                n.setStyle(s);
            }
        }

    }

    public static void makePlot() {
        launch();
    }
}