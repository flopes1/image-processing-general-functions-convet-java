package com.poli.model.util.math;

import java.util.Random;

public class GaussianFunction
{

    private int mean;
    private double standardDeviation;
    private Random random = new Random();

    public GaussianFunction(int mean, double standardDeviation)
    {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public int calculateFunctionOutput(int x)
    {
        double functionResult = this.random.nextGaussian() * this.standardDeviation + this.mean;

        functionResult += x;

        return (int) functionResult;
    }

}
