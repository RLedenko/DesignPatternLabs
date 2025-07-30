#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Animal
{
    const char* name;
    const char* (**table)();
};

const char* dogGreet(void)
{
    return "vau!";
}

const char* dogMenu(void)
{
    return "kuhanu govedinu";
}

const char* catGreet(void)
{
    return "mijau!";
}

const char* catMenu(void)
{
    return "konzerviranu tunjevinu";
}

const char* (*dogTable[])() = {dogGreet, dogMenu};
const char* (*catTable[])() = {catGreet, catMenu};

void constructDog(struct Animal* source, const char* name)
{
    source->name = name;
    source->table = dogTable;
}

struct Animal* createDog(const char* name)
{
    struct Animal* created = malloc(sizeof(struct Animal));
    constructDog(created, name);
    return created;
}

void constructCat(struct Animal* source, const char* name)
{
    source->name = name;
    source->table = catTable;
}

struct Animal* createCat(const char* name)
{
    struct Animal* created = malloc(sizeof(struct Animal));
    constructCat(created, name);
    return created;
}

void animalPrintGreeting(struct Animal* animal)
{
    printf("%s pozdravlja: %s\n", animal->name, animal->table[0]());
}

void animalPrintMenu(struct Animal* animal)
{
    printf("%s voli %s\n", animal->name, animal->table[1]());
}

void testAnimals(void)
{
    struct Animal* p1=createDog("Hamlet");
    struct Animal* p2=createCat("Ofelija");
    struct Animal* p3=createDog("Polonije");
  
    animalPrintGreeting(p1);
    animalPrintGreeting(p2);
    animalPrintGreeting(p3);
  
    animalPrintMenu(p1);
    animalPrintMenu(p2);
    animalPrintMenu(p3);
  
    free(p1); free(p2); free(p3);
}

struct Animal* createNDogs(size_t n)
{
    struct Animal* dogs = (struct Animal*)malloc(n * sizeof(struct Animal*));
    for(size_t i = 0; i < n; i++)
        constructDog(&dogs[i], "Dog");
    return dogs;
}

int main()
{
    testAnimals();

// Primjer za statički alociranu mačku
    struct Animal p1;
    p1.name = "Mica";
    p1.table = catTable;
    animalPrintGreeting(&p1);
    animalPrintMenu(&p1);

// 5 pasa
    struct Animal* dogs = createNDogs(5);
    for(size_t i = 0; i < 5; i++)
        animalPrintGreeting(&dogs[i]);

    for(size_t i = 0; i < 5; i++)
        free(&dogs[i]);
    free(dogs);

    return 0;
}