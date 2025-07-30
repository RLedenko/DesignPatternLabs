#include "animal.hpp"

#include <iostream>

void printGreeting(Animal& animal)
{
    std::cout << animal.name() << " pozdravlja " << animal.greet() << "\n";
}

void printMenu(Animal& animal)
{
    std::cout << animal.name() << " jede " << animal.menu() << "\n";
}