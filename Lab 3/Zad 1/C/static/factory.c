#include "factory.h"

#include <libloaderapi.h>

void* myfactory(const char* libname, const char* command)
{
    HMODULE lib = LoadLibraryA(libname);
    if(!lib)
        return NULL;

    void* req = GetProcAddress(lib, command);

    if(!req)
        return NULL;

    return req;
}
