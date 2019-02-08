import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        Bean bean = new Bean();
        parser.parseToBean("resources/p01", bean);
        bean.calculateNearestDepot();
        bean.calculateDist();
        bean.printBean();
        GA ga = new GA(bean);
//        ga.run_generation();
//
//        int gens = 10000;
//        float[] maxFitness = new float[gens];
//        for(int i = 0; i<gens;i++){
//            ga.run_generation();
//            maxFitness[i]=ga.population[0].fitness;
//        }
        System.out.println(Arrays.toString(ga.population));
        //System.out.println(Arrays.toString(maxFitness));

        //ScatterPlot.makePlot();
    }
}
