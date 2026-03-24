#include <iostream>
#include <exception>
#include <vector>

#include "Evaluator.hpp"
#include "GeneticAlgorithm.hpp"
#include "ProblemLoader.hpp"
#include "ProblemData.hpp"

using namespace LcVRPContest;

void runOptimization(const std::string& filepath, int popSize, double crossProb, double mutProb, int maxIterations, std::mt19937& generator) {
    std::cout << "RUNNING INSTANCE: " << filepath << std::endl;

    try {
        Evaluator evaluator(filepath, false, generator);

        GeneticAlgorithm ga(popSize, crossProb, mutProb, maxIterations, generator);

        std::cout << "Problem loaded. Customers: " << evaluator.getGenotypeSize()
            << ", Groups: " << evaluator.getNumberOfGroups() << std::endl;
        std::cout << "Optimization started..." << std::endl;

        ga.run(evaluator);

        const Individual* bestInd = ga.getBestSolution();
        if (bestInd) {
            double bestFitness = evaluator.evaluate(bestInd->getGenotype());
            std::cout << "OPTIMIZATION FINISHED" << std::endl;
            std::cout << "Best Fitness (Lowest total Distance): " << bestFitness << std::endl;
        }
        else {
            std::cout << "Error: No solution found." << std::endl;
        }
    }
    catch (const std::exception& e) {
        std::cerr << "CRITICAL ERROR during optimization: " << e.what() << std::endl;
    }
}

void runSafetyTests(std::mt19937& generator) {
    std::cout << "      TESTS      " << std::endl;

    std::cout << "[TEST] G.A Invalid Population Size (-10)    ";
    try {
        GeneticAlgorithm ga(-10, 0.6, 0.05, 100, generator);
        std::cout << "FAILED (Constructor should throw)" << std::endl;
    }
    catch (const std::invalid_argument& e) {
        std::cout << "PASSED (Caught: " << e.what() << ")" << std::endl;
    }

    std::cout << "[TEST] G.A Invalid Probability (1.5)    ";
    try {
        GeneticAlgorithm ga(100, 1.5, 0.05, 100, generator);
        std::cout << "FAILED (Constructor should throw)" << std::endl;
    }
    catch (const std::invalid_argument& e) {
        std::cout << "PASSED (Caught: " << e.what() << ")" << std::endl;
    }

    std::cout << "[TEST] Loading non-existent file    ";
    try {
        Evaluator badEval("WrongFolder", false, generator);
        std::cout << "FAILED (Evaluator should throw on bad file)" << std::endl;
    }
    catch (const std::exception& e) {
        std::cout << "PASSED (Caught: " << e.what() << ")" << std::endl;
    }

    std::cout << "[TEST] Evaluator: Genotype size mismatch    ";
    try {
        Evaluator eval("data/lcvrp/Vrp-Set-P/P-n19-k2.lcvrp", false, generator);

        std::vector<int> badGenotype(5, 0);
        double result = eval.evaluate(badGenotype);

        if (result == Evaluator::WRONG_VAL || result == -1.0) {
            std::cout << "PASSED (Returned error value: " << result << ")" << std::endl;
        }
        else {
            std::cout << "FAILED (Returned valid fitness: " << result << ")" << std::endl;
        }
    }
    catch (const std::exception& e) {
        std::cout << "ERROR during test setup: " << e.what() << std::endl;
    }

    std::cout << "[TEST] Evaluator: Invalid group ID    ";
    try {
        Evaluator eval("data/lcvrp/Vrp-Set-P/P-n19-k2.lcvrp", false, generator);
        int size = eval.getGenotypeSize();

        std::vector<int> badGenotype(size, 0);
        badGenotype[0] = 999;

        double result = eval.evaluate(badGenotype);

        if (result == Evaluator::WRONG_VAL || result == -1.0) {
            std::cout << "PASSED (Returned error value: " << result << ")" << std::endl;
        }
        else {
            std::cout << "FAILED (Returned valid fitness: " << result << ")" << std::endl;
        }
    }
    catch (const std::exception& e) {
        std::cout << "ERROR during test setup: " << e.what() << std::endl;
    }
    std::cout << std::endl;
}

int main() {
    std::random_device rd;
    std::mt19937 gen(rd());

    runOptimization("data/lcvrp/Vrp-Set-P/P-n19-k2.lcvrp", 100, 0.6, 0.05, 500, gen);
    std::cout << std::endl;
    runOptimization("data/lcvrp/Vrp-Set-XML100/XML100_1111_11.lcvrp", 100, 0.6, 0.05, 1000, gen);
    std::cout << "\n\n";
    runSafetyTests(gen);
       
    return 0;
}