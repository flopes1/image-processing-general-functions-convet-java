package com.poli.model.filter.noise;

import com.poli.model.util.math.GaussianFunction;

public class GaussianNoise extends NoiseType
{
    private int mean;
    private int standardDeviation;
    private GaussianFunction function;

    public GaussianNoise(EnumNoise noiseType, int mean, int standardDeviation)
    {
        super(noiseType);
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.function = new GaussianFunction(this.mean, this.standardDeviation);
    }

    @Override
    public int generateNoise(int inputValue)
    {
        int newValue = this.function.calculateFunctionOutput(inputValue);

        return newValue;
    }

    public int getMean()
    {
        return mean;
    }

    public int getStandardDeviation()
    {
        return standardDeviation;
    }

}
