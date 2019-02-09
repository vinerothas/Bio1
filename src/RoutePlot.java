import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class RoutePlot extends Application {
    @Override public void start(Stage stage) {

        //bean.calculateNearestDepot();
        //bean.calculateDist();
        Bean bean = new Bean();
        Solution solution = new Solution();

        //loadSolution(bean,solution,1);

        solution = new Start().runTest(23,bean);

        plot(stage,solution,bean);

    }

    private void loadSolution(Bean bean, Solution solution, int test){
        if (test < 10) {
            solution.parse("resources/solutions/p0"+test+".res");
        }else{
            solution.parse("resources/solutions/p"+test+".res");
        }
        System.out.println(solution);

        Parser parser = new Parser();
        if (test < 10) {
            parser.parseToBean("resources/data/p0"+test, bean);
        }else{
            parser.parseToBean("resources/data/p"+test, bean);
        }
    }

    private void plot(Stage stage, Solution solution, Bean bean){
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        lineChart.setTitle("Stock Monitoring, 2010");
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

    public static void makePlot() {
        launch();
    }
}
