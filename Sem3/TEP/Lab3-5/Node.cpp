#include "Node.h"

Node::Node(std::string tokenValue)
	: token(tokenValue)
{
	type = determineType();
	children = std::vector<Node*>(getChildSize(), nullptr);
}

Node::Node(const Node& other){
	token = other.token;
	type = other.type;
	children = std::vector<Node*>(other.children.size(), nullptr);
	for (int i = 0; i < other.children.size(); i++){
		children[i] = new Node(*other.children[i]);
	}
}

Node::~Node(){
	for (int i = 0; i < children.size(); i++){
		if (children[i] != nullptr) {
			delete children[i];
			children[i] = nullptr;
		}
	}
	children.clear();
}

void Node::printSubTree() const{
	std::cout << token << " ";
	for (Node* child : children) {
		child->printSubTree();
	}
}

Node* Node::deepCopy() const {
	return new Node(*this);
}

Result<double, Error> Node::calculateSubTree(const std::map<std::string, int> &variables) {
	if (type == NUM) {
		return Result<double, Error>::ok(std::stoi(token));
	}

	if (type == OP) {
		Result<double, Error> r1 = children[0]->calculateSubTree(variables);
		if (!r1.isSuccess()) return Result<double, Error>::fail(r1.getErrors());
		double val1 = r1.getValue();

		if (token == "sin") return Result<double, Error>::ok(sin(val1));
		if (token == "cos") return Result<double, Error>::ok(cos(val1));

		Result<double, Error> r2 = children[1]->calculateSubTree(variables);
		if (!r2.isSuccess()) return Result<double, Error>::fail(r2.getErrors());
		double val2 = r2.getValue();

		if (token == "+") return Result<double, Error>::ok(val1 + val2);
		if (token == "-") return Result<double, Error>::ok(val1 - val2);
		if (token == "*") return Result<double, Error>::ok(val1 * val2);

		if (token == "/") {
			if (val2 == 0) {
				return Result<double, Error>::fail(new Error("Division by zero"));
			}
			return Result<double, Error>::ok(val1 / val2);
		}
	}
	// variable
	if (variables.find(token) == variables.end()) {
		return Result<double, Error>::fail(new Error("Variable not found: " + token));
	}
	return Result<double, Error>::ok(variables.find(token)->second);
}

void Node::joinTrees(Node* subTreeRoot){
	if (type == OP){
		if (children[0]->type == OP) {
			children[0]->joinTrees(subTreeRoot);
			return;
		}
		// 1st child is a leaf
		delete children[0];
		children[0] = subTreeRoot;
		return;
	}

}

void Node::addChild(Node* child) {
	if (type != OP) return;
	for (int i = 0; i < children.size(); ++i) {
		if (children[i] == nullptr) {
			children[i] = child;
			return;
		}
	}
	std::cout << "Warning: This token can't have more children: " << token << std::endl;
}

void Node::collectVariables(std::map<std::string, int>& out) const {
	if (type == VAR) {
		out[token] = -1;
	}
	for (int i = 0; i < children.size(); ++i) {
		if (children[i] != nullptr) {
			children[i]->collectVariables(out);
		}
	}
}

void Node::serialize(std::ostream& os) const {
	os << token << " ";
	for (int i = 0; i < children.size(); ++i) {
		if (children[i] != nullptr) {
			children[i]->serialize(os);
		}
	}
}

int Node::getChildSize() const {
	if (type == NUM || type == VAR) { return 0; }
	if (token == "sin" || token == "cos") { return 1; }
	return 2;
}

Node::Type Node::getType() const {
	return type;
}

std::string Node::getToken() const {
	return token;
}

Node::Type Node::determineType(){
	if (token == "+" || token == "-" ||  token == "*" || token == "/" || token == "sin" || token == "cos"){
		return OP;
	}
	bool isNum = true;
	char c;
	for (int i = 0; i < token.length(); i++){
		c = token[i];
		if (!(c >= '0' && c <= '9')){
			isNum = false;
			if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))){
				std::cout << "Illegal char found in token:" << c << ".Said char will be ignored."<< std::endl;
				token.erase(i, 1);
				i--;
			}
		}
	}

	if (token.length() == 0){
		std::cout << "After deletion of illegal signs the token is empty. It will be added as 'x'." << std::endl;
		token = "x";
		return VAR;
	}

	return isNum ? NUM : VAR;
}
