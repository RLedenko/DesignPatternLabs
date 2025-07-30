#include <iostream>
#include <cstring>
#include <vector>
#include <set>

template<typename Iterator, typename Predicate>
Iterator mymax(Iterator first, Iterator last, Predicate pred)
{
    Iterator _max = first;
    while(++first != last)    
        if(pred(*first, *_max))
            _max = first;
    return _max;
}

int gt_int(const int& l_val, const int& r_val)
{
    return l_val > r_val ? 1 : 0;
}

int gt_char(const char& l_val, const char& r_val)
{
    return l_val > r_val ? 1 : 0;
}

int gt_str(const char* l_val, const char* r_val)
{
    return strcmp(l_val, r_val) > 0 ? 1 : 0;
}

#define DATASET {1, 3, 5, 2, 19, 0, -1, 6}

int main()
{
    int arr[] = DATASET;
    std::vector<int> vec = DATASET;
    std::set<int> set = DATASET;
    
    std::cout << "C style array max = " << *mymax(arr, arr + sizeof(arr)/sizeof(int), gt_int) << "\n" 
              << "  std::vector max = " << *mymax(vec.begin(), vec.end(), gt_int) << "\n"
              << "     std::set max = " << *mymax(set.begin(), set.end(), gt_int) << "\n"
    ;

    return 0;
}