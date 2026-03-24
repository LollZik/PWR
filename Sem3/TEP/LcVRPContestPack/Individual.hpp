#pragma once
#include "Evaluator.hpp"
#include <vector>
#include <utility>
#include <random>

namespace LcVRPContest {
	class Individual {
	public:
		Individual(int genotypeSize, int numGroups, std::mt19937& generator);
		Individual(const Individual& other);
		Individual(Individual&& other);

		Individual& operator=(const Individual& other);
		Individual& operator=(Individual&& other);

		double calculateFitness(Evaluator& evaluator);
		bool mutate(double mutationProbability);

		std::pair<Individual, Individual> crossover(const Individual& otherParent, double crossoverProbability) const;

		const std::vector<int>& getGenotype() const;
		double getFitness() const;

	private:
		Individual(const std::vector<int>& newGenotype, int numberOfGroups, std::mt19937& generator);

		std::mt19937& gen;
		std::vector<int> genotype;
		int numberOfGroups;
		double fitness;
	};
}