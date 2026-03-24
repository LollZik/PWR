#include "Individual.hpp"
#include <random>
#include <algorithm>

using namespace LcVRPContest;


Individual::Individual(int genotypeSize, int numGroups, std::mt19937& generator)
    : numberOfGroups(numGroups), fitness(-1.0), gen(generator)
{
    std::uniform_int_distribution<> dis(0, numberOfGroups - 1);

    genotype.resize(genotypeSize);
    for (int& gene : genotype) {
        gene = dis(gen);
    }
}

Individual::Individual(const Individual& other)
    : genotype(other.genotype), numberOfGroups(other.numberOfGroups), fitness(other.fitness), gen(other.gen)
{
}

Individual::Individual(const std::vector<int>& newGenotype, int numGroups, std::mt19937& generator)
    : genotype(newGenotype), numberOfGroups(numGroups), fitness(-1.0), gen(generator)
{
}

Individual::Individual(Individual&& other)
    : genotype(std::move(other.genotype)), numberOfGroups(other.numberOfGroups), fitness(other.fitness), gen(other.gen)
{
    other.fitness = -1.0;
}

Individual& Individual::operator=(const Individual& other) {
    if (this != &other) {
        genotype = other.genotype;
        numberOfGroups = other.numberOfGroups;
        fitness = other.fitness;
        gen = other.gen;
    }
    return *this;
}

Individual& Individual::operator=(Individual&& other) {
    if (this != &other) {
        genotype = std::move(other.genotype);
        numberOfGroups = other.numberOfGroups;
        fitness = other.fitness;
        gen = std::move(other.gen);
    }
    return *this;
}

double Individual::calculateFitness(Evaluator& evaluator) {
    if (fitness != -1.0) {
        return fitness;
    }
    fitness = evaluator.evaluate(genotype);
    return fitness;
}

bool Individual::mutate(double mutationProbability) {
    std::uniform_real_distribution<> probDist(0.0, 1.0);
    std::uniform_int_distribution<> groupDist(0, numberOfGroups - 1);

    bool mutated = false;

    for (int& gene : genotype) {
        if (probDist(gen) < mutationProbability) {
            gene = groupDist(gen);
            mutated = true;
        }
    }

    if (mutated) {
        fitness = -1.0;
    }

    return mutated;
}

std::pair<Individual, Individual> Individual::crossover(const Individual& otherParent, double crossoverProbability) const {
    if (genotype.size() < 2) {
        return std::make_pair(*this, otherParent);
    }

    std::uniform_real_distribution<> probDist(0.0, 1.0);

    if (probDist(gen) >= crossoverProbability) {
        return std::make_pair(*this, otherParent);
    }

    std::uniform_int_distribution<> cutDist(1, (int)genotype.size() - 1);
    int cutPoint = cutDist(gen);

    std::vector<int> child1Genotype;
    std::vector<int> child2Genotype;

    child1Genotype.reserve(genotype.size());
    child2Genotype.reserve(genotype.size());

    for (int i = 0; i < cutPoint; ++i) {
        child1Genotype.push_back(this->genotype[i]);
        child2Genotype.push_back(otherParent.genotype[i]);
    }

    for (size_t i = cutPoint; i < genotype.size(); ++i) {
        child1Genotype.push_back(otherParent.genotype[i]);
        child2Genotype.push_back(this->genotype[i]);
    }

    return std::make_pair(
        Individual(child1Genotype, numberOfGroups, gen),
        Individual(child2Genotype, numberOfGroups, gen)
    );
}

const std::vector<int>& Individual::getGenotype() const {
    return genotype;
}

double Individual::getFitness() const {
    return fitness;
}