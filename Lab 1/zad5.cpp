#include <iostream>

class B {
public:
    virtual int __cdecl prva()=0;
    virtual int __cdecl druga(int)=0;
};
    
class D: public B {
public:
    virtual int __cdecl prva(){return 42;}
    virtual int __cdecl druga(int x){return prva()+x;}
};

void print_vtable_return_vals(B* pb)
{
    void** vtable = *(void***)pb;
    int __cdecl (*foo)(void*)      = (int __cdecl (*)(void*))(vtable)[0];
    int __cdecl (*bar)(void*, int) = (int __cdecl (*)(void*, int))(vtable)[1]; 

    std::cout << "pb->prva() = "    << foo(pb)   << \
               "\npb->druga(13) = " << bar(pb, 13) << "\n";
}

int main()
{
    B* b = new D;
    print_vtable_return_vals(b);
    
    delete b;

    return 0;
}