package Tic_Tac_Atrebas.jav.Neural;

import Tic_Tac_Atrebas.jav.ClassicGameModel;
import Tic_Tac_Atrebas.jav.GameConfiguration;
import Tic_Tac_Atrebas.jav.GameModel;

public class EvolutionStarter {
    GameModel gameModel;
    public EvolutionStarter(int generations, int populationSize, GameConfiguration config) {
        gameModel = new ClassicGameModel(config);
        NeuralNetwork net1 = new NeuralNetwork(2, 1);
        NeuralNetwork net2 = new NeuralNetwork(2, 1);

        System.out.println("Generation 0: " + net1);

        for (int gen = 0; gen < generations; gen++) {
            // Mutation
            net1.mutate();
            net2.mutate();

            if (gen % 10 == 0) {
                System.out.println("Generation " + gen + ": " + net1);
            }

            // Test-Output
            double[] output = net1.forward(new double[]{0.5, 0.3});
            System.out.println("  Output: " + output[0]);
        }





        // Population erstellen
        NeuralNetwork[] population = new NeuralNetwork[20];
        for (int i = 0; i < population.length; i++) {
            population[i] = new NeuralNetwork(2, 1);
        }

        for (int gen = 0; gen < populationSize; gen++) {
            population[gen].fitness*= NeuralNetwork.fitnessKeepRate;
        }

        // Generationen trainierenm
        for (int gen = 0; gen < populationSize; gen++) {
            // Fitness bewerten (deine Aufgabe!)
            for (int i = 0; i < population.length; i++) {
                NeuralNetwork net = population[i];
                evaluateFitness(net, i, population); //already sets the fitness
            }

            // Best speichern
            if (gen % 10 == 0) {
                //NeuralNetwork best = getBest(population);
                //NeuralNetPersistence.save(best.brain, "best_gen_" + gen + ".json");
            }

            // Mutation für nächste Generation
            NeuralNetwork[] newPop = new NeuralNetwork[population.length];
            //newPop[0] = getBest(population); // Elite erhalten
            for (int i = 1; i < newPop.length; i++) {
              //  newPop[i] = newPop[0].mutate(0.1);
            }
            population = newPop;
        }


    }

        private void evaluateFitness(NeuralNetwork net, int index, NeuralNetwork[] population)
        {
            // Hier deine Fitnessfunktion
            int oppi;
            NeuralNetwork opponent;
            for (int i = 0; i < 8; i++) {
                oppi = (index + 11*(i+1)) % 20;
                opponent= population[oppi];
                gameModel.reset();

                while(!gameModel.full()) {
                    double[] input = gameModel.getFieldsArray();
                    double[] output = net.forward(input);
                    int move = (int) (output[0] * (gameModel.getAiBoard().length-1)); // Output in Spielfeldgröße umwandeln
                    if(!gameModel.makeMoveAtField(move)) {
                        // Ungültiger Zug, Fitness stark reduzieren
                        net.fitness -= 10;
                        break;
                    }

                    if(gameModel.gameOver) {
                        if(gameModel.winner==1) {
                            net.fitness += 20; // Gewinn
                            opponent.fitness -= 7; // Verlust
                        } else if(gameModel.winner==2) {
                            opponent.fitness += 20; // Gewinn
                            net.fitness -= 7; // Verlust
                        } else {
                            net.fitness += 3; // Unentschieden
                            opponent.fitness += 3; // Unentschieden
                        }
                        break;
                    }

                    input = gameModel.getFieldsArray();
                    output = opponent.forward(input);
                    move = (int) (output[0] * (gameModel.getAiBoard().length-1)); // Output in Spielfeldgröße umwandeln
                    if(!gameModel.makeMoveAtField(move)) {
                        // Ungültiger Zug, Fitness stark reduzieren
                        opponent.fitness -= 10;
                        break;
                    }

                    if(gameModel.gameOver) {
                        if(gameModel.winner==1) {
                            net.fitness += 20; // Gewinn
                            opponent.fitness -= 7; // Verlust
                        } else if(gameModel.winner==2) {
                            opponent.fitness += 20; // Gewinn
                            net.fitness -= 7; // Verlust
                        } else {
                            net.fitness += 3; // Unentschieden
                            opponent.fitness += 3; // Unentschieden
                        }
                        break;
                    }

                }
            }
        }
}







