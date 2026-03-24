#pragma once
#include <iostream>

class Error {
public:
	Error(std::string e);
	Error(const Error& other);
	std::string getError() const;
private:
	std::string errDesc;
};