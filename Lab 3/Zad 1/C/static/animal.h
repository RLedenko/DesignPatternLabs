#pragma once

struct Animal;

#ifdef __cplusplus
extern "C" 
{
#endif

void animalPrintGreeting(struct Animal* animal);
void animalPrintMenu(struct Animal* animal);

typedef void (__cdecl *construct_type)(void*, const char*);
typedef unsigned long long (__cdecl *size_of_type)();

#ifdef __cplusplus
}
#endif