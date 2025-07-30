#include "tiger.hpp"
#include "factory.hpp"

Tiger::Tiger(std::string name)
{
    this->_name = name;
}

char const* Tiger::name()
{
    return _name.c_str();
}

char const* Tiger::greet()
{
    return "grrr!";
}

char const* Tiger::menu()
{
    return "meso";
}


void* Tiger::myCreator(const std::string& arg)
{
    return (void*)(new Tiger(arg));
}

int Tiger::hreg=Factory::instance().registerCreator("tiger", myCreator);