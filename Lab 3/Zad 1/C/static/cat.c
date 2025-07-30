#include <stdio.h>
#include <stdlib.h>

struct Animal
{
    const char* m_name;
    void* (**vptr)();
};

const char* name(struct Animal* self)
{
    return self->m_name;
}

const char* greet(void)
{
    return "mijau!";
}

const char* menu(void)
{
    return "konzerviranu tunjevinu";
}

void* (*catTable[])() = {name, greet, menu};

void construct(struct Animal* source, const char* name)
{
    source->m_name = name;
    source->vptr = catTable;
}

struct Animal* create(const char* name)
{
    struct Animal* created = malloc(sizeof(struct Animal));
    construct(created, name);
    return created;
}