#include <iostream>
#include "Tree.h"
#include "SmartPointer.hpp"
#include "UniquePointer.hpp"


int main() {
	SmartPointer<int> p1(new int(2137));
	SmartPointer<int> p2 = p1;
	std::cout << *p1 << std::endl;
	std::cout << *p2 << std::endl;
	{
		SmartPointer<int> p3 = p1;
		p3 = p2;
		SmartPointer<int[]> p4 = new int[3]{1,2,3};
		std::cout << "W bloku: " << *p3 << std::endl;
		std::cout << p4[2] << std::endl;
		std::cout << p4[1] << std::endl;
		p4 = new int[4] {1, 2, 3, 4};
		std::cout << p4[3] << std::endl;
	}
	std::cout << "Po wyjsciu:" << *p1 << std::endl;
	std::cout << *p2 << "\nModyfikacja:" << std::endl;

	UniquePointer<int> p5 (new int(1337));
	UniquePointer<int> p6 = new int(2);
	std::cout << *p5 << std::endl;
	std::cout << *p6 << std::endl;
	//p5 = p6; <- deleted
	p5 = new int(3);
	std::cout << *p5 << std::endl;

	std::string s1 = "+ a b";
	std::string s2 = "+ c d";

	Tree t1(s1);
	Tree t2(s2);

	Tree t3 = t1 + t2;
	Tree t4;
	t4 = t1 + t2;
    
	return EXIT_SUCCESS;
}