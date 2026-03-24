#pragma once

#include "ProblemData.hpp"
#include <vector>
#include <random>


namespace LcVRPContest {
	class Evaluator {
	public:
		static constexpr double WRONG_VAL = -1.0;

		Evaluator(ProblemData& problem_data, std::mt19937& generator);
		Evaluator(const std::string& filePath, bool useRandomPermutation, std::mt19937& gen);

		double evaluate(const std::vector<int>& genotype);
		void loadInstance(const std::string& filePath, bool useRandomPermutation);

		int getGenotypeSize() const;
		int getNumberOfGroups() const;

		const ProblemData& getProblemData() const;
	private:
		ProblemData problemData;
		std::mt19937& gen;

		bool processCustomerVisit(int group, int customerId, std::vector<int>& loads, std::vector<int>& prevNodes, std::vector<double>& distances);
		bool returnToDepot(int numGroups, const std::vector<int>& prevNodes, std::vector<double>& distances);
	};
}
