package com.poli.model.segmentation;

import java.util.HashMap;

import com.poli.model.Image;

public class OtsuThreshold
{

    private HashMap<Integer, Integer> pixelDistribution;
    private Image image;

    public OtsuThreshold(Image image)
    {
        this.image = image;
        this.generateImageHistogram();
    }

    private void generateImageHistogram()
    {
        this.pixelDistribution = new HashMap<Integer, Integer>();

        for (int i = 0; i < this.image.getRows(); i++)
        {
            for (int j = 0; j < this.image.getCols(); j++)
            {
                int currentPixel = this.image.getPixel(i, j);

                if (this.pixelDistribution.containsKey(currentPixel))
                {
                    int value = this.pixelDistribution.get(currentPixel);
                    this.pixelDistribution.put(currentPixel, ++value);
                }
                else
                {
                    this.pixelDistribution.put(currentPixel, 1);
                }

            }
        }
    }

    public int getOtsuThreshold()
    {
        int threshHold = 0;

        float weightB = 0;
        float weightF = 0;
        int totalPixel = this.image.getCols() * this.image.getRows();
        
        float withinClassVariance = 0;
        
        for(int t = 0; t < 256; t++)
        {
            
            
            
            
            
        }
        
        
        return threshHold;
    }

}
