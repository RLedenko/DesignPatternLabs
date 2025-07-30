#include <stdio.h>
#include <stdlib.h>

#include "factory.h"
#include "animal.h"

int main(int argc, char** argv)
{
    for(int i = 0; i < argc/2; ++i)
    {
        void* size_of = (size_of_type)myfactory(argv[1+2*i], "size_of");
        if(!size_of)
        {
            printf("Creation of plug-in object %s failed.\n", argv[1+2*i]);
            continue;
        }

        unsigned long long size = ((size_of_type)size_of)();
        struct Animal* p = alloca(size);
        
        ((construct_type)myfactory(argv[1+2*i], "construct"))(p, argv[1+2*i+1]);
        if(!p)
        {
            printf("Creation of plug-in object %s failed.\n", argv[1+2*i]);
            continue;
        }

        animalPrintGreeting(p);
        animalPrintMenu(p);
    }
}