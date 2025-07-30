#include <bits/stdc++.h>
#include "factory.hpp"
#include "animal.hpp"

int main(void)
{
    Factory& fact(Factory::instance());
    std::vector<std::string> vecIds = fact.getIds();

    for (int i = 0; i < vecIds.size(); ++i)
    {
        std::ostringstream oss;
        oss << "Ljubimac " << i;
        Animal* pa = (Animal*)fact.create(vecIds[i], oss.str());
        printGreeting(*pa);
        printMenu(*pa);
        delete pa; 
    }
}