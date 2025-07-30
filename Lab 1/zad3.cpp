#include <iostream>

class CoolClass
{
public:
    virtual void set(int x){x_=x;};
    virtual int get(){return x_;};

private:
    int x_;
};

class PlainOldClass
{
public:
    void set(int x){x_=x;};
    int get(){return x_;};

private:
    int x_;
};

int main()
{
    std::cout << "CoolClass: " << sizeof(CoolClass) << "\nPlainOldClass: " << sizeof(PlainOldClass) << "\n";
    return 0;
}