#pragma once

#include "ProblemData.hpp"
#include <string>
#include <random>

namespace LcVRPContest {
	class ProblemLoader {
	public:
		ProblemLoader(const std::string& filePath, bool randomPermutation, std::mt19937& generator);
		ProblemData LoadProblem();

	private:
		std::mt19937& gen;
		std::string filePath;
		bool useRandomPermutation;

		void loadNodeCoords(std::ifstream& file, ProblemData& data);
		void loadDemands(std::ifstream& file, ProblemData& data);
		void loadDepot(std::ifstream& file, ProblemData& data);
		void loadEdgeWeights(std::ifstream& file, ProblemData& data);
	};
}
