#include "TableAllocator.h"
using namespace std;

void TableAllocator::allocTableFill34(int size){
	if (size <= 0) {
		cout << "Wrong size\n";
		return;
	}

	int *p = new int[size];

	for (int i = 0; i < size; i++){
		*(p + i) = numberToFill;
	}

	for (int i = 0; i < size;i++) {
		cout << (*(p + i))<<" ";
	}
	cout << "\n";
	delete[] p;

}

bool TableAllocator::alloc2DTable(int ***table, int xSize, int ySize){
	if (xSize <= 0 || ySize <= 0){
		cout << "Wrong size";
		return false;
	}
	if (table == nullptr){
		cout << "Pointer is a nullpointer";
		return false;
	}
	*table = new int* [xSize];

	for (int i = 0; i < xSize; i++){
		*(*table + i) = new int[ySize];
	}
	return true;
}

bool TableAllocator::dealloc2DTable(int*** table, int xSize, int ySize){
	if (xSize <= 0) {
		cout << "Wrong size";
		return false;
	}
	if (table == nullptr) {
		cout << "Pointer is a nullpointer";
		return false;
	}

	for (int i = 0; i < xSize; i++){
		delete[] (*table)[i];
	}
	delete[] (*table);
	return true;
}
