package com.poli.model.filter.noise;

import com.poli.model.Image;

public class NoiseGenerator
{

    private NoiseType noise;

    public NoiseGenerator(NoiseType noise)
    {
        this.noise = noise;
    }

    public Image addNoise(Image inputImage)
    {
        for (int row = 0; row < inputImage.getRows(); row++)
        {
            for (int col = 0; col < inputImage.getCols(); col++)
            {
                int pixelValue = inputImage.getPixel(row, col);
                int newValue = this.noise.generateNoise(pixelValue);
                inputImage.setPixel(row, col, newValue);
            }
        }
        return inputImage;
    }

}
