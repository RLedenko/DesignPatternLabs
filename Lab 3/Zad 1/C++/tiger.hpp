#pragma once

#include "animal.hpp"

#include <iostream>

class Tiger : public Animal
{
private:
    static int hreg;
    std::string _name;
public:
    Tiger(std::string name);
    static void* myCreator(const std::string& arg);
    char const* name() override;
    char const* greet() override;
    char const* menu() override;
};