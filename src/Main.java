public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        Bean bean = new Bean();
        parser.parseToBean("resources/p01", bean);
        bean.calculateNearestDepot();
        bean.printBean();
        GA ga = new GA(bean);

        //ScatterPlot.makePlot();
    }
}
