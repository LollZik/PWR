#include "GeneticAlgorithm.hpp"
#include <iostream>
#include <algorithm>
#include <random>

using namespace LcVRPContest;


GeneticAlgorithm::GeneticAlgorithm(int popSize, double crossProb, double mutProb, int maxIterations, std::mt19937& generator)
    : popSize(popSize), crossProb(crossProb), mutProb(mutProb), maxIterations(maxIterations), gen(generator)
{
    bestSolution = nullptr;
    if (popSize <= 0) {
        throw std::invalid_argument("Population size must be > 0.");
    }

    if (this->popSize % 2 != 0) {
        this->popSize++;
    }

    if (crossProb < 0.0 || crossProb > 1.0 || mutProb < 0.0 || mutProb > 1.0) {
        throw std::invalid_argument("Probabilities must be in range [0.0, 1.0].");
    }

    if (maxIterations <= 0) {
        throw std::invalid_argument("Max iterations must be > 0.");
    }
}

GeneticAlgorithm::~GeneticAlgorithm() {
    if (bestSolution != nullptr) {
        delete bestSolution;
    }
}

void GeneticAlgorithm::run(Evaluator& evaluator) {
    if (bestSolution != nullptr) {
        delete bestSolution;
        bestSolution = nullptr;
    }
    initializePopulation(evaluator.getGenotypeSize(), evaluator.getNumberOfGroups());
    evaluatePopulation(evaluator);
    for (const Individual& ind : population) { // Find first generation's best solution
        updateBestSolution(ind);
    }

    for (int i = 0; i < maxIterations; ++i) {
        evolvePopulation();
        evaluatePopulation(evaluator);
        for (const Individual& ind : population) {
            updateBestSolution(ind);
        }
    }

}

const Individual* GeneticAlgorithm::getBestSolution() const {
    return bestSolution;
}

void GeneticAlgorithm::initializePopulation(int genotypeSize, int numberOfGroups) {
    population.clear();
    population.reserve(popSize);

    for (int i = 0; i < popSize; ++i) { 
        population.emplace_back(genotypeSize, numberOfGroups, gen);
    }
}

void GeneticAlgorithm::evaluatePopulation(Evaluator& evaluator) {
    for (Individual& ind : population) {
        ind.calculateFitness(evaluator);
    }
}

const Individual& GeneticAlgorithm::selectParent(const std::vector<Individual>& currentPop) {
    std::uniform_int_distribution<> dis(0, (int)currentPop.size() - 1);

    int i1 = dis(gen);
    int i2 = dis(gen);

    const Individual& ind1 = currentPop[i1];
    const Individual& ind2 = currentPop[i2];

    return ind1.getFitness() < ind2.getFitness() ? ind1 : ind2;
}

void GeneticAlgorithm::evolvePopulation() {
    std::vector<Individual> newPopulation;
    newPopulation.reserve(popSize);

    while (newPopulation.size() < (size_t)popSize) {
        const Individual& parent1 = selectParent(population);
        const Individual& parent2 = selectParent(population);

        std::pair<Individual, Individual> children = parent1.crossover(parent2, crossProb);

        children.first.mutate(mutProb);
        children.second.mutate(mutProb);

        newPopulation.push_back(children.first);
        newPopulation.push_back(children.second);
    }

    population.swap(newPopulation);
}

void GeneticAlgorithm::updateBestSolution(const Individual& candidate) {
    if (bestSolution == nullptr) {
        bestSolution = new Individual(candidate);
    }
    else if (candidate.getFitness() < bestSolution->getFitness()) {
        delete bestSolution;
        bestSolution = new Individual(candidate);
    }
}