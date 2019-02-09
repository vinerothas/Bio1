package main;

import javafx.stage.Stage;

import java.util.Arrays;

public class Start{
    Stage stage;
    int gens = 5000;
    int pops = 2000;
    double[] maxFitness;
    int tests = 10;

    public void start(Stage stage) {
        this.stage = stage;
        //for (int j = 1; j < 24; j) runTestOfMany(j);

        //Bean bean = new Bean();
        //Solution solution = runSingleTest(1,bean);

        runManyTests(1);

        //main.ScatterPlot.makePlot();
        //RoutePlot.plot(stage,solution,bean);
    }

    public Solution runSingleTest(int test, Bean bean){
        Parser parser = new Parser();
        parser.parseToBean(test,bean);

        bean.calculateNearestDepot();
        bean.calculateDist();
        //bean.printBean();
        GA ga = new GA(bean,pops);
        ga.run_generation();

        maxFitness = new double[gens];
        for(int i = 0; i<gens;i++){
            ga.run_generation();
            maxFitness[i]=ga.population[0].fitness;
        }

        //FitnessPlot.plot(stage, maxFitness);
        //System.out.println(Arrays.toString(ga.population));
        System.out.println(Arrays.toString(maxFitness));
        System.out.println(maxFitness[maxFitness.length-1]);
        System.out.println(ga.population[0]);
        return new Solution(bean, ga.population[0]);
    }

    public void runManyTests(int test){
        Bean bean = new Bean();
        Parser parser = new Parser();
        parser.parseToBean(test,bean);
        bean.calculateNearestDepot();
        bean.calculateDist();
        double[][] testFitness = new double[tests][];
        for (int i = 0; i < tests; i++) {
            runTestOfMany(bean);
            testFitness[i] = maxFitness;
            System.out.println(maxFitness[maxFitness.length-1]);
        }
        FitnessPlot.plot(stage,testFitness);
    }

    public void runTestOfMany(Bean bean){
        GA ga = new GA(bean,pops);
        ga.run_generation();

        maxFitness = new double[gens];
        for(int i = 0; i<gens;i++){
            ga.run_generation();
            maxFitness[i]=ga.population[0].fitness;
        }
    }

    private void loadSolution(Bean bean, Solution solution, int test){
        if (test < 10) {
            solution.parse("/resources/solutions/p0"+test+".res");
        }else{
            solution.parse("/resources/solutions/p"+test+".res");
        }
        System.out.println(solution);

        Parser parser = new Parser();
        if (test < 10) {
            parser.parseToBean(test, bean);
        }else{
            parser.parseToBean(test, bean);
        }
    }

}

