package com.poli.model.segmentation.threshold;

import java.util.ArrayList;
import java.util.HashMap;

import com.poli.model.Image;
import com.poli.model.segmentation.threshold.util.ImageThreshold;
import com.poli.model.util.ArrayUtils;

public class HistogramGroupThreshold extends ImageThreshold
{
    private ImageThreshold otsu;

    public HistogramGroupThreshold(Image image)
    {
        super(image);
        this.otsu = new OtsuThreshold(this.getImage());
    }

    @Override
    public int getThreshold()
    {
        int otsuThreshold = this.otsu.getThreshold();

        ArrayList<Integer> firstGroup = new ArrayList<Integer>();
        ArrayList<Integer> secondGroup = new ArrayList<Integer>();

        for (int row = 0; row < this.getImage().getRows(); row++)
        {
            for (int col = 0; col < this.getImage().getCols(); col++)
            {
                int pixel = this.getImage().getPixel(row, col);

                if (pixel > otsuThreshold)
                {
                    firstGroup.add(pixel);
                }
                else
                {
                    secondGroup.add(pixel);
                }
            }
        }

        double firstGroupPonderedMean = this.calculateGrayLevelMean(firstGroup);
        double secondGroupPonderedMean = this.calculateGrayLevelMean(secondGroup);

        double threshold = firstGroupPonderedMean > secondGroupPonderedMean ? this.getLocalThreshold(secondGroup)
                : this.getLocalThreshold(firstGroup);

        return (int) threshold;
    }

    private double getLocalThreshold(ArrayList<Integer> selectedGroup)
    {
        double value = ArrayUtils.getMinValue(selectedGroup);
        if (value == 0)
        {
            value = ArrayUtils.getMeanValue(selectedGroup);
        }
        return (int) value;
    }

    private double calculateGrayLevelMean(ArrayList<Integer> arrayList)
    {
        double totalSum = 0;
        double weight = 0;

        HashMap<Integer, Integer> localHistogram = this.generateLocalHistogram(arrayList);

        for (int i = 0; i < 256; i++)
        {
            if (i <= 10 || i >= 245)
                continue;

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
