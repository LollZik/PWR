#include "TreeInterface.h"

TreeInterface::TreeInterface() {
	tree = Tree();
}

TreeInterface::TreeInterface(Tree &t) {
	tree = t;
}

void TreeInterface::gatherInput() {
	do {
		std::cout << ">Enter a command (type 'exit' to leave)\n>";
		std::cin >> command;

		if (command == "print") {
			Result<void, Error> res = tree.print();

			if (!res.isSuccess()) {
				std::cout << "Error: " << res.getErrors()[0]->getError() << std::endl;
			}
		}

		else if (command == "vars") {
			Result<std::string, Error> res = tree.vars();

			if (res.isSuccess()) {
				std::cout << "Vars: " << res.getValue() << std::endl;
			}
			else {
				std::cout << "Error: " << res.getErrors()[0]->getError() << std::endl;
			}
		}

		else {
			getline(std::cin, argument);
			if (command == "enter") {
				Result<Tree*, Error> res = tree.enter(argument);
				if (res.isSuccess()) {
					std::cout << "Tree created successfully." << std::endl;
				}
				else {
					std::cout << "Failed to create tree:" << std::endl;
					std::vector<Error*>& errs = res.getErrors();
					for (int i = 0; i < errs.size(); ++i) {
						std::cout << " - " << errs[i]->getError() << std::endl;
					}
				}
			}
			else if (command == "join") {
				tree = tree + Tree(argument);
			}
			else if (command == "comp") {
				Result<double, Error> res = tree.comp(argument);
				if (res.isSuccess()) {
					std::cout << "Result: " << res.getValue() << std::endl;
				}
				else {
					std::cout << "Computation failed:" << std::endl;
					std::vector<Error*>& errs = res.getErrors();

					for (int k = 0; k < errs.size(); ++k) {
						std::cout << " - " << errs[k]->getError() << std::endl;
					}
				}
			}
			else if (command != "exit") {
				std::cout << "\nInvalid command! Try again.\n";
			}
		}
	} while (command != "exit");
}