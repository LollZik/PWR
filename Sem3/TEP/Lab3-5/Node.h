#pragma once
#include <iostream>
#include <vector>
#include<map>
#include<string>
#include "Result.hpp"
#include "Error.hpp"
class Node {

public:
	enum Type { OP, NUM, VAR };
	Node(std::string tokenValue);
	Node(const Node &other);
	~Node();

	void printSubTree() const;
	Node* deepCopy() const;
	Result<double, Error> calculateSubTree(const std::map<std::string, int> &variables);
	void joinTrees(Node* subTree);
	void addChild(Node* child);
	void collectVariables(std::map<std::string, int>& out) const;

	void serialize(std::ostream& os) const;

	int getChildSize() const;
	Type getType() const;
	std::string getToken() const;

private:
	std::string token;
	std::vector <Node*> children;
	Type type;
	Type determineType();
};
