#include "Tree.h"
#include <string>

Tree::Tree()
{
    root = nullptr;
}

Tree::Tree(std::string& formula) {
    root = nullptr;
    Result<Tree*, Error> res = enter(formula);
}

Tree::Tree(const Tree& other):
    variables(other.variables)
{
    if (other.root != nullptr) {
        root = other.root->deepCopy();
    }
    else {
        root = nullptr;
    }
}

Tree::Tree(Tree&& other):
    root(other.root), variables(std::move(other.variables))
{
    other.root = nullptr;
}

Tree::~Tree()
{
    if (root != nullptr) delete root;
}

Tree Tree::operator=(const Tree& other)
{
    if (this == &other) { 
        return *this;
    }
    if (root != nullptr) {
        delete root;
    }
    root = nullptr;
    variables = other.variables;

    if (other.root != nullptr) {
        root = other.root->deepCopy();
    }

    return *this;
}

Tree Tree::operator=(Tree&& other){
    if (this == &other) {
        return *this;
    }

    delete root;
    root = std::exchange(other.root, nullptr);
    /*root = other.root;
    other.root = nullptr;*/
    variables = std::move(other.variables);

    return *this;
}

Tree Tree::operator+(const Tree& other) const
{
    Tree result = Tree(*this);
    Tree subTree = Tree(other);
    
    result.merge(subTree);
    return result;
}

Result<Tree*, Error> Tree::enter(const std::string& formula) {
    std::vector<std::string> tokens = split(formula, ' ');
    int pos = 0;
    Result<Node*, Error> res = buildTree(tokens, pos);

    if (res.isSuccess()) {
        if (pos < tokens.size()) {
            delete res.getValue();
            return Result<Tree*, Error>::fail(new Error("Too many arguments provided."));
        }

        if (root != nullptr) {
            delete root;
        }
        root = res.getValue();

        variables.clear();
        if (root != nullptr) {
            root->collectVariables(variables);
        }
        return Result<Tree*, Error>::ok(this);
    }
    else {
        return Result<Tree*, Error>::fail(res.getErrors());
    }
}

Result<std::string, Error> Tree::vars() const {
    if (root == nullptr) {
        return Result<std::string, Error>::fail(new Error("Tree doesn't exist."));
    }
    
    std::string result;
    for (std::map<std::string, int>::const_iterator it = variables.begin(); it != variables.end(); ++it) {
        result += it->first + " ";
    }

    return Result<std::string, Error>::ok(result);
}

Result<void, Error> Tree::print() const{
    if (root == nullptr) {
        return Result<void, Error>::fail(new Error("Tree is empty."));
    }

    root->printSubTree();
    std::cout << std::endl;

    return Result<void, Error>::ok();
}

Result<double, Error> Tree::comp(std::string& values) {
    if (root == nullptr) {
        return Result<double, Error>::fail(new Error("Tree doesn't exist - Can't compute."));
    }

    std::vector<std::string> v = split(values, ' ');

    if (v.size() != variables.size()) {
        std::string msg = "Wrong number of variables. Expected " + std::to_string(variables.size()) + " argument(s).";
        return Result<double, Error>::fail(new Error(msg));
    }

    for (std::string sign : v) {
        for (char c : sign) {
            if (!(c <= '9' && c >= '0')) {
                return Result<double, Error>::fail(new Error("Variable argument is not a number."));
            }
        }
    }
 
    std::map<std::string, int> arguments = variables;
    int i = 0;

    for (std::map<std::string, int>::iterator it = arguments.begin(); it != arguments.end(); ++it) {
        it->second = std::stoi(v[i++]);
    }
    return root->calculateSubTree(arguments);
}

void Tree::serialize(std::ostream& os) const{
    if (root != nullptr) {
        root->serialize(os);
    }
}

std::vector<std::string> Tree::split(const std::string &text, char delimiter) {
    std::vector<std::string> result;
    if (text.empty()) {
        return result;
    }
    std::string current;

    for (char c : text) {
        if (c == delimiter) {
            if (current != "") {
                result.push_back(current);
            }
            current.clear();
        }
        else {
            current += c;
        }
    }

    if (current != "") {
        result.push_back(current);
    }
    return result;
}

Result<Node*, Error> Tree::buildTree(const std::vector<std::string> &toks, int &pos) {
    if (toks.empty() || pos >= toks.size()) {
        return Result<Node*, Error>::fail(new Error("Not enough arguments."));
    }

    std::string t = toks[pos++];
    Node* node = new Node(t);
    int n = node->getChildSize();

    for (int i = 0; i < n; i++){
        Result<Node*, Error> childRes = buildTree(toks, pos);

        if (!childRes.isSuccess()) {
            delete node;
            return Result<Node*, Error>::fail(childRes.getErrors());
        }
        node->addChild(childRes.getValue());
    }

    if (node->getType() == Node::VAR) {
        if (variables.find(t) == variables.end()) { // If not found
            variables[node->getToken()] = -1;
        }
    }
    return Result<Node*, Error>::ok(node);
}

void Tree::merge(Tree &subTree) {
    if (root == nullptr || root->getType() != Node::OP) {
        delete root;
        root = subTree.root;
    }
    else {
        if (subTree.root == nullptr) {
            return;
        }
        root->joinTrees(subTree.root);
    }

    subTree.root = nullptr;
    if (root != nullptr) {
        variables.clear();
        root->collectVariables(variables);
    }
}