#include "ProblemData.hpp"
#include <iostream>

using namespace LcVRPContest;

ProblemData::ProblemData()
	: dimension(0),
      capacity(0),
      depot(1),
      numGroups(0) {}

double ProblemData::CalculateDistance(int i, int j) const {
    if (i < 0 || i >= dimension || j < 0 || j >= dimension) {
        return WRONG_VAL;
    }

    if (i == j) {
        return 0.0;
    }

    if (!edgeWeights.empty()) {
        return  edgeWeights[i][j];
    }
    
    return WRONG_VAL;
}

void ProblemData::BuildEdgeWeightMatrix() {
    if (edgeWeightType == "EUC_2D") {
        if (coordinates.size() != dimension){
            return;
        }

        edgeWeights.assign(dimension, std::vector<double>(dimension));

        for (int i = 0; i < dimension; ++i) {
            edgeWeights[i][i] = 0.0;
            for (int j = 0; j < i; ++j) {
                double dx = coordinates[i].x - coordinates[j].x;
                double dy = coordinates[i].y - coordinates[j].y;
                double dist = sqrt(dx * dx + dy * dy);

                edgeWeights[i][j] = dist;
                edgeWeights[j][i] = dist;
            }
        }
    }
}

std::string ProblemData::getName() const { return name; }
int ProblemData::getDimension() const { return dimension; }
int ProblemData::getCapacity() const { return capacity; }
std::string ProblemData::getEdgeWeightType() const { return edgeWeightType; }
int ProblemData::getDepot() const { return depot; }
int ProblemData::getNumCustomers() const { return dimension - 1; }
int ProblemData::getNumGroups() const { return numGroups; }
const std::vector<Coordinate>& ProblemData::getCoordinates() const { return coordinates; }
const std::vector<int>& ProblemData::getDemands() const { return demands; }
const std::vector<int>& ProblemData::getPermutation() const { return permutation; }
const std::vector<std::vector<double>>& ProblemData::getEdgeWeights() const { return edgeWeights; }


void ProblemData::setDimension(int newDimension) {
    if (newDimension < 2) throw std::invalid_argument("Dimension must be >= 2.");
    dimension = newDimension;
    coordinates.resize(dimension);
    demands.resize(dimension);
}

void ProblemData::setName(const std::string& newName) {
    if (newName.empty()) {
        throw std::invalid_argument("Problem name cannot be empty.");
    }
    name = newName;
}

void ProblemData::setCapacity(int newCapacity) {
    if (newCapacity <= 0) {
        throw std::invalid_argument("Capacity must be positive.");
    }
    capacity = newCapacity; 
}

void ProblemData::setEdgeWeightType(const std::string& newType) { edgeWeightType = newType; }

void ProblemData::setDepot(int newDepot) {
    if (newDepot < 1) { 
        throw std::invalid_argument("Depot ID must be >= 1."); 
    }
    depot = newDepot; 
}

void ProblemData::setNumGroups(int newNumGroups) { 
    if (newNumGroups < 1) { 
        throw std::invalid_argument("NumGroups must be >= 1."); 
    }
    numGroups = newNumGroups;
}

void ProblemData::setCoordinates(const std::vector<Coordinate>& newCoordinates) {
    if (newCoordinates.size() != dimension) {
        throw std::length_error("Coordinates size mismatches dimension.");
    }
    coordinates = newCoordinates;
}

void ProblemData::setDemands(const std::vector<int>& newDemands) {
    if (newDemands.size() != dimension) {
        throw std::length_error("Demands size mismatches dimension.");
    }
    demands = newDemands;
}
void ProblemData::setPermutation(const std::vector<int>& newPermutation) {
    if (newPermutation.size() != (dimension - 1)) {
        throw std::length_error("Permutation size mismatches number of customers.");
    }
    permutation = newPermutation;
}
void ProblemData::setEdgeWeights(const std::vector<std::vector<double>>& newEdgeWeights) {
    if (newEdgeWeights.size() != dimension) {
        throw std::length_error("Edge weights matrix size mismatches dimension.");
    }
    edgeWeights = newEdgeWeights;
}
