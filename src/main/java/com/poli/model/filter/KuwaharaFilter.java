package com.poli.model.filter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class KuwaharaFilter
{

	private int rate;
	private double quadrantSize;
	private HashMap<Integer, List<Integer>> quadrantElements;
	private HashMap<Integer, Double> meanOfQuadrants;
	private HashMap<Integer, Double> standardDeviationQuadrants;

	public KuwaharaFilter(int rate)
	{
		this.setRate(rate);
		this.setQuadrantSize();
		this.quadrantElements = new HashMap<Integer, List<Integer>>();
		this.meanOfQuadrants = new HashMap<Integer, Double>();
		this.standardDeviationQuadrants = new HashMap<Integer, Double>();
	}

	public int calculateFilterResult(BufferedImage image)
	{
		int newValue = 0;

		this.calculateMeanOfKuwaharaQuadrants(image);
		this.calculateStandardDeviationOfKuwaharaQuadrants();

		int lower = 1;
		double lowerValue = this.standardDeviationQuadrants.get(lower);

		for (Entry<Integer, Double> current : this.standardDeviationQuadrants.entrySet())
		{
			if (current.getValue() <= lowerValue)
			{
				lowerValue = current.getValue();
				lower = current.getKey();
			}
		}

		newValue = this.meanOfQuadrants.get(lower).intValue();

		return newValue;
	}

	private void calculateStandardDeviationOfKuwaharaQuadrants()
	{
		double firstQuadrant = this.calculateStandartDeviation(this.getMeanOfQuadrants().get(1),
				this.getQuadrantElements().get(1));
		double secondQuadrant = this.calculateStandartDeviation(this.getMeanOfQuadrants().get(2),
				this.getQuadrantElements().get(2));
		double thirdQuadrant = this.calculateStandartDeviation(this.getMeanOfQuadrants().get(3),
				this.getQuadrantElements().get(3));
		double fourthQuadrant = this.calculateStandartDeviation(this.getMeanOfQuadrants().get(4),
				this.getQuadrantElements().get(4));

		this.standardDeviationQuadrants.put(1, firstQuadrant);
		this.standardDeviationQuadrants.put(2, secondQuadrant);
		this.standardDeviationQuadrants.put(3, thirdQuadrant);
		this.standardDeviationQuadrants.put(4, fourthQuadrant);
	}

	private double calculateStandartDeviation(Double mean, List<Integer> elements)
	{
		double standartDeviation = 0;

		for (Integer xi : elements)
		{
			standartDeviation += Math.pow(xi - mean, 2);
		}

		standartDeviation /= elements.size();
		standartDeviation = Math.sqrt(standartDeviation);

		return standartDeviation;
	}

	private void calculateMeanOfKuwaharaQuadrants(BufferedImage subimage)
	{
		int midRate = this.getRate() / 2;
		int firstCount = 0;
		int secondCount = 0;
		int thirdCount = 0;
		int fourthCount = 0;

		List<Integer> firstElements = new ArrayList<Integer>();
		List<Integer> secondElements = new ArrayList<Integer>();
		List<Integer> thirdElements = new ArrayList<Integer>();
		List<Integer> fourthElements = new ArrayList<Integer>();

		for (int i = 0; i < subimage.getHeight(); i++)
		{
			for (int j = 0; j < subimage.getWidth(); j++)
			{
				int pixel = subimage.getRGB(j, i);
				pixel = (pixel & 0x000000ff);

				if (i <= midRate && j <= midRate)
				{
					firstCount += pixel;
					firstElements.add(pixel);
				}
				if (i <= midRate && j >= midRate)
				{
					secondCount += pixel;
					secondElements.add(pixel);
				}
				if (i >= midRate && j <= midRate)
				{
					thirdCount += pixel;
					thirdElements.add(pixel);
				}
				if (i >= midRate && j >= midRate)
				{
					fourthCount += pixel;
					fourthElements.add(pixel);
				}
			}
		}

		this.meanOfQuadrants.put(1, (double) firstCount / quadrantSize);
		this.quadrantElements.put(1, firstElements);
		this.meanOfQuadrants.put(2, (double) secondCount / quadrantSize);
		this.quadrantElements.put(2, secondElements);
		this.meanOfQuadrants.put(3, (double) thirdCount / quadrantSize);
		this.quadrantElements.put(3, thirdElements);
		this.meanOfQuadrants.put(4, (double) fourthCount / quadrantSize);
		this.quadrantElements.put(4, fourthElements);

	}

	public int getRate()
	{
		return rate;
	}

	public void setRate(int rate)
	{
		this.rate = rate;
	}

	public double getQuadrantSize()
	{
		return quadrantSize;
	}

	public void setQuadrantSize()
	{
		this.quadrantSize = Math.pow(((this.getRate() / 2) + 1), 2);
	}

	public HashMap<Integer, List<Integer>> getQuadrantElements()
	{
		return quadrantElements;
	}

	public void setQuadrantElements(HashMap<Integer, List<Integer>> quadrantElements)
	{
		this.quadrantElements = quadrantElements;
	}

	public HashMap<Integer, Double> getMeanOfQuadrants()
	{
		return meanOfQuadrants;
	}

	public void setMeanOfQuadrants(HashMap<Integer, Double> meanOfQuadrants)
	{
		this.meanOfQuadrants = meanOfQuadrants;
	}

	public HashMap<Integer, Double> getStandardDeviationQuadrants()
	{
		return standardDeviationQuadrants;
	}

	public void setStandardDeviationQuadrants(HashMap<Integer, Double> standardDeviationQuadrants)
	{
		this.standardDeviationQuadrants = standardDeviationQuadrants;
	}

}
