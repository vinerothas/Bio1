package main;

public class Param {

    static int gens = 1000;
    static int threads = 7;
    static int popsPerThread = 1000;
    static int pops = threads*popsPerThread; //keep pops divisible by number of threads

    static double mutationRate = 1;
    static double mutationRateM = 0.6;
    static double mutationRateS = 0.3;
    static double mutationRateI = 0.1;
    static double crossoverRate = 0.9;

    static int tests = 10;
    static int test = 8;

    static boolean allTestsOnce = false;
    static boolean allTestsMany = false;
    static boolean oneTestOnce  = false;
    static boolean oneTestMany = false;

    static double fitnessComparison = 0.0001;

    final static int capacityPenalty = 8; //(bitwise left shift) fitness penalty for too high demand in a route (per unit demand)
    final static int lengthPenalty = 8; //(bitwise left shift) fitness penalty for routes exceeding duration constraints

    static int tournamentSize = 5;
}
