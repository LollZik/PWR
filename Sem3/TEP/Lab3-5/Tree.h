#pragma once
#include<iostream>
#include "Node.h"
#include<map>
#include "Result.hpp"
#include "Error.hpp"
#include <fstream>

class Tree {

public:
	Tree();
	Tree(std::string& formula);
	Tree(const Tree& other);
	Tree(Tree&& other);
	~Tree();

	Tree operator=(const Tree& other);
	Tree operator=(Tree&& other);
	Tree operator+(const Tree& other) const;

	Result<Tree*, Error> enter(const std::string& formula);
	Result<std::string, Error> vars() const;
	Result<void, Error> print() const;
	Result<double, Error> comp(std::string& values);

	void serialize(std::ostream& os) const;

private:
	Node* root;
	std::map<std::string, int> variables;

	std::vector<std::string> split(const std::string &text, char delimiter);
	Result<Node*, Error> buildTree(const std::vector<std::string> &toks, int &pos);
	void merge(Tree &subTree);
};