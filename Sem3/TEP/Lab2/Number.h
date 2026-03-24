#include <iostream>
#include <string>

class Number
{
public: 
	Number();
	Number(int value);
	Number(int *values, int length, bool isNegative);
	Number(const Number& other);
	~Number();

	void operator=(const int value);
	void operator=(const Number &other);

	Number operator+(const Number &other);
	Number operator-(const Number &other);
	Number operator*(const Number &other);
	Number operator/(const Number &other);
	Number& operator++();
	Number operator++(int);

	std::string toStr(void);

	int getSize() const;
	int getDigit(int index) const;
	bool isNegative() const;

private:
	static const int defaultLen = 1;
	static const int defaultValue = 0;
	int size;
	bool negative;
	int* digits;
};

