package Driver;

import EAs.*;
import Objects.*;
import EAs.EAConfig;
import TSP_Solvers.*;

import java.io.*;
import java.util.Collections;

public class Driver {

    public static int X_POSITION_LIMIT = 20;
    public static int Y_POSITION_LIMIT = 20;

    public static void testFixedTourSolver(String solverType, String Tour) {
        // init file of cities(hardest instance after attract mutator)
        HamiltonCycle HC = new HamiltonCycle();
        try (BufferedReader br = new BufferedReader(new FileReader("src/Driver/demoFixedPositions.txt"))) {
            int i = 0;
            for (String line; (line = br.readLine()) != null;) {
                String[] numbers = line.split("\\s+");
//                System.out.println(numbers[0] + "xxx" + numbers[1] + "xxx" + numbers[2]);
                City C = new City(i + 1, false, Double.parseDouble(numbers[1]), Double.parseDouble(numbers[2]));
                HC.add(C);
                i++;
            }
        } catch (Exception e) {

        }

        HC.printCityInfo();
        Collections.shuffle(HC);
        HC.setTourOrder(Tour);
        HC.printCycle();
        System.out.println("=======================================================");
        TSP_Solver TS = new TSP_SolverFactory().setAlgorithm(solverType);
        HamiltonCycle result = TS.solve(HC);

        result.printCycle();
    }

    public static void run1plus1EA(String[] args) {
        // ARG: RUN WHICH TEST?
        String testType = args[1];

        // ARG: NUMBER OF CITIES
        int num_of_cities = Integer.parseInt(args[2]);

        HamiltonCycle HC = new HamiltonCycle(num_of_cities, X_POSITION_LIMIT, Y_POSITION_LIMIT);
        System.out.println("======================= ORIGINAL City Positions =======================");
        HC.printCityInfo();
        System.out.println("\n\n#######################################################################\n\n");

        HamiltonCycle best_of_EA = null;
        if (testType.equals("-transpose") || testType.equals("-all")) {
            /**
             * DO TRANSPOSE *
             */
            System.out.println("TRANSPOSE_MUTATOR VERSION");
            System.out.println("==================================");
            best_of_EA = OnePlusOneEA.onePlusOneEA(HC, "transpose", false);
            System.out.println("======================= TRANSPOSE: FINAL City Positions =======================");
            best_of_EA.printCityInfo();
        }

        if (testType.equals("-median") || testType.equals("-all")) {
            /**
             * DO MEDIAN *
             */
            System.out.println("MEDIAN_MUTATOR VERSION");
            System.out.println("==================================");
            best_of_EA = OnePlusOneEA.onePlusOneEA(HC, "median", false);
            System.out.println("======================= MEDIAN: FINAL City Positions =======================");
            best_of_EA.printCityInfo();
        }

        if (testType.equals("-disperse") || testType.equals("-all")) {
            /**
             * DO DISPERSE *
             */
            System.out.println("DISPERSE_MUTATOR VERSION");
            System.out.println("==================================");
            best_of_EA = OnePlusOneEA.onePlusOneEA(HC, "disperse", false);
            System.out.println("======================= DISPERSE: FINAL City Positions =======================");
            best_of_EA.printCityInfo();
        }

        if (testType.equals("-hackney") || testType.equals("-all")) {
            /**
             * DO HACKNEY *
             */
            System.out.println("HACKNEY_MUTATOR VERSION");
            System.out.println("==================================");
            best_of_EA = OnePlusOneEA.onePlusOneEA(HC, "hackney", false);
            System.out.println("======================= HACKNEY: FINAL City Positions =======================");
            best_of_EA.printCityInfo();
        }
    }

    public static void runEx2(String[] args) {
        EAConfig config = new EAConfig();
        switch (args[1]) {
            case "EA1":
                config.EAname = "1";
                break;

            case "EA2":
                config.EAname = "2";
                break;

            case "EA3":
                config.EAname = "3";
                break;
        }
        config.cityAmount = Integer.parseInt(args[2]);
        EA ea = EAFactory.getEA(config);

        ea.start();
        ea.report();
    }

    public static void runEx3(String[] args) {
        String algorithmA = args[1];
        String algorithmB = args[2];
        String measureType = args[3];
        boolean debug_flag;
        if (args.length > 4) {
            debug_flag = Boolean.parseBoolean(args[4]);
        } else {
            debug_flag = false;
        }

        System.out.println("++++ COMPARING [" + algorithmA + "] AGAINST [" + algorithmB + "] using (" + measureType + ")");
        System.out.println("==== 100 \tCities per Instance ====");
        System.out.println("==== 10 \tInstances per Population ====");
        System.out.println("==== 1000 \tGenerations ====");

        PerformanceMeasure PM = new PerformanceMeasure(debug_flag);
        HamiltonCycle bestInstance = PM.runComparison_maximisePerformance(algorithmA, algorithmB, measureType);

        System.out.println("\nBest " + measureType + " performance score: " + bestInstance.getAverageFitness());
        System.out.println("=== BEST INSTANCE CITY INFO ===");
        bestInstance.printCityInfo();
    }

    public static void main(String[] args) {

        // ARG: RUN WHAT?
        if (args.length > 0) {
            switch (args[0]) {
                case "Ex1":
                    run1plus1EA(args);
                    break;

                case "Ex2":
                    runEx2(args);
                    break;

                case "Ex3":
                    runEx3(args);
                    break;
            }
        } else {
            System.out.println("ERROR: NO INPUT ARGUMENTS FOUND");
        }
    }
}
