#include <stdio.h>
#include <stdlib.h>

struct Unary_Function
{
//private:
    int lower_bound;
    int upper_bound;

//public:
    double (*value_at)(void*, double);
    double (*negative_value_at)(void*, double);
};

double _unary_function_base_value_at(void* self, double x)
{
    return 0.;
}

double _unary_function_base_negative_value_at(void* self, double x)
{
    return -((struct Unary_Function*)self)->value_at(self, x);
}

struct Unary_Function create_Unary_Function(int lb, int ub)
{
    struct Unary_Function created = {.lower_bound = lb, .upper_bound = ub, .value_at = _unary_function_base_value_at, .negative_value_at = _unary_function_base_negative_value_at};
    return created;
}

void tabulate(struct Unary_Function* self)
{
    for(int x = self->lower_bound; x <= self->upper_bound; x++)
    {
        printf("f(%d)=%lf\n", x, self->value_at(self, x));
    }
}

int same_functions_for_ints(struct Unary_Function* f1, struct Unary_Function* f2, double tolerance)
{
    if(f1->lower_bound != f2->lower_bound) return 0;
    if(f1->upper_bound != f2->upper_bound) return 0;
    for(int x = f1->lower_bound; x <= f1->upper_bound; x++)
    {
        double delta = f1->value_at(f1, x) - f2->value_at(f2, x);
        if(delta < 0.) delta = -delta;
        if(delta > tolerance) return 0;
    }
    return 1;
}

struct Square
{
    struct Unary_Function super;
};

double _square_value_at(void *self, double x)
{
    return x*x;
}

struct Square create_Square(int lb, int ub)
{
    struct Square created;
    created.super = create_Unary_Function(lb, ub);
    created.super.value_at = _square_value_at;
    return created;
}

struct Linear
{
    struct Unary_Function super;
//private
    double a;
    double b;
};

double _linear_value_at(void* self, double x)
{
    struct Linear linear = *(struct Linear*)self;
    return linear.a * x + linear.b;
}

struct Linear create_Linear(int lb, int ub, double a_coef, double b_coef)
{
    struct Linear created = {.super = create_Unary_Function(lb, ub), .a = a_coef, .b = b_coef};
    created.super.value_at = _linear_value_at;
    return created;
}

int main()
{
    struct Square f1 = create_Square(-2, 2);
    tabulate(&f1.super);

    struct Linear f2 = create_Linear(-2, 2, 5, -2);
    tabulate(&f2.super);
    
    printf("f1==f2: %s\n", same_functions_for_ints(&f1.super, &f2.super, 1e-6) ? "DA" : "NE");
    printf("neg_val f2(1) = %lf\n", f2.super.negative_value_at(&f2, 1.));

    return 0;
}