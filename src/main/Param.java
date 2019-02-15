package main;

public class Param {

    static int gens = 10000000;
    static int threads = 7;
    static int popsPerThread = 200;
    static int pops = threads*popsPerThread; //keep pops divisible by number of threads

    static double mutationRate = 1;
    static double mutationRateM = 0.65;
    static double mutationRateS = 0.3;
    static double mutationRateI = 0.05;
    static double crossoverRate = 0.8;

    static double noisyOperatorChance = 0.1;

    static int tests = 50;
    static int test = 29;

    static boolean allTestsOnce = false;
    static boolean allTestsMany = false;
    static boolean oneTestOnce  = true;
    static boolean oneTestMany = false;
    static boolean plotSolution = false;

    static double fitnessComparison = 0.00001;

    final static int capacityPenalty = 7; //(bitwise left shift) fitness penalty for too high demand in a route (per unit demand)
    final static int lengthPenalty = 7; //(bitwise left shift) fitness penalty for routes exceeding duration constraints

    static int tournamentSize = 50;

}
