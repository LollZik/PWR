#pragma once
#include "RefCounter.hpp"

template <typename T>
class SmartPointer {
public:
	SmartPointer(T* ptr);
	SmartPointer(const SmartPointer& other);
	SmartPointer(SmartPointer&& other);
	~SmartPointer();

	T& operator*() const;
	T* operator->() const;
	SmartPointer& operator=(const SmartPointer& other);
	SmartPointer& operator=(SmartPointer&& other);
private:
	T* pointer;
	RefCounter* counter;
};

// Arrays

template <typename T>
class SmartPointer<T[]> {
public:
	SmartPointer(T* ptr);
	SmartPointer(const SmartPointer& other);
	SmartPointer(SmartPointer&& other);
	~SmartPointer();

	T& operator[](size_t idx) const;
	SmartPointer& operator=(const SmartPointer& other);
	SmartPointer& operator=(SmartPointer&& other);
private:
	T* pointer;
	RefCounter* counter;
};




// IMPLEMENTATIONS

	// T
template <typename T>
SmartPointer<T>::SmartPointer(T* ptr) {
	pointer = ptr;
	counter = new RefCounter();
	counter->add();
}

template <typename T>
SmartPointer<T>::SmartPointer(const SmartPointer& other) {
	counter = other.counter;
	counter->add();
	pointer = other.pointer;
}

template <typename T>
SmartPointer<T>::SmartPointer(SmartPointer&& other):
	counter(other.counter)
{
	counter->add();
	pointer = other.pointer;
}

template <typename T>
SmartPointer<T>::~SmartPointer() {
	if (counter->dec() == 0) {
		delete pointer;
		delete counter;
	}
}

template <typename T>
T& SmartPointer<T>::operator*() const{
	return *pointer;
}

template <typename T>
T* SmartPointer<T>::operator->() const{
	return pointer;
}

template <typename T>
SmartPointer<T>& SmartPointer<T>::operator=(const SmartPointer& other) {
	if (this == &other) {
		return *this;
	}

	if (counter->dec() == 0) {
		delete pointer;
		delete counter;
	}

	pointer = other.pointer;
	counter = other.counter;
	counter->add();

	return *this;
}

template <typename T>
SmartPointer<T>& SmartPointer<T>::operator=(SmartPointer&& other) {
	if (this == &other) {
		return *this;
	}

	pointer = std::exchange(other.pointer, pointer);
	counter = other.counter;
	counter->add();

	return *this;
}


// T[]
template <typename T>
SmartPointer<T[]>::SmartPointer(T* ptr) {
	pointer = ptr;
	counter = new RefCounter();
	counter->add();
}

template <typename T>
SmartPointer<T[]>::SmartPointer(const SmartPointer& other) {
	counter = other.counter;
	counter->add();
	pointer = other.pointer;
}

template <typename T>
SmartPointer<T[]>::SmartPointer(SmartPointer&& other):
	counter(other.counter)
{
	counter->add();
	pointer = other.pointer;
}

template <typename T>
SmartPointer<T[]>::~SmartPointer() {
	if (counter->dec() == 0) {
		delete[] pointer;
		delete counter;
	}
}

template <typename T>
T& SmartPointer<T[]>::operator[](size_t idx) const{
	return pointer[idx];
}

template <typename T>
SmartPointer<T[]>& SmartPointer<T[]>::operator=(const SmartPointer& other) {
	if (this == &other) {
		return *this;
	}

	if (counter->dec() == 0) {
		delete[] pointer;
		delete counter;
	}

	pointer = other.pointer;
	counter = other.counter;
	counter->add();

	return *this;
}

template <typename T>
SmartPointer<T[]>& SmartPointer<T[]>::operator=(SmartPointer&& other) {
	if (this == &other) {
		return *this;
	}

	pointer = std::exchange(other.pointer, pointer);
	counter = other.counter;
	counter->add();

	return *this;
}