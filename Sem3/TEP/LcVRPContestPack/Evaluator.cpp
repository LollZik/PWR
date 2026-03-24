#include "Evaluator.hpp"
#include "ProblemLoader.hpp"
#include "ProblemData.hpp"
#include <algorithm>
#include <numeric>

using namespace LcVRPContest;

Evaluator::Evaluator(ProblemData& problem_data, std::mt19937& generator)
	: problemData(problem_data), gen(generator)
{}

Evaluator::Evaluator(const std::string& filePath, bool useRandomPermutation, std::mt19937& generator)
	:gen(generator) {
	ProblemLoader pl = ProblemLoader(filePath, useRandomPermutation, gen);
	problemData = pl.LoadProblem();
}


double Evaluator::evaluate(const std::vector<int>& genotype) {

	if (genotype.size() != problemData.getNumCustomers()) {
		return WRONG_VAL;
	}
	int numGroups = problemData.getNumGroups();


	std::vector<int> currentLoads(numGroups, 0);
	std::vector<int> previousNodes(numGroups, problemData.getDepot() - 1);
	std::vector<double> distances(numGroups, 0.0);

	const std::vector<int>& permutation = problemData.getPermutation();

	for (int customerId : permutation) {
		int genotypeIndex = customerId - 2;

		if (genotypeIndex < 0 || genotypeIndex >= genotype.size()) {
			return WRONG_VAL;
		}

		int group = genotype[genotypeIndex];

		if (group < 0 || group >= numGroups) {
			return WRONG_VAL;
		}

		if (!processCustomerVisit(group, customerId, currentLoads, previousNodes, distances)) {
			return WRONG_VAL;
		}
	}

	if (!returnToDepot(numGroups, previousNodes, distances)) {
		return WRONG_VAL;
	}

	double totalCost = 0.0;
	for (double dist : distances) {
		totalCost += dist;
	}

	return totalCost;
}

bool Evaluator::processCustomerVisit(int group, int customerId, std::vector<int>& loads,
	std::vector<int>& prevNodes, std::vector<double>& distances)
{
	int capacity = problemData.getCapacity();
	int depotIndex = problemData.getDepot();

	int currentCustomerIndex = customerId - 1;
	int demand = problemData.getDemands()[currentCustomerIndex];

	if (loads[group] + demand > capacity) {
		// Capacity limit would be exceeded, go back to depot to "reload" the truck
		double d1 = problemData.CalculateDistance(prevNodes[group], depotIndex);
		double d2 = problemData.CalculateDistance(depotIndex, currentCustomerIndex);

		if (d1 < 0 || d2 < 0) { 
			return false; 
		}

		distances[group] += (d1 + d2);
		loads[group] = 0;
	}

	else {
		double d = problemData.CalculateDistance(prevNodes[group], currentCustomerIndex);
		if (d < 0) return false;

		distances[group] += d;
	}

	loads[group] += demand;
	prevNodes[group] = currentCustomerIndex;

	return true;
}

bool Evaluator::returnToDepot(int numGroups, const std::vector<int>& prevNodes, std::vector<double>& distances){
	int depotIndex = 0;

	for (int i = 0; i < numGroups; ++i) {
		if (prevNodes[i] != depotIndex) {
			double retDist = problemData.CalculateDistance(prevNodes[i], depotIndex);

			if (retDist < 0){
				return false;
			}

			distances[i] += retDist;
		}
	}
	return true;
}

void Evaluator::loadInstance(const std::string& filePath, bool useRandomPermutation) {
	ProblemLoader loader(filePath, useRandomPermutation, gen);
	problemData = loader.LoadProblem();
}

const ProblemData& Evaluator::getProblemData() const {
	return problemData;
}

int Evaluator::getGenotypeSize() const {
	return problemData.getNumCustomers();
}

int Evaluator::getNumberOfGroups() const {
	return problemData.getNumGroups();
}