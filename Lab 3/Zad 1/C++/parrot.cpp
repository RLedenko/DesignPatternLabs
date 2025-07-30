#include "parrot.hpp"
#include "factory.hpp"

Parrot::Parrot(std::string name)
{
    this->_name = name;
}

char const* Parrot::name()
{
    return _name.c_str();
}

char const* Parrot::greet()
{
    return "prrt!";
}

char const* Parrot::menu()
{
    return "sjemenke";
}


void* Parrot::myCreator(const std::string& arg)
{
    return (void*)(new Parrot(arg));
}

int Parrot::hreg=Factory::instance().registerCreator("parrot", myCreator);