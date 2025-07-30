#include <iostream>
#include <random>
#include <vector>
#include <algorithm>
#include <cmath>

#pragma region Generators

class NumberGenerator
{
public:
    virtual std::vector<int> generate() = 0;
    virtual ~NumberGenerator() = default;
};

class SequentialGenerator : NumberGenerator
{
private:
    int m_begin, m_end, m_step;
public:
    SequentialGenerator(int begin, int end, int step) : m_begin(begin), m_end(end), m_step(step) 
    {}

    std::vector<int> generate() override
    {
        std::vector<int> ret; int i = -1;
        std::generate_n(std::back_inserter(ret), (m_end - m_begin)/m_step + 1, [&]()->int{return m_begin + ++i * m_step;});
        return ret;
    }
};

class NormalGenerator : public NumberGenerator
{
private:
    float m_mu, m_sigma;
    int m_count;
    std::normal_distribution<float> m_dist;
    std::random_device m_gen;
public:
    NormalGenerator(float mu, float sigma, int count) : m_mu(mu), m_sigma(sigma), m_count(count), m_dist(mu, sigma)
    {}

    std::vector<int> generate() override
    {
        std::vector<int> ret(m_count);
        std::generate_n(ret.begin(), m_count, [&]()->int{return std::round(m_dist(m_gen));});
        return ret;
    }
};

class FibbonacciGenerator : public NumberGenerator
{
private:
    int m_count;
public:
    FibbonacciGenerator(int count) : m_count(count)
    {}

    std::vector<int> generate() override
    {
        std::vector<int> ret = {1, 1}; int Fp = 1, F = 1;
        std::generate_n(std::back_inserter(ret), m_count - 2, [&]()->int{int t = F; F += Fp; Fp = t; return F;});
        return ret;
    }
};

#pragma endregion

#pragma region Percentiles

class PercentileCalculator
{
public:
    virtual int percentile(float p, std::vector<int>& values) = 0;
    virtual ~PercentileCalculator() = default;
};

class ClosestValueCalculator : public PercentileCalculator
{
public:
    int percentile(float p, std::vector<int>& values) override
    {
        std::vector<int> sorted_values = values;
        std::sort(sorted_values.begin(), sorted_values.end());
        int n_p = static_cast<int>(p * sorted_values.size() / 100 + 0.5f);
        return sorted_values[n_p];
    }
};

class InterpolatedValueCalculator : public PercentileCalculator
{
public:
    int percentile(float p, std::vector<int>& values) override
    {
        size_t N = values.size();

        std::vector<int> sorted_values = values;
        std::sort(sorted_values.begin(), sorted_values.end());

        std::vector<float> p_values(N);
        int i = 0;
        std::generate_n(p_values.begin(), N, [&]()->int{return 100.f * (static_cast<float>(++i) - 0.5f) / N;});

        if(p < p_values[0])
            return sorted_values[0];

        if(p > p_values[N - 1])
            return p_values[N - 1];

        i = -1;
        while(i != N - 1 && p_values[++i] < p);

        return sorted_values[i - 1] + N / 100.f * (p - p_values[i - 1]) * (sorted_values[i] - sorted_values[i - 1]);
    }
};

#pragma endregion 

class DistributionTester
{
private:
    NumberGenerator* m_gen;
    PercentileCalculator* m_calc;
public:
    DistributionTester(NumberGenerator* gen, PercentileCalculator* calc) : m_gen(gen), m_calc(calc)
    {}

    void process()
    {
        auto values = m_gen->generate();
        std::cout << "Generirane vrijednosti: " << values[0];
        for(size_t i = 1; i < values.size(); i++)
            std::cout << ", " << values[i];
        std::cout << "\n";

        float percentile = 0.f;
        while((percentile += 10.f) < 91.f)
            std::cout << percentile << "-ti percentil skupa iznosi: " << m_calc->percentile(percentile, values) << "\n";
    }
};

int main()
{
    NormalGenerator gen(0, 10, 100);
    InterpolatedValueCalculator calc;
    
    DistributionTester d_test(static_cast<NumberGenerator*>(&gen), static_cast<PercentileCalculator*>(&calc));

    d_test.process();
    return 0;
}