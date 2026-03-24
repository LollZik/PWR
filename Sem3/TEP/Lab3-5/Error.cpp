#include "Error.hpp"

Error::Error(std::string e) {
	errDesc = e;
}
Error::Error(const Error& other) {
	errDesc = other.errDesc;
}

std::string Error::getError() const {
	return errDesc;
}
