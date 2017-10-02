package com.poli.model.filter.noise;

public abstract class NoiseType
{
    private EnumNoise noiseType;

    public NoiseType(EnumNoise noiseType)
    {
        this.noiseType = noiseType;
    }

    public EnumNoise getType()
    {
        return this.noiseType;
    }

    public abstract int generateNoise(int inputValue);

}
