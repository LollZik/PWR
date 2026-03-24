#include "Number.h"
#include <stdexcept>
static const int baseTen = 10;

int calcValLen(int value) {
	value = abs(value);
	if (value == 0) {
		return 1;
	}
	int c = 0;
	while (value > 0) {
		value /= baseTen;
		c++;
	}
	return c;
}

void fillDigits(int value, int* digits) {
	value = abs(value);
	int i = 0;
	if (value == 0) {
		digits[0] = 0;
		return;
	}
	while (value > 0) {
		digits[i] = value % baseTen;
		value /= baseTen;
		i++;
	}
}

int compareAbs(const Number& a, const Number& b) {
	if (a.getSize() != b.getSize()) {
		return (a.getSize() > b.getSize() ? 1 : -1);
	}
	else{
		for(int i = a.getSize() - 1; i >= 0; i--) {
			if (a.getDigit(i) != b.getDigit(i)){
				return (a.getDigit(i) > b.getDigit(i) ? 1 : -1);
			}
		}
		return 0;
	}
}

Number clearZeros(int* list, int size, bool sign) {

	int resSize = size;

	while (resSize > 1 && list[resSize - 1] == 0) {
		resSize--;
	}

	int* finalAns = new int[resSize];
	for (int i = 0; i < resSize; i++) {
		finalAns[i] = list[i];
	}
	delete[] list;

	return Number(finalAns, resSize, sign);
}

Number add(Number a, Number b){
	int aSize = a.getSize();
	int bSize = b.getSize();

	int max = 1 + (aSize > bSize ? aSize : bSize);

	int* ans = new int[max];
	int sum;

	int carry = 0;

	int aVal = 0;
	int bVal = 0;

	for (int i = 0; i < max; i++){\
		aVal = (i < aSize ? a.getDigit(i) : 0);
		bVal = (i < bSize ? b.getDigit(i) : 0);
		sum = aVal + bVal + carry;
		ans[i] = sum % baseTen;
		carry = sum / baseTen;
	}
	if (ans[max - 1] != 0){
		return Number(ans, max, false);
	}
	max--;

	return clearZeros(ans, max, false);
}

Number subtract(const Number& a, const Number& b) {
	if (compareAbs(a, b) < 0) {
		return subtract(b, a);
	}

	int borrow = 0;
	int n = a.getSize();
	int bSize = b.getSize();

	int ai;
	int bi;

	int diff;
	int* ans = new int[n];
	for (int i = 0; i < n; i++){
		ai = a.getDigit(i);
		bi = (i < bSize ? b.getDigit(i) : 0);

		diff = ai - bi - borrow;
		if (diff < 0){
			diff += baseTen;
			borrow = 1;
		}
		else{
			borrow = 0;
		}
		ans[i] = diff;
	}
	int resSize = n;

	return clearZeros(ans, resSize, false);
}

Number* preCalcMultiplayers(Number num) {
	Number* list = new Number[10];
	list[0] = 0;
	for (int i = 1; i < baseTen; i++){
		list[i] = num * i;
	}
	return list;
}

Number findBiggest(Number* list, int size, Number num) {
	for (int i = size - 1; i >= 0; i--) {
		if (compareAbs(list[i], num) <= 0) {
			return list[i];
		}
	}
	return list[0];
}

int findNumInList(Number* list, int size, Number value) {
	for (int i = 0; i < size; i++) {
		if (compareAbs(list[i], value) == 0) {
			return i;
		}
	}
	return 0;
}

Number reverseNumber(int* num, int size, bool sign) {
	for (int i = 0; i < size/2; i++){
		int temp = num[i];
		num[i] = num[size - 1 - i];
		num[size - 1 - i] = temp;
	}
	
	return clearZeros(num, size, sign);
}

Number::Number()
:size(1),
negative(false),
digits(new int[size])
{
	
	digits[0] = 0;
}

Number::Number(int value)
	:size(calcValLen(value)),
	digits(new int[size])
{
	if (value < 0) {
		negative = true;
	}
	else {
		negative = false;
	}

	if (value == 0){
		digits[0] = 0;
	}
	else{
		fillDigits(value, digits);
	}
}

Number::Number(int *values, int length, bool isNegative){
	size = length;
	negative = isNegative;
	digits = values;
}

Number::Number(const Number& other){
	size = other.getSize();
	negative = other.isNegative();
	digits = new int[size];

	for (int i = 0; i < size; i++) {
		digits[i] = other.digits[i];
	}
}

Number::~Number(){
	delete[] digits;
}

void Number::operator=(const int value){
	negative = value < 0;
	int len = calcValLen(value);
	delete[] digits;
	digits = new int[len];
	size = len;
	fillDigits(value, digits);
}

void Number::operator=(const Number& other){
	if (this == &other) { return; }

	delete[] digits;

	if (other.digits == nullptr) {
		size = 1;
		digits = new int[size];
		digits[0] = 0;
		negative = false;
		return;
	}

	negative = other.negative;
	size = other.size;
	digits = new int[size];

	for (int i = 0; i < size; i++) {
		digits[i] = other.digits[i];
	}
}

Number Number::operator+(const Number &other){
	Number result;
	if (negative == other.isNegative()) {
		result = add(*this, other);
		result.negative = negative;
	}
	else{
		if (compareAbs(*this, other) >= 0){
			result = subtract(*this, other);
			result.negative = negative;
		}
		else {
			result = subtract(other, *this);
			result.negative = other.isNegative();
		}
	}
	if (result.size == 1 && result.digits[0] == 0) { 
		result.negative = false; 
	}
	return result;
}

Number Number::operator-(const Number &other){
	// a - b == a + (-b)
	Number negOther = other;
	negOther.negative = !negOther.isNegative();
	return *this + negOther;
}

Number Number::operator*(const Number &other){
	bool sign = (negative != other.negative);

	int sizeA = size;
	int sizeB = other.size;

	int* res = new int[sizeA + sizeB];
	int carry = 0;
	int temp;

	for (int i = 0; i < sizeA + sizeB; i++){
		res[i] = 0;
	}

	for (int i = 0; i < sizeA; i++) {
		carry = 0;
		for (int j = 0; j < sizeB; j++){
			temp = digits[i] * other.digits[j];
			res[i + j] += temp;
			carry = res[i + j] / baseTen;
			res[i + j] = res[i + j] % baseTen;
			res[i + j + 1] += carry;
		}
	}
	
	int len = sizeA + sizeB - 1;
	while (len > 0 && res[len] == 0){ 
		len--;
	}
	len++;

	int* finalAns = new int[len];
	for (int i = 0; i < len; i++) {
		finalAns[i] = res[i];
	}
	delete[] res;

	return Number(finalAns, len, sign);
}

Number Number::operator/(const Number &other){
	if (other.size == 1 && other.digits[0] == 0) {
		throw std::overflow_error("Divide by zero exception");
	}
	if (compareAbs(*this, other) < 0) {
		return Number();
	}

	bool sign = (negative != other.negative);
	int sizeA = size;

	int* res = new int[sizeA];

	for (int i = 0; i < sizeA; i++) {
		res[i] = 0;
	}
	Number remainder = 0;
	int resIdx = 0;

	Number* mult = preCalcMultiplayers(other);

	for (int i = sizeA - 1; i >= 0; i--) {
		remainder = remainder * 10;
		remainder = remainder + digits[i];
	
		if (compareAbs(remainder, other) >= 0){
			Number temp = findBiggest(mult, baseTen, remainder);
			temp.negative = false;
	
			res[resIdx] = findNumInList(mult, baseTen, temp);
			resIdx++;
			remainder = remainder - temp;
			
		}
	
	}
	delete[] mult;
	return reverseNumber(res, resIdx, sign);
	return reverseNumber(res, resIdx, sign);
}

Number& Number::operator++(){
	 *this = *this + 1;
	 return *this;
}
Number Number::operator++(int){
	Number old = *this;
	operator++();
	return old;
}

std::string Number::toStr(){
	std::string ans;
	ans.reserve(size + (negative ? 1 : 0));

	if (negative) {
		ans.push_back('-');
	}

	for (int i = size - 1; i >= 0; i--) {
		ans.push_back(char('0' + digits[i]));
	}
	return ans;
}

int Number::getSize() const{
	return size;
}

int Number::getDigit(int index) const{
	if (index > size || index < 0) {
		std::cout << "Wrong index";
		return NULL;
	}
	
	return digits[index];
}

bool Number::isNegative() const{
	return negative;
}

