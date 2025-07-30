#pragma once

#ifdef __cplusplus
extern "C" 
{
#endif

void* myfactory(const char* libname, const char* ctorarg);
void mydestroy(const char* libname, void* object);

#ifdef __cplusplus
}
#endif