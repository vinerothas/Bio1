package main;

public class Param {

    static int gens = 10000000;
    static int threads = 7;
    static int pops = threads*100; //keep pops divisible by number of threads

    static double elitismPercent = 10; //ACTUALLY PERCENT of how much of best population is guaranteed to survive

    static double mutationRate = 1;
    static double mutationRateM = 0.3;
    static double mutationRateS = 0.3;
    static double mutationRateC = 0.3;
    static double mutationRateD = 0.1;
    static double crossoverRate = 0.4;

    static int tests = 10;
    static int test = 25;

    static boolean allTestsOnce = false;
    static boolean allTestsMany = false;
    static boolean oneTestOnce  = true;
    static boolean oneTestMany = false;

    static double fitnessComparison = 0.0001;

    final static int capacityPenalty = 12; //(bitwise left shift) fitness penalty for too high demand in a route (per unit demand)
    final static int depotPenalty = 12; //(bitwise left shift) fitness penalty for too many vehicles from same depot (per vehicle)
}
