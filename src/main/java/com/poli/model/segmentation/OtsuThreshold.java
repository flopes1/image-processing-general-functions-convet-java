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

		for (int i = 0; i < 256; i++)
		{
			this.pixelDistribution.put(i, 0);
		}

		for (int i = 0; i < this.image.getRows(); i++)
		{
			for (int j = 0; j < this.image.getCols(); j++)
			{
				int currentPixel = this.image.getPixel(i, j);

				int value = this.pixelDistribution.get(currentPixel);
				this.pixelDistribution.put(currentPixel, ++value);

			}
		}
	}

	public int getOtsuThreshold()
	{
		int threshHold = 0;

		int totalPixel = this.image.getCols() * this.image.getRows();
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
