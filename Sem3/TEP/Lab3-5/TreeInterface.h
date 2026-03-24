#pragma once
#include <iostream>
#include "Tree.h"
#include "Result.hpp"
#include "Error.hpp"

class TreeInterface {
public:

	TreeInterface();
	TreeInterface(Tree &t);
	void gatherInput();

private:
	std::string command;
	std::string argument;
	Tree tree;
};

