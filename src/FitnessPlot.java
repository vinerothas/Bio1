import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Arrays;


public class FitnessPlot extends Application {

    @Override public void start(Stage stage) {
        Parser parser = new Parser();
        Bean bean = new Bean();
        parser.parseToBean("resources/data/p01", bean);
        bean.calculateNearestDepot();
        bean.calculateDist();
        //bean.printBean();
        GA ga = new GA(bean,2000);
        ga.run_generation();



        int gens = 10000;
        double[] maxFitness = new double[gens];
        for(int i = 0; i<gens;i++){
            ga.run_generation();
            maxFitness[i]=ga.population[0].fitness;
        }
        System.out.println(Arrays.toString(ga.population));
        System.out.println(Arrays.toString(maxFitness));

        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        int increment = maxFitness.length/500;
        for (int i = 0; i < maxFitness.length; i+=increment) {
            if(maxFitness[i] == Float.MAX_VALUE){
                series.getData().add(new XYChart.Data(i, 2500));
            }else{
                series.getData().add(new XYChart.Data(i, maxFitness[i]));
            }
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    public static void makePlot() {
        launch();
    }
}