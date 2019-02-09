package main;

import javafx.stage.Stage;

public class Start{
    Stage stage;
    int gens = 10000;
    int threads = 7;
    int pops = threads*800; //keep pops divisible by number of threads
    double[] maxFitness;
    int tests = 10;

    public void start(Stage stage) {
        this.stage = stage;
        System.out.println("Gens: "+gens+"   Threads: "+threads+"   Pops: "+pops);
        Bean bean = new Bean();
        for (int j = 1; j < 24; j++) runSingleTest(j,new Bean());

        //Solution solution = runSingleTest(23,bean);
        //Stage secondStage = new Stage();

        //runManyTests(1);

        //main.ScatterPlot.makePlot();
        //RoutePlot.plot(secondStage,solution,bean);
    }

    public Solution runSingleTest(int test, Bean bean){
        long startTime = System.currentTimeMillis();
        Parser parser = new Parser();
        parser.parseToBean(test,bean);

        bean.calculateNearestDepot();
        bean.calculateDist();
        //bean.printBean();
        GA ga = new GA(bean,pops,threads);
        ga.run_generation();

        maxFitness = new double[gens];
        for(int i = 0; i<gens;i++){
            ga.run_generation();
            maxFitness[i]=ga.population[0].fitness;
        }

        //FitnessPlot.plot(stage, new double[][]{maxFitness});
        //System.out.println(Arrays.toString(ga.population));
        //System.out.println(Arrays.toString(maxFitness));
        System.out.println(maxFitness[maxFitness.length-1]);
        //System.out.println(ga.population[0]);
        float timeElapsed = ((float)((System.currentTimeMillis()-startTime)))/(float)1000;
        System.out.println(String.format("Seconds elapsed: %.2f", timeElapsed));
        return new Solution(bean, ga.population[0]);
        //return null;
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
        GA ga = new GA(bean,pops,threads);
        ga.run_generation();

        maxFitness = new double[gens];
        for(int i = 0; i<gens;i++){
            ga.run_generation();
            maxFitness[i]=ga.population[0].fitness;
        }
        //System.out.println("Threaded time: "+ga.time);
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

