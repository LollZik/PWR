#pragma once
#include <fstream>
#include <string>
#include "Result.hpp"
#include "Error.hpp"
#include "Tree.h"

template <typename T>
class FileSaver {
public:
    static void save(const Result<T, Error>& result, const std::string& filename);
};

template <>
class FileSaver<Tree*> {
public:
    static void save(const Result<Tree*, Error>& result, const std::string& filename);
};

// <T>


template <typename T>
void FileSaver<T>::save(const Result<T, Error>& result, const std::string& filename) {
    std::ofstream file(filename.c_str());
    if (!file.is_open()) return;

    if (!result.isSuccess()) {
        const std::vector<Error*>& errs = result.getErrors();
        for (int i = 0; i < errs.size(); ++i) {
            file << errs[i]->getError() << "\n";
        }
    }
    file.close();
}

// <Tree*>

void FileSaver<Tree*>::save(const Result<Tree*, Error>& result, const std::string& filename) {
    std::ofstream file(filename.c_str());
    if (!file.is_open()) return;

    if (result.isSuccess()) {
        Tree* tree = result.getValue();
        if (tree != nullptr) {
            tree->serialize(file);
        }
    }
    else {
        const std::vector<Error*>& errs = result.getErrors();
        for (int i = 0; i < errs.size(); ++i) {
            file << errs[i]->getError() << "\n";
        }
    }
    file.close();
}