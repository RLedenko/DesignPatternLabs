#include <iostream>
#include <assert.h>
#include <stdlib.h>

struct Point
{
    int x; 
    int y;
};

struct Shape
{
    enum EType {circle, square, rhombus};
    EType type_;
};

struct Circle
{
    Shape::EType type_;
    double radius_;
    Point center_;
};

struct Square
{
    Shape::EType type_;
    double side_;
    Point center_;
};

struct Rhombus
{
    Shape::EType type_;
    double side_;
    Point center_;
};

void drawSquare(struct Square*)
{
    std::cerr << "in drawSquare\n";
}

void drawCircle(struct Circle*)
{
    std::cerr << "in drawCircle\n";
}

void drawRhombus(struct Rhombus*)
{
    std::cerr << "in drawRhombus\n";
}

void drawShapes(Shape** shapes, int n)
{
    for (int i = 0; i < n; ++i)
    {
        struct Shape* s = shapes[i];
        switch (s->type_)
        {
            case Shape::square:
                drawSquare((struct Square*)s);
                break;
            case Shape::circle:
                drawCircle((struct Circle*)s);
                break;
            case Shape::rhombus:
                drawRhombus((struct Rhombus*)s);
                break;
            default:
                assert(0); 
                exit(0);
        }
    }
}

void moveCircle(struct Circle* c, int delta_x, int delta_y)
{
    printf("Moved circle from (%d, %d) to ", c->center_.x, c->center_.y);
    c->center_.x += delta_x;
    c->center_.y += delta_y;
    printf("(%d, %d).\n", c->center_.x, c->center_.y);
}

void moveSquare(struct Square* s, int delta_x, int delta_y)
{
    printf("Moved square from (%d, %d) to ", s->center_.x, s->center_.y);
    s->center_.x += delta_x;
    s->center_.y += delta_y;
    printf("(%d, %d).\n", s->center_.x, s->center_.y);
}

void moveShapes(Shape** shapes, int n, int delta_x, int delta_y)
{
    for(int i = 0; i < n; i++)
    {
        struct Shape* sh = shapes[i];
        switch(sh->type_)
        {
            case Shape::circle:
                moveCircle((struct Circle*)sh, delta_x, delta_y);
                break;
            case Shape::square:
                moveSquare((struct Square*)sh, delta_x, delta_y);
                break;
            default:
                assert(0);
                exit(0);
        }
    }
}

int main()
{
    Shape* shapes[5];
    shapes[0] = (Shape*)new Circle;
    shapes[0] -> type_=Shape::circle;
    shapes[1] = (Shape*)new Square;
    shapes[1] -> type_=Shape::square;
    shapes[2] = (Shape*)new Square;
    shapes[2] -> type_=Shape::square;
    shapes[3] = (Shape*)new Circle;
    shapes[3] -> type_=Shape::circle;
    shapes[4] = (Shape*)new Rhombus;
    shapes[4] -> type_=Shape::rhombus;

    drawShapes(shapes, 5);
    moveShapes(shapes, 5, 1, 1);
}