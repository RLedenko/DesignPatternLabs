#pragma once

#include <iostream>
#include <vector>
#include <map>

class Factory
{
public:
    static Factory &instance();
    int registerCreator(const std::string &id, void* (*foo)(const std::string&));
    void* create(const std::string &id, const std::string &arg);
    std::vector<std::string> getIds();

private:
    Factory() = default;
    ~Factory();
    Factory(const Factory&) = delete;
    Factory& operator=(const Factory&) = delete;

    std::map<std::string, void* (*)(const std::string&)> creators_;
    static Factory* _fact_instance;
};