package main;

public class Param {

    static int gens = 30000;
    static int threads = 6;
    static int pops = threads*1000; //keep pops divisible by number of threads

    static double elitismPercent = 10; //ACTUALLY PERCENT of how much of best population is guaranteed to survive

    static double mutationRateM = 0.6;
    static double mutationRateS = 0.6;
    static double mutationRateA = 0.2;
    static double mutationRateR = 0.2;
    static double crossoverRate = 0.1;

    static int tests = 10;
    static int test = 23;

    static boolean allTestsOnce = false;
    static boolean allTestsMany = false;
    static boolean oneTestOnce  = true;
    static boolean oneTestMany = false;

    static double fitnessComparison = 0.01;

    final static int capacityPenalty = 12; //(bitwise left shift) fitness penalty for too high demand in a route (per unit demand)
    final static int depotPenalty = 12; //(bitwise left shift) fitness penalty for too many vehicles from same depot (per vehicle)
}
