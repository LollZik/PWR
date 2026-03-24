#pragma once

#include <vector>
#include <stdexcept>

namespace LcVRPContest {

    struct Coordinate {
        double x;
        double y;

        Coordinate() : x(0.0), y(0.0) {}
        Coordinate(double newX, double newY) : x(newX), y(newY) {}
    };

    class ProblemData {
    public:
        ProblemData();

        double CalculateDistance(int i, int j) const;
        void BuildEdgeWeightMatrix();

        static constexpr double WRONG_VAL = -1.0;

        // Getters & Setters
        std::string getName() const;
        int getDimension() const;
        int getCapacity() const;
        std::string getEdgeWeightType() const;
        int getDepot() const;
        int getNumCustomers() const;
        int getNumGroups() const;

        const std::vector<Coordinate>& getCoordinates() const;
        const std::vector<int>& getDemands() const;
        const std::vector<int>& getPermutation() const;
        const std::vector<std::vector<double>>& getEdgeWeights() const;

        void setName(const std::string& newName);
        void setDimension(int newDimension);
        void setCapacity(int newCapacity);
        void setEdgeWeightType(const std::string& newType);
        void setDepot(int newDepot);
        void setNumGroups(int newNumGroups);
        void setCoordinates(const std::vector<Coordinate>& newCoordinates);
        void setDemands(const std::vector<int>& newDemands);
        void setPermutation(const std::vector<int>& newPermutation);
        void setEdgeWeights(const std::vector<std::vector<double>>& newEdgeWeights);

    private:
        std::string name;
        int dimension;
        int capacity;
        std::string edgeWeightType;
        int depot;
        int numGroups;

        std::vector<Coordinate> coordinates;
        std::vector<int> demands;
        std::vector<int> permutation;
        std::vector<std::vector<double>> edgeWeights;
    };
}