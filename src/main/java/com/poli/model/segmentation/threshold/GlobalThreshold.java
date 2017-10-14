package com.poli.model.segmentation.threshold;

import java.util.ArrayList;
import java.util.HashMap;

import com.poli.model.Image;
import com.poli.model.segmentation.threshold.util.ImageThreshold;

public class GlobalThreshold extends ImageThreshold
{
    private HashMap<Integer, Integer> pixelDistribution;
    private int t0;

    public GlobalThreshold(Image image, int t0)
    {
        super(image);
        this.t0 = t0;
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
        double threshhold = 127;
        double oltT = 0;

        do
        {
            oltT = threshhold;
            ArrayList<Integer> firstGroup = new ArrayList<Integer>();
            ArrayList<Integer> secondGroup = new ArrayList<Integer>();

            for (int row = 0; row < this.getImage().getRows(); row++)
            {
                for (int col = 0; col < this.getImage().getCols(); col++)
                {
                    int pixel = this.getImage().getPixel(row, col);

                    if (pixel > threshhold)
                    {
                        firstGroup.add(pixel);
                    }
                    else
                    {
                        secondGroup.add(pixel);
                    }
                }
            }

            double firstGroupMean = this.calculateGrayLevelMean(firstGroup);
            double secondGroupMean = this.calculateGrayLevelMean(secondGroup);

            threshhold = (firstGroupMean + secondGroupMean) / 2.0;
        }
        while (Math.abs(threshhold - oltT) >= this.t0);

        return (int) threshhold;
    }

    private double calculateGrayLevelMean(ArrayList<Integer> arrayList)
    {
        double totalSum = 0;
        double weight = 0;

        HashMap<Integer, Integer> localHistogram = this.generateLocalHistogram(arrayList);

        for (int i = 0; i < 256; i++)
        {
            totalSum += i * localHistogram.get(i);
            weight += localHistogram.get(i);
        }
        return (int) (totalSum / weight);
    }

    private HashMap<Integer, Integer> generateLocalHistogram(ArrayList<Integer> arrayList)
    {
        HashMap<Integer, Integer> pixelDistribution = new HashMap<Integer, Integer>();

        for (int i = 0; i < 256; i++)
        {
            pixelDistribution.put(i, 0);
        }

        for (int i = 0; i < arrayList.size(); i++)
        {
            int pixelValue = arrayList.get(i);
            int value = pixelDistribution.get(pixelValue);
            pixelDistribution.put(pixelValue, ++value);

        }
        return pixelDistribution;
    }

    @Override
    public Image binarizeImage()
    {
        // TODO Auto-generated method stub
        return null;
    }


}
