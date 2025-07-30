#include <stdio.h>
#include <string.h>

const void* mymax(const void* base, size_t nmemb, size_t size, int (*compar)(const void*, const void*))
{
    const void* _max = base;
    for(size_t i = 1; i < nmemb; i++)
        if(compar(base + i * size, _max))
            _max = base + i * size;
    return _max;
}

int gt_int(const void* l_val, const void* r_val)
{
    return *(int*)l_val > *(int*)r_val ? 1 : 0;
}

int gt_char(const void* l_val, const void* r_val)
{
    return *(char*)l_val > *(char*)r_val ? 1 : 0;
}

int gt_str(const void* l_val, const void* r_val)
{
    return strcmp(*(const char**)l_val, *(const char**)r_val) > 0 ? 1 : 0;
}

int main()
{
    int arr_int[] = {1, 3, 4, 9, 2, 3, 4, 12, 2, -1, 7};
    char arr_char[] = "Suncana strana ulice";
    const char* arr_str[] = {"Gle", "malu", "vocku", "poslije", "kise", "Puna", "je", "kapi", "pa", "ih", "njise"};
    printf("%d\n", *(int*)mymax(arr_int, 11, sizeof(int), gt_int));
    printf("%c\n", *(char*)mymax(arr_char, 20, sizeof(char), gt_char));
    printf("%s\n", *(const char**)mymax(arr_str, 11, sizeof(char*), gt_str));
    return 0;
}