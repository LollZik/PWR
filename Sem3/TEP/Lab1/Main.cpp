#include <iostream>
#include "TableAllocator.h"
#include "CTable.h"
using namespace std;

int main()
{
    TableAllocator allocator;
    cout << "Zad 1:\n";

    allocator.allocTableFill34(7);
    allocator.allocTableFill34(0);
    allocator.allocTableFill34(-1);
    allocator.allocTableFill34(3);

    cout << "\n\nZad 2:\n";
    int **p1 = nullptr;
    int **p2;
    int** p3 = {};
    cout << allocator.alloc2DTable(&p1, 3, 3);
    cout << allocator.alloc2DTable(&p1, -3, 3);
    cout << allocator.alloc2DTable(&p2, 6, 7);
    cout << allocator.alloc2DTable(&p3, 2, 4);


    cout << "\n\nZad 3:\n";
    cout << allocator.dealloc2DTable(&p1, 3, 3);
    cout << allocator.dealloc2DTable(&p2, 6, 23);
    cout << allocator.dealloc2DTable(&p3, 2, -8124);

    cout << "\n\nZad 4:\n";

    CTable test1;
    cout << test1.getName() << "\n";

    CTable test2("Turbosprezarka", 123);
    cout << test2.getSize() << "\n";
    cout << test2.setSize(200) << "\n";
    cout << test2.setSize(100) << "\n";
    cout << test2.setSize(-1) << "\n";

    CTable test3(test2);
    CTable test4 = *test3.clone();
    test4.setName("test4");
    cout << test4.getName() << "\n";
    cout << test4.getSize() << "\n";
    test4.modTab(test4, 500);
    cout << test4.getSize() << "\n";
    test4.modTab(&test4, 400);
    cout << test4.getSize() << "\n";


    cout << "\n\nModyfikacja:\n";

    CTable test5("Modyfikacja", 10);
    int sign = 1;
    for (int i = 0; i <= 10;i++){
        test5.putToTable((i+1)*sign, i);
        sign *= -1;
    }
    cout << "\n\Lista przed wywolaniem:\n";
    for (int i = 0; i < test5.getSize(); i++){
        cout << test5.getTable()[i]<<" ";
    }
    CTable test5b = test5.extractNegative();

    cout << "\nLista po wywolaniu:\n";
    for (int i = 0; i < test5.getSize(); i++) {
        cout << test5.getTable()[i] << " ";
    }

    cout << "\n\Lista ujemnych:\n";
    for (int i = 0; i < test5b.getSize(); i++) {
        cout << test5b.getTable()[i] << " ";
    }
    cout << "\n";
}
