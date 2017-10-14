package com.poli.model.segmentation.threshold;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.poli.model.Image;
import com.poli.model.segmentation.threshold.util.ImageThreshold;

public class OtsuThreshold extends ImageThreshold
{
    private HashMap<Integer, Integer> pixelDistribution;
    private boolean use2Threshold;

    public OtsuThreshold(Image image)
    {
        super(image);
        this.generateImageHistogram();
    }

    public OtsuThreshold(Image image, boolean use2Threshold)
    {
        this(image);
        this.use2Threshold = use2Threshold;
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

        return this.use2Threshold ? (threshHold / 2) : threshHold;
    }

    @Override
    public Image binarizeImage()
    {
        BufferedImage img = new BufferedImage(this.getImage().getCols(), this.getImage().getRows(),
                BufferedImage.TYPE_BYTE_BINARY);

        Image newImage = new Image(img);

        int threshold = this.getThreshold();

        if (this.use2Threshold)
        {
            threshold /= 2;
        }

        for (int row = 0; row < this.getImage().getRows(); row++)
        {
            for (int col = 0; col < this.getImage().getCols(); col++)
            {
                int pixelValue = this.getImage().getPixel(row, col);

                int color = -1;

                if (pixelValue >= threshold)
                {
                    color = 255;
                }
                else
                {
                    color = 0;
                }

                newImage.setPixel(row, col, color);

            }
        }

        return newImage;
    }

}
