#pragma once
#include <iostream>
#include <vector>
#include "Error.hpp"

template <typename T, typename E>
class Result
{
public:
	Result(const T& val);
	Result(E* error);
	Result(const std::vector<E*>& errs);
	Result(const Result<T, E>& other);
	~Result();

	static Result<T, E> ok(const T& value);
	static Result<T, E> fail(E* error);
	static Result<T, E> fail(const std::vector<E*>& errors);

	Result<T, E>& operator=(const Result<T, E>& other);

	bool isSuccess() const;
	T getValue() const;

	std::vector<E*>& getErrors();
	const std::vector<E*>& getErrors() const;

private:
	T* value;
	std::vector<E*> errors;
};


// void


template <typename E>
class Result<void, E>
{
public:
	Result();
	Result(E* error);
	Result(const std::vector<E*>& errs);
	Result(const Result<void, E>& other);
	~Result();

	static Result<void, E> ok();
	static Result<void, E> fail(E* error);
	static Result<void, E> fail(const std::vector<E*>& errs);

	Result<void, E>& operator=(const Result<void, E>& other);

	bool isSuccess() const;

	std::vector<E*>& getErrors();
	const std::vector<E*>& getErrors() const;

private:
	std::vector<E*> errors;
};


// char

template <typename E>
class Result<char*, E>
{
public:
	Result() = delete;
	Result(const char* val) = delete;
	Result(E* error) = delete;
	Result(const std::vector<E*>& errs) = delete;
	Result(const Result<char*, E>& other) = delete;
	~Result();

	static Result<std::string, E> ok(const char value[]);
	static Result<std::string, E> fail(E* error);
	static Result<std::string, E> fail(const std::vector<E*>& errors);

	Result<std::string, E>& operator=(const Result<char*, E>& other);
	Result<std::string, E>& operator=(const Result<std::string, E>& other);

	std::string getValue() const;


private:
	std::string value;
	std::vector<E*> errors;
};






// Implementations










// <T, E>


template <typename T, typename E>
Result<T, E>::Result(const T& val) {
	value = new T(val);
}

template <typename T, typename E>
Result<T, E>::Result(E* error) {
	value = nullptr;
	if (error != nullptr) {
		errors.push_back(error);
	}
}

template <typename T, typename E>
Result<T, E>::Result(const std::vector<E*>& errs) {
	value = nullptr;
	for (int i = 0; i < errs.size(); i++) {
		errors.push_back(new E(*errs[i]));
	}
}

template <typename T, typename E>
Result<T, E>::Result(const Result<T, E>& other) {
	if (other.value != nullptr) {
		value = new T(*other.value);
	}
	else {
		value = nullptr;
	}
	for (int i = 0; i < other.errors.size(); i++) {
		errors.push_back(new E(*other.errors[i]));
	}
}

template <typename T, typename E>
Result<T, E>::~Result() {
	if (value != nullptr) {
		delete value;
	}
	for (int i = 0; i < errors.size(); i++) {
		delete errors[i];
	}
	errors.clear();
}

template <typename T, typename E>
Result<T, E> Result<T, E>::ok(const T& value) {
	return Result<T, E>(value);
}

template <typename T, typename E>
Result<T, E> Result<T, E>::fail(E* error) {
	return Result<T, E>(error);
}

template <typename T, typename E>
Result<T, E> Result<T, E>::fail(const std::vector<E*>& errors) {
	return Result<T, E>(errors);
}

template <typename T, typename E>
Result<T, E>& Result<T, E>::operator=(const Result<T, E>& other) {
	if (this == &other) { return *this; }

	if (value != nullptr) delete value;
	for (int i = 0; i < errors.size(); i++) delete errors[i];
	errors.clear();

	if (other.value != nullptr) {
		value = new T(other->value);
	}
	else {
		value = nullptr;
	}
	for (int i = 0; i < other.errors.size(); ++i) {
		errors.push_back(new E(other->errors[i]));
	}
	return *this;
}

template <typename T, typename E>
bool Result<T, E>::isSuccess() const {
	return value != nullptr && errors.empty();
}

template <typename T, typename E>
T Result<T, E>::getValue() const {
	return *value;
}

template <typename T, typename E>
std::vector<E*>& Result<T, E>::getErrors() {
	return errors;
}

template <typename T, typename E>
const std::vector<E*>& Result<T, E>::getErrors() const {
	return errors;
}



// <void, E>


template <typename E>
Result<void, E>::Result() {}

template <typename E>
Result<void, E>::Result(E* error) {
	if (error != nullptr) {
		errors.push_back(error);
	}
}

template <typename E>
Result<void, E>::Result(const std::vector<E*>& errs) {
	for (int i = 0; i < errs.size(); i++) {
		errors.push_back(new E(*errs[i]));
	}
}

template <typename E>
Result<void, E>::Result(const Result<void, E>& other) {
	for (int i = 0; i < other.errors.size(); i++) {
		errors.push_back(new E(other->errors[i]));
	}
}

template <typename E>
Result<void, E>::~Result() {
	for (int i = 0; i < errors.size(); i++) delete errors[i];
	errors.clear();
}

template <typename E>
Result<void, E> Result<void, E>::ok() {
	return Result<void, E>();
}

template <typename E>
Result<void, E> Result<void, E>::fail(E* error) {
	return Result<void, E>(error);
}

template <typename E>
Result<void, E> Result<void, E>::fail(const std::vector<E*>& errs) {
	return Result<void, E>(errs);
}

template <typename E>
Result<void, E>& Result<void, E>::operator=(const Result<void, E>& other) {
	if (this == &other) return *this;
	for (int i = 0; i < errors.size(); i++) delete errors[i];
	errors.clear();
	for (int i = 0; i < other->errors.size(); i++) {
		errors.push_back(new E(other->errors[i]));
	}
	return *this;
}

template <typename E>
bool Result<void, E>::isSuccess() const {
	return errors.empty();
}

template <typename E>
std::vector<E*>& Result<void, E>::getErrors() {
	return errors;
}

template <typename E>
const std::vector<E*>& Result<void, E>::getErrors() const {
	return errors;
}





// <char *>										   

template <typename E>
Result<char*, E>::~Result() {
	
}

template <typename E>
Result<std::string, E> Result<char*, E>::ok(const char value[]){
	return Result<std::string, E>(value);
}

template <typename E>
Result<std::string, E> Result<char*, E>::fail(E* error) {
	return Result<std::string, E>(error);
}

template <typename E>
Result<std::string, E> Result<char*, E>::fail(const std::vector<E*>& errs) {
	return Result<std::string, E>(errs);
}

template <typename E>
Result<std::string, E>& Result<char*, E>::operator=(const Result<char*, E>& other){
	if (this == &other) { return *this; }

	for (int i = 0; i < errors.size(); i++) delete errors[i];
	errors.clear();

	for (int i = 0; i < other.errors.size(); ++i) {
		errors.push_back(new E(other->errors[i]));
	}

	value = "";

	for (int i = 0; i < other->value.size(); ++i) {
		value += other->value[i];
	}
	
	return *this;
}

template <typename E>
Result<std::string, E>& Result<char*, E>::operator=(const Result<std::string, E>& other) {
	if (this == &other) { return *this; }

	for (int i = 0; i < errors.size(); i++) delete errors[i];
	errors.clear();

	for (int i = 0; i < other.errors.size(); ++i) {
		errors.push_back(new E(other->errors[i]));
	}

	value = other->value;

	return *this;
}

template <typename E>
std::string Result<char*, E>::getValue() const{
	return value;
}