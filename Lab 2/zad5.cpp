#include <iostream>
#include <fstream>
#include <cctype>
#include <stdexcept>
#include <vector>
#include <iomanip>
#include <numeric>
#include <algorithm>

#if defined(_WIN32)

#include <windows.h>
#define sleepf(dt) Sleep(dt)

#elif defined(__linux__)

#include <unistd.h>
#define sleepf(dt) sleep(dt)

#endif

#pragma region Izvori

class IzvorBrojeva
{
public:
    virtual int next() = 0;
    virtual ~IzvorBrojeva() = default;
};

class TipkovnickiIzvor : public IzvorBrojeva
{
public:
    int next() override
    {
        if(std::cin.eof())
            return -1;

        int num;
        std::cin >> num;
        return num;
    }
};

class DatotecniIzvor : public IzvorBrojeva
{
private:
    std::ifstream m_src;
    int eof;
public:
    DatotecniIzvor(const std::string& path) : m_src(path), eof(0)
    {}

    int next() override
    {
        if(m_src.eof())
            return -1;

        int num;
        m_src >> num;
        return num;
    }

    ~DatotecniIzvor()
    {
        m_src.close();
    }
};

#pragma endregion
#pragma region Akcije

class Akcija
{
public:
    virtual void update(const std::vector<int>& values) = 0;
    virtual ~Akcija() = default;
};

class AkcijaDatoteka : public Akcija
{
private:
    std::ofstream m_dst;
public:
    AkcijaDatoteka(const std::string& path) : m_dst(path, std::ios_base::app)
    {}

    void update(const std::vector<int>& values) override
    {
        m_dst << "{" << values[0];
        for(int i = 1; i < values.size(); i++)
            m_dst << ", " << values[i];
        
        time_t t = std::time(nullptr);
        m_dst << "} @ " << std::put_time(std::localtime(&t), "%d-%m-%Y %H-%M-%S") << "\n";
    }

    ~AkcijaDatoteka()
    {
        m_dst.close();
    }
};

class AkcijaIspisSume : public Akcija
{
public:
    void update(const std::vector<int>& values) override
    {
        std::cout << "Suma elemenata iznosi: " << std::reduce(values.begin(), values.end(), 0) << "\n";
    }
};

class AkcijaIspisProsjek : public Akcija
{
public:
    void update(const std::vector<int>& values) override
    {
        std::cout << "Prosjecna vrijednost elemenata iznosi: " << static_cast<float>(std::reduce(values.begin(), values.end(), 0)) / values.size() << "\n";
    }
};

class AkcijaIspisMedijan : public Akcija
{
public:
    void update(const std::vector<int>& values) override
    {
        std::vector<int> sorted_values = values;
        std::sort(sorted_values.begin(), sorted_values.end());
        size_t N = sorted_values.size();
        std::cout << "Medijan elemenata iznosi: " << (N % 2 ? sorted_values[N >> 1] : 0.5f * (sorted_values[N >> 1] + sorted_values[(N >> 1) - 1])) << "\n";
    }
};

#pragma endregion

class SlijedBrojeva
{
private:
    IzvorBrojeva* m_src;
    std::vector<Akcija*> m_actions;

    std::vector<int> container;
public:
    SlijedBrojeva(IzvorBrojeva* src) : m_src(src)
    {}

    void attach(Akcija* update)
    {
        m_actions.push_back(update);
    }

    void notify()
    {
        for(auto a : m_actions)
            a->update(container);
    }

    void kreni()
    {
        int new_val;
        while((new_val = m_src->next()) != -1)
        {
            container.push_back(new_val);
            notify();

            sleepf(1000);
        }
    }
};

int main()
{
    AkcijaIspisMedijan am;
    AkcijaIspisProsjek ap;
    AkcijaIspisSume as;
    AkcijaDatoteka ad("o.txt");

    DatotecniIzvor di("n.txt");

    SlijedBrojeva sb(&di);
    sb.attach(&am);
    sb.attach(&ap);
    sb.attach(&as);
    sb.attach(&ad);

    sb.kreni();
    
    return 0;
}