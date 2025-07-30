#include "factory.hpp"

#include <algorithm>

Factory* Factory::_fact_instance = 0;

Factory& Factory::instance()
{
    if(_fact_instance == 0)
        _fact_instance = new Factory();

    return *_fact_instance;
}

int Factory::registerCreator(const std::string &id, void* (*foo)(const std::string&))
{
    return creators_.insert({id, foo}).second ? 0 : -1;
}

void* Factory::create(const std::string &id, const std::string &arg)
{
    return creators_[id](arg);
}

std::vector<std::string> Factory::getIds()
{
    std::vector<std::string> ret;
    std::transform(creators_.begin(), creators_.end(), std::back_inserter(ret), [](const std::pair<std::string, void* (*)(const std::string&)> &pair){return pair.first;});
    return ret;
}


Factory::~Factory()
{
    delete _fact_instance;
}