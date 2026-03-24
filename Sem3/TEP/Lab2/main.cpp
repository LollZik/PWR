#include <iostream>
#include <stdexcept>
#include "Number.h"

int main() {

	//Zad 2
	Number num1(368);
	Number num2(1567);

	std::cout <<"Zad 2\n" << num1.toStr() << "\n";
	std::cout << num2.toStr() <<"\n";
	num1 = num2;
	std::cout << num1.toStr() << "\n";
	std::cout << num2.toStr() << "\n\n";
	
	Number n = (nullptr, 2, false);

	// Zad 6
	Number test6a;
	Number test6b = -8;
	Number test6c = test6b;
	Number test6d = 122;
	Number test6e = -2;
	Number test6f = 1;

	std::cout << "Zad 6\n" << test6a.toStr() << "\n";
	std::cout << test6b.toStr() << "\n";
	std::cout << test6c.toStr() << "\n";
	std::cout << test6d.toStr() ;
	std::cout << "\nDodawanie:\n";
	std::cout << (test6b + test6d).toStr() << "\n"; // -8 + 122 = 114
	std::cout << (test6d + test6d).toStr() << "\n"; // 122 + 122 = 244
	std::cout << (test6f + 4).toStr() << "\n"; // 1 + 4 = 5
	std::cout << (test6c + test6b).toStr(); // -8 + (-8) = -16
	std::cout << "\nOdejmowanie:\n";

	std::cout << (test6b - test6d).toStr() << "\n"; // -8 - 122 = -130
	std::cout << (test6d - test6b).toStr() << "\n"; // 122 - (-8) = 130
	std::cout << (test6f - 100).toStr() << "\n"; // 1 - 100 = -99
	std::cout << (test6c - test6b).toStr(); // -8 - (-8) = 0
	std::cout << "\nMnozenie:\n";

	std::cout << (test6d * test6b).toStr() << "\n"; // 122*(-8) = -976
	std::cout << (test6d * test6a).toStr() << "\n"; // 122 * 0 = 0
	std::cout << (test6d * 2).toStr() << "\n";      // 122 * 2 = 244
	std::cout << (test6e * test6f).toStr() << "\n"; // -2 * 1 = -2
	std::cout << (test6a * test6f).toStr(); // 0 * 1 = 0

	std::cout << "\nDzielenie:\n";
	std::cout << (test6d / test6b).toStr() << "\n"; // 122 / (-8) = -15
	try{
		std::cout << (test6d / test6a).toStr() << "\n"; // 122 / 0 = exception
	}
	catch(std::overflow_error& e){
		std::cout << e.what() << std::endl;
	}
	std::cout << (test6d / 2.1f).toStr() << "\n";	    // 122 / 2 = 61
	std::cout << (test6e / test6f).toStr() << "\n"; // -2 / 1 = -2
	std::cout << (test6a / test6f).toStr() << "\n"; // 0 / 1 = 0

	std::cout << "\nModyfikacja:\n";
	std::cout << (test6f++).toStr()<<"\n"; // 1++
	std::cout << (test6f).toStr() << "\n"; // (2)
	std::cout << (++test6b).toStr(); // ++(-8)
}

