#include "ProblemLoader.hpp"
#include <fstream>
#include <sstream>
#include <iostream>
#include <algorithm>
#include <stdexcept>

using namespace LcVRPContest;

ProblemLoader::ProblemLoader(const std::string& filePath, bool randomPermutation, std::mt19937& generator)
    : filePath(filePath), useRandomPermutation(randomPermutation), gen(generator)
{
}

ProblemData ProblemLoader::LoadProblem() {
    std::ifstream file(filePath);
    if (!file.is_open()) {
        throw std::runtime_error("Can't open the file: " + filePath);
    }
    ProblemData data;
    std::string token;

    bool hasDim = false;
    bool hasEdgeType = false;
    bool hasCap = false;
    bool hasGroups = false;
    bool hasPerm = false;
    bool hasDem = false;
    bool hasDepot = false;

    bool hasNodeData = false;

    
    while (file >> token){
        if (token == "NAME" || token == "NAME:") {
            std::string value;
            file >> value;

            if (value == ":") {
                file >> value;
            }
            data.setName(value);
        }
        else if (token == "DIMENSION" || token == "DIMENSION:") {
            std::string value;
            file >> value;

            if (value == ":") {
                file >> value;
            }

            try {
                int dim = std::stoi(value);
                if (dim < 2) throw std::runtime_error("DIMENSION needs to be >= 2");
                data.setDimension(dim);
                hasDim = true;
            }
            catch (...) {
                throw std::runtime_error("DIMENSION parsing error.\n");
            }
        }
        else if (token == "CAPACITY" || token == "CAPACITY:") {
            std::string value;
            file >> value;
            if (value == ":") file >> value;

            try {
                int cap = std::stoi(value);
                if (cap <= 0) throw std::runtime_error("CAPACITY has to be a positive number.");
                data.setCapacity(cap);
                hasCap = true;
            }
            catch (...) {
                throw std::runtime_error("CAPACITY parsing error.\n");
            }
        }
        else if (token == "NODE_COORD_SECTION") {
            if (!hasDim) throw std::runtime_error("DIMENSION section is missing.\n");
            loadNodeCoords(file, data);
            hasNodeData = true;
        }
        else if (token == "DEMAND_SECTION") {
            if (!hasDim) throw std::runtime_error("DIMENSION section is missing.\n");
            loadDemands(file, data);
            hasDem = true;
        }
        else if (token == "DEPOT_SECTION") {
            loadDepot(file, data);
            hasDepot = true;
        }
        else if (token == "EDGE_WEIGHT_SECTION") {
            if (!hasDim) throw std::runtime_error("DIMENSION section is missing.\n");
            loadEdgeWeights(file, data);
            hasNodeData = true;
        }

        else if (token == "NUM_GROUPS" || token == "NUM_GROUPS:") {
            std::string value;
            file >> value;
            if (value == ":") file >> value;
            try {
                int groups =std::stoi(value);
                if (groups < 1) throw std::runtime_error("NUM_GROUPS has to be >= 1\n");
                data.setNumGroups(groups);
                hasGroups = true;
            }
            catch (...) { throw std::runtime_error("NUM_GROUPS parsing error.\n");
            }
        }

        else if (token == "PERMUTATION" || token == "PERMUTATION:") {
            if (!hasDim) throw std::runtime_error("DIMENSION section must appear before PERMUTATION.\n");

            char nextChar = file.peek();
            while (isspace(nextChar)) { 
                file.get();
                nextChar = file.peek();
            }

            if (nextChar == ':') {
                std::string dump;
                file >> dump;
            }

            int numCustomers = data.getNumCustomers();
            std::vector<int> permFromFile;
            permFromFile.reserve(numCustomers);

            for (int i = 0; i < numCustomers; ++i) {
                int val;
                if (!(file >> val)) {
                    throw std::runtime_error("Error reading PERMUTATION section. Expected " + std::to_string(numCustomers) + " integers.");
                }
                permFromFile.push_back(val);
            }
            if (useRandomPermutation) {
                int customers = data.getNumCustomers();
                std::vector<int> perm;
                for (int i = 0; i < customers; ++i) {
                    perm.push_back(i + 2); // First client's ID is 2
                }

              
                std::shuffle(perm.begin(), perm.end(), gen);
                data.setPermutation(perm);
            }
            else {
                data.setPermutation(permFromFile);
            }
            hasPerm = true;
        }

        else if (token == "EDGE_WEIGHT_TYPE" || token == "EDGE_WEIGHT_TYPE:") {
            std::string type;
            file >> type;
            if (type == ":") file >> type;
            data.setEdgeWeightType(type);
            hasEdgeType = true;
        }

        else if (token == "EOF") {
            break;
        }
    }
    file.close();
    if (!hasDim) throw std::runtime_error("No DIMENSION section in the file.\n");
    if (!hasCap) throw std::runtime_error("No CAPACITY section in the file.\n");

    if (data.getEdgeWeightType() == "EUC_2D") {
        data.BuildEdgeWeightMatrix();
    }

  
    else {
        int customers = data.getNumCustomers();
        std::vector<int> perm;
        for (int i = 0; i < customers; ++i) {
            perm.push_back(i + 2);
        }
        data.setPermutation(perm);
    }
    if (!(hasDim && hasEdgeType && hasCap && hasGroups && hasPerm && hasDem && hasDepot && hasNodeData)) {
        throw std::runtime_error("Not enough data is the file.\n");
    }

    return data;
}

void ProblemLoader::loadNodeCoords(std::ifstream& file, ProblemData& data) {
    int dim = data.getDimension();
    std::vector<Coordinate> coords(dim);

    for (int i = 0; i < dim; ++i) {
        int id;
        double x, y;
        if (!(file >> id >> x >> y)) throw std::runtime_error("Data error in NODE_COORD_SECTION.\n");

        if (id < 1 || id > dim) throw std::runtime_error("ID is out of it's scope: " + std::to_string(id));
        
        coords[id - 1] = Coordinate(x, y);
    }
    data.setCoordinates(coords);
}

void ProblemLoader::loadDemands(std::ifstream& file, ProblemData& data) {
    int dim = data.getDimension();
    std::vector<int> demands(dim);

    for (int i = 0; i < dim; ++i) {
        int id, demand;
        if (!(file >> id >> demand)) throw std::runtime_error("Data error in DEMAND_SECTION.\n");

        if (id < 1 || id > dim) throw std::runtime_error("ID out of scope in DEMAND section.\n");
        if (demand < 0) throw std::runtime_error("Negative demand values are not allowed.\n");

        demands[id - 1] = demand;
    }
    data.setDemands(demands);
}

void ProblemLoader::loadEdgeWeights(std::ifstream& file, ProblemData& data) {
    int dim = data.getDimension();
    std::vector<std::vector<double>> matrix(dim, std::vector<double>(dim));

    for (int i = 1; i < dim; ++i) {
        for (int j = 0; j < i; ++j) {
            double val;
            if (!(file >> val)) throw std::runtime_error("Not enought data found in EDGE_WEIGHT_SECTION.\n");

            matrix[i][j] = val;
            matrix[j][i] = val;
        }
    }
    data.setEdgeWeights(matrix);
}

void ProblemLoader::loadDepot(std::ifstream& file, ProblemData& data){
    int depot;
    file >> depot;
    if (file.fail()) throw std::runtime_error("Invalid depot ID.\n");
    data.setDepot(depot);

    std::string next;
    file >> next; // -1
}
