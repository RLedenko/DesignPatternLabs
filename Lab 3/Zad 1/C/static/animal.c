#include "animal.h"

#include <stdio.h>

typedef const char* (*function)(...);

struct Animal
{
    function* vptr;
};

void animalPrintGreeting(struct Animal* animal)
{
    printf("%s pozdravlja: %s\n", animal->vptr[0](animal), animal->vptr[1]());
}

void animalPrintMenu(struct Animal* animal)
{
    printf("%s voli %s\n", animal->vptr[0](animal), animal->vptr[2]());
}

