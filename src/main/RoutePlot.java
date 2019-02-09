package main;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class RoutePlot {

    public static void plot(Stage stage, Solution solution, Bean bean){
        stage.setTitle("Routes");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        //creating the chart
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        lineChart.setTitle("Routes");
        //defining a series
        for (int i = 0; i < solution.routes; i++) {
            XYChart.Series series = new XYChart.Series();
            series.setName("Route "+i);
            int route[] = solution.customerOrder[i];
            int d = solution.startDepot[i]-1;
            series.getData().add(new XYChart.Data(bean.depot_x[d],bean.depot_y[d]));
            for (int j = 0; j < route.length; j++) {
                int c = solution.customerOrder[i][j]-1;
                series.getData().add(new XYChart.Data(bean.customer_x[c],bean.customer_y[c]));
            }
            d = solution.endDepot[i]-1;
            series.getData().add(new XYChart.Data(bean.depot_x[d],bean.depot_y[d]));
            lineChart.getData().add(series);
        }

        Scene scene  = new Scene(lineChart,800,600);

        stage.setScene(scene);
        stage.show();
    }

}
