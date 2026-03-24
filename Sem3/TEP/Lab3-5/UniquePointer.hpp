#pragma once
#include <iostream>

template <typename T>
class UniquePointer {
public:
	UniquePointer(T* ptr);
	UniquePointer(const UniquePointer& other) = delete;
	UniquePointer(UniquePointer&& other);
	~UniquePointer();

	T& operator*() const;
	T* operator->() const;
	UniquePointer& operator=(const UniquePointer& other) = delete;
	UniquePointer& operator=(UniquePointer&& other);
private:
	T* pointer;
};

// Arrays

template <typename T>
class UniquePointer<T[]> {
public:
	UniquePointer(T* ptr);
	UniquePointer(const UniquePointer& other) = delete;
	UniquePointer(UniquePointer&& other);
	~UniquePointer();

	T& operator[](size_t idx) const;
	UniquePointer& operator=(const UniquePointer& other) = delete;
	UniquePointer& operator=(UniquePointer&& other);
private:
	T* pointer;
};




// IMPLEMENTATIONS

	// T
template <typename T>
UniquePointer<T>::UniquePointer(T* ptr) {
	pointer = ptr;
}

template <typename T>
UniquePointer<T>::UniquePointer(UniquePointer&& other) {
	pointer = other.pointer;
}

template <typename T>
UniquePointer<T>::~UniquePointer() {
		delete pointer;
}

template <typename T>
T& UniquePointer<T>::operator*() const {
	return *pointer;
}

template <typename T>
T* UniquePointer<T>::operator->() const {
	return pointer;
}

template <typename T>
UniquePointer<T>& UniquePointer<T>::operator=(UniquePointer&& other) {
	if (this == &other) {
		return *this;
	}

	delete pointer;

	pointer = std::exchange(other.pointer, nullptr);
	
	return *this;
}




// T[]
template <typename T>
UniquePointer<T[]>::UniquePointer(T* ptr) {
	pointer = ptr;
}

template <typename T>
UniquePointer<T[]>::UniquePointer(UniquePointer&& other) {
	pointer = other.pointer;
}


template <typename T>
UniquePointer<T[]>::~UniquePointer() {
	delete[] pointer;
}

template <typename T>
T& UniquePointer<T[]>::operator[](size_t idx) const {
	return pointer[idx];
}

template <typename T>
UniquePointer<T[]>& UniquePointer<T[]>::operator=(UniquePointer&& other) {
	if (this == &other) {
		return *this;
	}

	delete[] pointer;

	pointer = std::exchange(other.pointer, nullptr);

	return *this;
}