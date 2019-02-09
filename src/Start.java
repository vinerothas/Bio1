public class Start {

    public void start(){
        //        for (int j = 1; j < 24; j++) {
//            runTest(j);
//        }
        Bean bean = new Bean();
        runTest(1,bean);
    }

    public Solution runTest(int test, Bean bean){
        Parser parser = new Parser();
        if (test < 10) {
            parser.parseToBean("resources/data/p0"+test, bean);
        }else{
            parser.parseToBean("resources/data/p"+test, bean);
        }
        bean.calculateNearestDepot();
        bean.calculateDist();
        //bean.printBean();
        int gens = 100000;
        int pops = 10000;
        GA ga = new GA(bean,pops);
        ga.run_generation();


        double[] maxFitness = new double[gens];
        for(int i = 0; i<gens;i++){
            ga.run_generation();
            maxFitness[i]=ga.population[0].fitness;
        }
        //System.out.println(Arrays.toString(ga.population));
        //System.out.println(Arrays.toString(maxFitness));
        System.out.println(maxFitness[maxFitness.length-1]);
        //System.out.println(ga.population[0]);
        return new Solution(bean, ga.population[0]);
    }
}

