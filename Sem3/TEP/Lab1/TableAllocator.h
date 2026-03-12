#ifndef TABLE_ALLOCATOR_H
#define TABLE_ALLOCATOR_H
#include <iostream>

class TableAllocator{
public:	
	static void allocTableFill34(int size);

	static bool alloc2DTable(int ***table, int xSize, int ySize);

	static bool dealloc2DTable(int ***table, int xSize, int ySize);

private:
	static const int numberToFill = 34;

};
#endif

