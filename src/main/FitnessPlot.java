package main;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class FitnessPlot {

    public static void plot(Stage stage, double[][] maxFitness) {


        stage.setTitle("Fitness");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generations");
        yAxis.setLabel("Fitness");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Fitness");
        lineChart.setCreateSymbols(false);

        for (int i = 0; i < maxFitness.length; i++) {
            XYChart.Series series = new XYChart.Series();
            series.setName("Fitness "+i);
            int increment = maxFitness[i].length/500;
            for (int j = 500; j < maxFitness[i].length; j+=increment) {
                series.getData().add(new XYChart.Data(j, maxFitness[i][j]));
            }
            lineChart.getData().add(series);
        }

        Scene scene  = new Scene(lineChart,800,600);

        stage.setScene(scene);
        stage.show();
    }

}