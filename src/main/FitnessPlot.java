package main;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Arrays;


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
        double[] average = new double[maxFitness[0].length];
        double[] max = new double[maxFitness[0].length];
        double[] min = new double[maxFitness[0].length];
        Arrays.fill(min,Double.MAX_VALUE);

        boolean averageOnly = true;

        for (int i = 0; i < maxFitness.length; i++) {
            XYChart.Series series = new XYChart.Series();
            series.setName("Fitness "+i);
            //int increment = maxFitness[i].length/500;
            int increment = 1;
            for (int j = 0; j < maxFitness[i].length; j+=increment) {
                average[j] += maxFitness[i][j];
                if(maxFitness[i][j]>max[j]){
                    max[j] = maxFitness[i][j];
                }
                if(maxFitness[i][j]<min[j]){
                    min[j] = maxFitness[i][j];
                }
                series.getData().add(new XYChart.Data(j, maxFitness[i][j]));
            }
            if(!averageOnly)lineChart.getData().add(series);
        }

        for (int i = 0; i < average.length ; i++) {
            average[i] = average[i]/(double)maxFitness.length;
        }

        XYChart.Series seriesA = new XYChart.Series();
        seriesA.setName("Average");
        XYChart.Series seriesMax = new XYChart.Series();
        seriesMax.setName("Worst fitness");
        XYChart.Series seriesMin = new XYChart.Series();
        seriesMin.setName("Best fitness");
        for (int j = 0; j < average.length; j++) {
            seriesA.getData().add(new XYChart.Data(j, average[j]));
            seriesMax.getData().add(new XYChart.Data(j, max[j]));
            seriesMin.getData().add(new XYChart.Data(j, min[j]));
        }
        lineChart.getData().add(seriesA);
        lineChart.getData().add(seriesMax);
        lineChart.getData().add(seriesMin);

        Scene scene  = new Scene(lineChart,800,600);

        stage.setScene(scene);
        stage.show();
    }

}