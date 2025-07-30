#pragma once

class Animal
{
public:
    virtual char const* name()=0;
    virtual char const* greet()=0;
    virtual char const* menu()=0;
};

void printGreeting(Animal& animal);
void printMenu(Animal& animal);