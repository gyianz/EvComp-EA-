/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EAs;

import Objects.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * Population-based Evolutionary Algorithm 1. CO = Random; MU = Transpose;
 *
 * @author Xiaogang
 */
public class EA1 implements EA {

    private EAConfig config;
    private HamiltonCycle best;
    private InstancePopulation finalPopulation;

    @Override
    public void report() {
        int i = 1;
        for (HamiltonCycle HC : finalPopulation) {
            HC.renumber();
            System.out.println("##############################################");
            System.out.println("NAME: EA1_" + config.cityAmount + "_" + i);
            System.out.println("TYPE: TSP");
            System.out.println("COMMENT: Excercise 2 EA1 Instance " + i++);
            System.out.println("DIMENSION: " + HC.size());
            System.out.println("EDGE_WEIGHT_TYPE: EUC_2D");
            System.out.println("NODE_COORD_SECTION");
            HC.printCityInfo();
            System.out.println("EOF");
        }
    }

    public EA1(EAConfig config) {
        this.config = config;
    }

    @Override
    public void start() {
        int loopCount = 0;
        InstancePopulation population = new InstancePopulation(config.maxPopulationSize, config.cityAmount, 20, 20);
        InstancePopulation nextGen;
        Mutator mu = new Mutator();
        Crossover co = new Crossover();
        Random RNG = new Random(System.currentTimeMillis());
        while (loopCount < config.maxGen) {
            loopCount++;

            nextGen = new InstancePopulation();

            // produce offsprings and add to current generation
            for (int i = 0; i < config.maxPopulationSize / 2; i++) {
                population.add(co.randomCrossover(population.get(i), population.get(i + config.maxPopulationSize / 2)));
            }

            // mutate current generation and add to next generation
            for (int i = 0; i < population.size(); i++) {
                nextGen.add(mu.TransposeMutator(population.get(i), 0.5));
            }

            // evaluate each individual using 2-opt, use multiple threads to accelerate
            for (int i = 0; i < nextGen.size(); i++) {
                ExecutorService executor = Executors.newFixedThreadPool(config.trails);
                newTwoOptLS[] tasks = new newTwoOptLS[config.trails];
                for (int j = 0; j < config.trails; j++) {
                    HamiltonCycle instance = nextGen.get(i).clone();
                    Collections.shuffle(instance, RNG);
                    tasks[j] = new newTwoOptLS(instance, false);
                    executor.execute(tasks[j]);
                }
                executor.shutdown();
                while (!executor.isTerminated()) {
                }

                // set Fitness to each individual
                double fitness = 0;
                for (int j = 0; j < config.trails; j++) {
                    fitness += tasks[j].getFitness();
                }
                fitness /= config.trails;
                nextGen.get(i).setAverageFitness(fitness);
            }

            // sort the individuals according to their fitness values
            Collections.sort(nextGen);

            // trim the population so only best ones are left
            while (nextGen.size() > config.maxPopulationSize) {
                nextGen.remove(nextGen.size() - 1);
            }

            population = nextGen.clone();
        }
        best = population.get(0).clone();
        best.renumber();
        finalPopulation = population;
    }

}
