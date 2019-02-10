package main;

import javafx.stage.Stage;

public class Start {
    Stage stage;
    double[] maxFitness;

    public void start(Stage stage) {
        this.stage = stage;
        if (Param.oneTestMany || Param.oneTestOnce) System.out.print("P" + Param.test + " ");
        System.out.println("Gens: " + Param.gens + "   Threads: " + Param.threads + "   Pops: " + Param.pops + "  Crossover: " + Param.crossoverRate + "  Elitism: " + Param.elitismPercent);
        System.out.println("mutationRateM: " + Param.mutationRateM + "   mutationRateS: " + Param.mutationRateS + "   mutationRateC: " + Param.mutationRateC);

        Bean bean = new Bean();

        if (Param.allTestsOnce) {
            for (int j = 1; j < 24; j++) runSingleTest(j, new Bean());
        } else if (Param.allTestsMany) {
            for (int j = 1; j < 24; j++) runManyTests(j);
        } else if (Param.oneTestOnce) {
            Solution solution = runSingleTest(Param.test, bean);
            Stage secondStage = new Stage();
            RoutePlot.plot(secondStage, solution, bean);
        } else if (Param.oneTestMany) {
            runManyTests(Param.test);
        } else {
            testP1();
        }


    }

    public Solution runSingleTest(int test, Bean bean) {
        long startTime = System.currentTimeMillis();
        Parser parser = new Parser();
        parser.parseToBean(test, bean);

        bean.calculateNearestDepot();
        bean.calculateDist();
        //bean.printBean();
        GA ga = new GA(bean, Param.pops, Param.threads);
        ga.run_generation();

        maxFitness = new double[Param.gens];
        for (int i = 0; i < Param.gens; i++) {
            ga.run_generation();
            maxFitness[i] = ga.population[0].fitness;
            if (i % 1000 == 0) {
                System.out.println(i + " " + ga.population[0].fitness);
//                System.out.println(i + " " + ga.population[0]);
                for (int j = 0; j < 100; j++) {
                    System.out.print(String.format("%.2f", ga.population[j].fitness) + " ");
                }
                System.out.println();
            }
        }

        FitnessPlot.plot(stage, new double[][]{maxFitness});
        //System.out.println(Arrays.toString(ga.population));
        //System.out.println(Arrays.toString(maxFitness));
        System.out.println(maxFitness[maxFitness.length - 1]);
        //System.out.println(ga.population[0]);
        float timeElapsed = ((float) ((System.currentTimeMillis() - startTime))) / (float) 1000;
        System.out.println(String.format("Seconds elapsed: %.2f", timeElapsed));
        return new Solution(bean, ga.population[0]);
        //return null;
    }

    public void runManyTests(int test) {
        Bean bean = new Bean();
        Parser parser = new Parser();
        parser.parseToBean(test, bean);
        bean.calculateNearestDepot();
        bean.calculateDist();
        double[][] testFitness = new double[Param.tests][];
        float totalTime = 0;
        double totalFitness = 0;
        for (int i = 0; i < Param.tests; i++) {
            long startTime = System.currentTimeMillis();
            runTestOfMany(bean);
            float timeElapsed = ((float) ((System.currentTimeMillis() - startTime))) / (float) 1000;
            totalTime += timeElapsed;
            System.out.println(String.format("Seconds elapsed test " + i + " : %.2f", timeElapsed));
            testFitness[i] = maxFitness;
            totalFitness += maxFitness[maxFitness.length - 1];
            System.out.println(maxFitness[maxFitness.length - 1]);
        }
        //System.out.println(String.format("Total time: %.2f", totalTime)+String.format("   Total fitness: %.2f", totalFitness));
        totalTime /= Param.tests;
        totalFitness /= Param.tests;
        System.out.println(String.format("Average time: %.2f", totalTime) + String.format("   Average fitness: %.2f", totalFitness));
        //FitnessPlot.plot(new Stage(),testFitness);
    }

    public void runTestOfMany(Bean bean) {
        GA ga = new GA(bean, Param.pops, Param.threads);
        ga.run_generation();

        //Scanner in = new Scanner(System.in);
        //long startTime = System.currentTimeMillis();
        maxFitness = new double[Param.gens];
        for (int i = 0; i < Param.gens; i++) {
            //if(((float)((System.currentTimeMillis()-startTime)))/(float)1000 >195){break;}
            ga.run_generation();
            maxFitness[i] = ga.population[0].fitness;
            if (i % 100 == 0) {
                System.out.println(i + " " + ga.population[0].fitness);
//                //if(i%2000==0)if(in.next().charAt(0)=='1')System.out.println("boop");}
            }
        }

        //System.out.println("Threaded time: "+ga.time);
    }

    public void testP1() {
        Bean bean = new Bean();
        Parser parser = new Parser();
        parser.parseToBean(1, bean);
        bean.calculateNearestDepot();
        bean.calculateDist();
        Pop pop = new Pop(true);
        pop.calculateFitness(bean);
        Solution solution = new Solution(bean, pop);
        RoutePlot.plot(stage, solution, bean);
        System.out.println(solution);
    }

    private void loadSolution(Bean bean, Solution solution, int test) {
        if (test < 10) {
            solution.parse("/resources/solutions/p0" + test + ".res");
        } else {
            solution.parse("/resources/solutions/p" + test + ".res");
        }
        System.out.println(solution);

        Parser parser = new Parser();
        if (test < 10) {
            parser.parseToBean(test, bean);
        } else {
            parser.parseToBean(test, bean);
        }
    }

}

