package com.poli.model.filter.noise;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNoise extends NoiseType
{
    private double noiseOccurPercent;
    private int initialRange;
    private int finalRange;

    public RandomNoise(EnumNoise noiseType, int noiseOccurPercent, int initialRange, int finalRange)
    {
        super(noiseType);
        this.noiseOccurPercent = noiseOccurPercent;
        this.finalRange = finalRange;
        this.initialRange = initialRange;
    }

    @Override
    public int generateNoise(int inputValue)
    {
        int newValue = inputValue;

        int percent = ThreadLocalRandom.current().nextInt(0, 100 + 1);

        if (this.initialRange == this.finalRange)
        {
            newValue = this.getNewValueEqual(inputValue, percent);
        }
        else
        {
            newValue = this.getNewValue(inputValue, percent);
        }

        return newValue;
    }

    private int getNewValue(int inputValue, int percent)
    {
        int newValue = inputValue;

        if (percent <= this.noiseOccurPercent)
        {
            newValue = this.initialRange;
        }
        else if (percent > this.noiseOccurPercent && percent <= (this.noiseOccurPercent * 2))
        {
            newValue = this.finalRange;
        }

        return newValue;
    }

    private int getNewValueEqual(int inputValue, int percent)
    {
        int newValue = inputValue;

        // Se evento ocorrer
        if (percent <= this.noiseOccurPercent)
        {
            newValue = this.initialRange;
        }

        return newValue;
    }

    public double getNoisePercent()
    {
        return noiseOccurPercent;
    }

    public int getInitialRange()
    {
        return initialRange;
    }

    public int getFinalRange()
    {
        return finalRange;
    }

}
