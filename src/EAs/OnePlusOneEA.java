package EAs;

import Objects.*;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Marcusljx on 8/29/2015.
 */
public class OnePlusOneEA {

    public static double distanceBetween(City A, City B) {
        double A_x = (double) A.getX();
        double A_y = (double) A.getY();
        double B_x = (double) B.getX();
        double B_y = (double) B.getY();

        return Math.sqrt(Math.pow(A_x - B_x, 2) + Math.pow(A_y - B_y, 2));
    }

    private static boolean verbose = false;

    public static HamiltonCycle onePlusOneEA(HamiltonCycle HC, String type, boolean verbose) {
        HamiltonCycle bestInstance = HC.clone();
        Mutator M = new Mutator();

        int eaIterations = 0;
        int bestFitness = 0;
        while (eaIterations < 10000) {
            HamiltonCycle copy = bestInstance.clone();
            HamiltonCycle current = null;

            // PERFORM MUTATION OVER DIFFERENT INITIAL SOLUTIONS
            HamiltonCycle mutatedInstance = null;
            if (type.equals("transpose")) {
                mutatedInstance = M.TransposeMutator(copy, 0.5);
            } else if (type.equals("median")) {
                mutatedInstance = M.MedianMutator(copy, 0.5);
            } else if (type.equals("disperse")) {
                mutatedInstance = M.DisperseMutator(copy, 0.5);
            } else if (type.equals("hackney")) {
                mutatedInstance = M.HackneyMutator(copy, 0.2);
            }

            ExecutorService executor = Executors.newFixedThreadPool(5);
            newTwoOptLS[] solvers = new newTwoOptLS[5];
            for (int i = 0; i < 5; i++) {
                Collections.shuffle(mutatedInstance);
                solvers[i] = new newTwoOptLS(mutatedInstance, false);
                executor.execute(solvers[i]);
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
            }

            int fitness = 0;
            for (int j = 0; j < 5; j++) {
                fitness += solvers[j].getFitness();
            }
            fitness /= 5;

            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestInstance = current.clone();
                System.out.print("TAKEN \t");
                System.out.print("{G_" + eaIterations + "} \t[" + fitness + "] \t");
                copy.printCycle();
            } else {
                if (verbose) {
                    System.out.print("\t \t");
                    System.out.print("{G_" + eaIterations + "} \t[" + fitness + "] \t");
                    copy.printCycle();
                }
            }

            eaIterations++;
        }
        System.out.println("BEST FITNESS: \t" + bestFitness);
        return bestInstance;
    }
}
