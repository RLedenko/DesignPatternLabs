#pragma once

struct Animal;

#ifdef __cplusplus
extern "C" 
{
#endif

void animalPrintGreeting(struct Animal* animal);
void animalPrintMenu(struct Animal* animal);

#ifdef __cplusplus
}
#endif