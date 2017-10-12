package com.poli.model.segmentation.threshold;

import java.util.HashMap;

import com.poli.model.Image;
import com.poli.model.segmentation.threshold.util.ImageThreshold;

public class OtsuThreshold extends ImageThreshold
{
    private HashMap<Integer, Integer> pixelDistribution;

    public OtsuThreshold(Image image)
    {
        super(image);
        this.generateImageHistogram();
    }

    private void generateImageHistogram()
    {

        this.pixelDistribution = new HashMap<Integer, Integer>();

        for (int i = 0; i < 256; i++)
        {
            this.pixelDistribution.put(i, 0);
        }

        for (int i = 0; i < this.getImage().getRows(); i++)
        {
            for (int j = 0; j < this.getImage().getCols(); j++)
            {
                int currentPixel = this.getImage().getPixel(i, j);

                int value = this.pixelDistribution.get(currentPixel);
                this.pixelDistribution.put(currentPixel, ++value);

            }
        }
    }

    @Override
    public int getThreshold()
    {
        int threshHold = 0;

        int totalPixel = this.getImage().getCols() * this.getImage().getRows();
        int totalSum = 0;

        for (int i = 0; i < 256; i++)
        {
            totalSum += i * this.pixelDistribution.get(i);
        }

        double weightBackground = 0;
        double weightForeground = 0;
        double maxVariance = 0;
        double currentSum = 0;

        for (int t = 0; t < 256; t++)
        {
            weightBackground += this.pixelDistribution.get(t);

            if (weightBackground > 0)
            {
                currentSum += (double) t * this.pixelDistribution.get(t);

                weightForeground = (totalPixel - weightBackground);

                if (weightForeground == 0)
                {
                    break;
                }
                double meanBackground = currentSum / weightBackground;
                double meanForeground = (totalSum - currentSum) / weightForeground;

                double beetweenVariance = weightBackground * weightForeground
                        * Math.pow(meanBackground - meanForeground, 2);

                if (beetweenVariance > maxVariance)
                {
                    maxVariance = beetweenVariance;
                    threshHold = t;
                }
            }
        }

        return threshHold;
    }

}
