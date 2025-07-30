#include "factory.h"

#include <libloaderapi.h>
#include <stdio.h>

typedef void* (__cdecl *create_type)(const char*);
typedef void* (__cdecl *destroy_type)(void*);

void* myfactory(const char* libname, const char* ctorarg)
{
    HMODULE lib = LoadLibraryA(libname);
    if(!lib)
        return NULL;

    create_type create = (create_type)GetProcAddress(lib, "create");
    if(!create)
        return NULL;

    return create(ctorarg);
}

void mydestroy(const char* libname, void* object)
{
    HMODULE lib = LoadLibraryA(libname);
    if(!lib)
        return;

    destroy_type destroy = (destroy_type)GetProcAddress(lib, "destroy");
    if(!destroy)
        return;

    destroy(object);
}