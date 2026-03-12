#include "CTable.h"
using namespace std;

CTable::CTable() {
	name = defaultName;
	len = defaultLen;
	table = new int[len];
	cout << "bezp: " << name << "\n";
}

CTable::CTable(string objName, int size) {
	name = objName;
	len = size;
	table = new int[len];
}

CTable::CTable(const CTable &other) {
	name = other.getName()+"_copy";
	len = other.getSize();
	table = new int[len];
	for (int i = 0; i < len; i++){
		table[i] = other.getTable()[i];
	}
	cout << "kopiuj: " << name << "\n";
}

CTable::~CTable() {
	delete[] table;
	cout << "usuwam " << name << "\n";
}

CTable* CTable::clone(){
	return new CTable(*this);
}

void CTable::modTab(CTable *tableArg, int newSize){
	tableArg->setSize(newSize);
}

void CTable::modTab(CTable tableArg, int newSize){
	tableArg.setSize(newSize);
}

void CTable::setName(string newName){
	name = newName;
}

string CTable::getName() const {
	return name;
}

bool CTable::setSize(int newSize){
	if (newSize <= 0){
		return false;
	}
	int copyLen = (newSize < len) ? newSize : len;
	
	int* p = new int[newSize];
	for (int i = 0; i < copyLen; i++){
		p[i] = table[i];
	}
	delete[] table;
	table = p;
	len = newSize;

	return true;
}

int CTable::getSize() const {
	return len;
}

void CTable::putToTable(int value, int index){
	if (index < len) {
		table[index] = value;
	}
}

int* CTable::getTable() const {
	return table;
}

CTable CTable::extractNegative(){
	int* negatives = new int[len];
	int* positives = new int[len];
	int p = 0;
	int n = 0;
	for (int i = 0; i < len; i++){
		if (table[i] < 0){
			negatives[n] = table[i];
			n++;
		}
		else{
			positives[p] = table[i];
			p++;
		}
	}
	CTable newTable(name, n);

	for (int i = 0; i < n; i++){
		newTable.getTable()[i] = negatives[i];
	}

	this->setSize(p);

	for (int i = 0; i < p; i++) {
		table[i] = positives[i];
	}

	delete[] positives;
	delete[] negatives;
	return newTable;
}