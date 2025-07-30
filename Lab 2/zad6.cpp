#include <iostream>
#include <vector>
#include <cmath>
#include <stdexcept>
#include <algorithm>

class Sheet;

class Cell
{
private:
    std::vector<Cell*> dependents;
public:
    std::string exp;
    double value;
    Sheet* parent;

    Cell(Sheet* parent) : exp("0"), value(0.), parent(parent)
    {}

    void attach(Cell* cell)
    {
        dependents.push_back(cell);
    }

    void detach(Cell* cell)
    {
        dependents.erase(std::remove(dependents.begin(), dependents.end(), cell), dependents.end());
    }

    void propagate();
};

class Sheet
{
private:
    std::vector<std::vector<Cell>> m_sheet;

public:
    Sheet(size_t x, size_t y) : m_sheet(x, std::vector<Cell>(y, 0))
    {
        for(auto& x : m_sheet)
            for(auto& cell : x)
                cell.parent = this;
    }

    Cell& cell(const std::string& ref)
    {
        size_t i = 0, j = 0;
        int mode = 0;
        for(char c : ref)
        {
            if(!mode && std::isdigit(c)) 
                mode++;

            if(!mode)
            {
                i *= 26;
                i += c - 'A';
            }
            else
            {
                j *= 10;
                j += c - '0';
            }
        }
        --j;

        return m_sheet[i][j];
    }

    void set(const std::string& ref, const std::string& content)
    {
        Cell& c = cell(ref);

        auto oldrefs = getrefs(c);
        for(Cell* old_cell : oldrefs)
            old_cell->detach(&c);

        c.exp = content;
        std::remove_if(c.exp.begin(), c.exp.end(), isspace);

        std::vector<Cell*> cycle_check, temp;
        cycle_check = getrefs(c);

        for(Cell* new_cell : cycle_check)
            new_cell->attach(&c);

        while(cycle_check != temp)
        {
            std::vector<Cell*> accumulator;
            for(auto x : cycle_check)
                for(auto y : getrefs(*x))
                    accumulator.push_back(y);
            
            if(std::find(accumulator.begin(), accumulator.end(), &c) != accumulator.end())
                throw std::invalid_argument("Cycle detected.");

            temp = cycle_check;
            cycle_check = accumulator;
        }

        c.propagate();
    }

    std::vector<Cell*> getrefs(Cell& cell)
    {
        std::vector<Cell*> ret;

        std::string ref = "";
        int mode = 0;
        for(char c : cell.exp)
        {
            if(!mode)
            {
                if(std::islower(c) || !std::isalnum(c))
                    throw std::invalid_argument("Invalid argument found : " + c + '\n');
                if(std::isdigit(c))
                    ++mode;
                ref += c;
            }
            else if(mode)
            {
                if(!isdigit(c))
                {
                    --mode;
                    if(c == '+' || c == '-')
                    {
                        ret.push_back(&(this->cell(ref)));
                        ref = "";
                        continue;
                    }
                    throw std::invalid_argument("Invalid operator found : " + c + '\n');
                }
                ref += c;
            }
        }

        if(ref != "" && std::isalpha(ref[0]))
            ret.push_back(&(this->cell(ref)));

        return ret;
    }

    double evaluate(Cell& cell)
    {
        auto refs = getrefs(cell);

        if(refs.empty())
        {
            cell.value = std::stod(cell.exp);
            return cell.value;
        }

        std::vector<char> ops;

        for(char c : cell.exp)
            if(c == '+' || c == '-')
                ops.push_back(c);
        
        double _val = evaluate(*refs[0]);
        for(size_t i = 1; i < refs.size(); i++)
        {
            if(ops[i - 1] == '+')
                _val += evaluate(*refs[i]);
            else
                _val -= evaluate(*refs[i]);
        }
        
        cell.value = _val;
        return cell.value;
    }

    void print()
    {
        for(std::vector<Cell>& x : m_sheet)
        {
            for(Cell& y : x)
                std::cout << y.value << "\t";
            std::cout << "\n";
        }
    }
};

void Cell::propagate()
{
    parent->evaluate(*this);
    for(Cell* cell : dependents)
    {
        parent->evaluate(*cell);
        cell->propagate();
    }
}

int main()
{
    Sheet sh(5, 5);
    sh.print();
    printf("\n");

    sh.set("A1", "2");
    sh.set("A2", "5");
    sh.set("A3", "A1+A2");
    sh.print();
    printf("\n");

    sh.set("A1", "4");
    sh.set("A4", "A1+A3");
    sh.print();
    printf("\n");

    sh.set("A1", "5");
    sh.print();
    printf("\n");

    sh.set("A4", "A3");
    sh.set("A1", "1");
    sh.print();
    printf("\n");

    sh.set("A4", "A1+A1+A1");
    sh.print();
    printf("\n");

    try
    {
        sh.set("A1", "A3");
    }
    catch(const std::exception& e)
    {
        std::cerr << "Cycle detected\n";
    }
    
    return 0;
}