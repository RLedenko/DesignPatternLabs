#include <stdlib.h>

typedef const char* (*function)(...);

struct Animal
{
    function* vptr;
    const char* name;
};

const char* name(void* self)
{
    return ((struct Animal*)self)->name;
}

const char* greet(void)
{
    return "prrt!";
}

const char* menu(void)
{
    return "sjemenke";
}

function table[] = {(function)name, (function)greet, (function)menu};

__cdecl void* create(const char* name)
{
    struct Animal* created = malloc(sizeof(struct Animal));
    created->name = name;
    created->vptr = table;
    return (void*)created;
}

__cdecl size_t size_of()
{
    return sizeof(struct Animal);
}

__cdecl void construct(void* source, const char* name)
{
    struct Animal* s = (struct Animal*)source;
    s->name = name;
    s->vptr = table;
}