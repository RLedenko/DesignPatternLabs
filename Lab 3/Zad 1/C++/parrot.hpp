#pragma once

#include "animal.hpp"

#include <iostream>

class Parrot : public Animal
{
private:
    static int hreg;
    std::string _name;
public:
    Parrot(std::string name);
    static void* myCreator(const std::string& arg);
    char const* name() override;
    char const* greet() override;
    char const* menu() override;
};