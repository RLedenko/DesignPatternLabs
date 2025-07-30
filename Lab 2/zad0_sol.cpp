#include <iostream>
#include <assert.h>
#include <stdlib.h>

struct Point
{
    int x; 
    int y;
};

class Shape
{
public:
    virtual void draw() = 0;
    virtual void move(int delta_x, int delta_y) = 0;
};

struct Circle : public Shape
{
public:
    double radius_;
    Point center_;

    virtual void draw() override
    {
        std::cerr << "in draw @ Circle\n";
    }

    virtual void move(int delta_x, int delta_y) override
    {
        printf("Moved circle from (%d, %d) to ", center_.x, center_.y);
        center_.x += delta_x;
        center_.y += delta_y;
        printf("(%d, %d).\n", center_.x, center_.y);
    }
};

struct Square : public Shape
{
    double side_;
    Point center_;

    virtual void draw() override
    {
        std::cerr << "in draw @ Square\n";
    }

    virtual void move(int delta_x, int delta_y) override
    {
        printf("Moved square from (%d, %d) to ", center_.x, center_.y);
        center_.x += delta_x;
        center_.y += delta_y;
        printf("(%d, %d).\n", center_.x, center_.y);
    }
};

struct Rhombus : public Shape
{
    double side_;
    Point center_;
    
    virtual void draw() override
    {
        std::cerr << "in draw @ Rhombus\n";
    }

    virtual void move(int delta_x, int delta_y) override
    {
        printf("Moved rhombus from (%d, %d) to ", center_.x, center_.y);
        center_.x += delta_x;
        center_.y += delta_y;
        printf("(%d, %d).\n", center_.x, center_.y);
    }
};

void drawShapes(Shape** shapes, int n)
{
    for (int i = 0; i < n; ++i)
        shapes[i]->draw();
}

void moveShapes(Shape** shapes, int n, int delta_x, int delta_y)
{
    for(int i = 0; i < n; i++)
        shapes[i]->move(delta_x, delta_y);
}

int main()
{
    Shape* shapes[5];
    shapes[0] = (Shape*)new Circle;
    shapes[1] = (Shape*)new Square;
    shapes[2] = (Shape*)new Square;
    shapes[3] = (Shape*)new Circle;
    shapes[4] = (Shape*)new Rhombus;

    drawShapes(shapes, 5);
    moveShapes(shapes, 5, 1, 1);
}