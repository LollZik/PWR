#ifndef CTABLE
#define CTABLE
#include <string>
#include <iostream>
using namespace std;

class CTable{
public:
	CTable();
	
	CTable(string name, int len);

	CTable(const CTable &other);

	~CTable();

	CTable *clone();

	void modTab(CTable *table, int newSize);

	void modTab(CTable table, int newSize);

	string getName() const;

	void setName(string newName);

	int getSize() const;

	bool setSize(int newSize);

	int* getTable() const;

	void putToTable(int value, int index);

	CTable extractNegative();

	
private:
	const string defaultName = "domyslna nazwa";

	static const int defaultLen = 4;

	string name;
	int len;
	int *table;

};
#endif

