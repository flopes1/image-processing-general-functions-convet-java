package com.poli.model.segmentation;

import java.util.HashMap;
import java.util.Map.Entry;

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

        int totalPixel = this.image.getCols() * this.image.getRows();

        double maxVariance = 0;

        for (int t = 0; t < 256; t++)
        {

            double weightBackground = 0;
            double weightForeground = 0;

            double meanBackground = 0;
            double meanForeground = 0;

            double meanBackDiv = 0;
            double meanForeDiv = 0;

            if (!this.pixelDistribution.containsKey(t))
            {
                continue;
            }

            if (t > 0)
            {
                for (int i = 0; i < t; i++)
                {
                    weightBackground += this.pixelDistribution.get(i);
                    meanBackground += (double) this.getKeyAsIndex(i) * this.pixelDistribution.get(i);
                    meanBackDiv += this.pixelDistribution.get(i);
                }

                weightBackground /= totalPixel;
                meanBackground /= meanBackDiv;
            }
            if (t < 256)
            {
                for (int i = t; i < 256; i++)
                {
                    weightForeground += this.pixelDistribution.get(i);
                    meanForeground += (double) this.getKeyAsIndex(i) * this.pixelDistribution.get(i);
                    meanForeDiv += this.pixelDistribution.get(i);
                }

                weightBackground /= totalPixel;
                meanBackground /= meanForeDiv;
            }

            double beetweenVariance = weightBackground * weightForeground
                    * Math.pow(meanBackground - meanForeground, 2);

            if (beetweenVariance > maxVariance)
            {
                maxVariance = beetweenVariance;
                threshHold = t;
            }
        }

        return threshHold;
    }

    private Integer getKeyAsIndex(int i)
    {
        Integer key = 0;

        int index = 0;

        for (Entry<Integer, Integer> element : this.pixelDistribution.entrySet())
        {
            if (index == i)
            {
                key = element.getKey();
                break;
            }
            index++;
        }

        return key;
    }

}
