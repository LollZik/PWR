#pragma once

#include <vector>
#include "Individual.hpp"
#include "Evaluator.hpp"
#include <random>

namespace LcVRPContest {
	class GeneticAlgorithm {
	public:

		GeneticAlgorithm(int popSize, double crossProb, double mutProb, int maxIterations, std::mt19937& generator);
		~GeneticAlgorithm();

		void run(Evaluator& evaluator);

		const Individual* getBestSolution() const;

	private:
		int popSize;
		double crossProb;
		double mutProb;
		int maxIterations;

		Individual* bestSolution;
		std::vector<Individual> population;
		std::mt19937& gen;

		void initializePopulation(int genotypeSize, int numberOfGroups);
		void evaluatePopulation(Evaluator& evaluator);
		void evolvePopulation();

		const Individual& selectParent(const std::vector<Individual>& currentPop);

		void updateBestSolution(const Individual& candidate);
	};
}