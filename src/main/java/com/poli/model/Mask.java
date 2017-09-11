package com.poli.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.poli.model.EnumFilterType.EnumFilter;
import com.poli.model.EnumFilterType.Type;

public class Mask
{

	private int rate;
	private EnumFilter type;
	private int[][] weights;

	public Mask(int rate, EnumFilter median)
	{
		this.setRate(rate);
		this.setType(median);
		this.setWeights();
	}

	public int[][] getWeights()
	{
		return weights;
	}

	public void setWeights()
	{
		this.weights = new int[rate][rate];

		if (EnumFilterType.getEnumType(this.getType()).equals(Type.LOW_PASS)
				|| EnumFilterType.getEnumType(this.getType()).equals(Type.ADAPTATIVE))
		{
			for (int i = 0; i < this.rate; i++)
			{
				for (int j = 0; j < this.rate; j++)
				{
					this.weights[i][j] = 1;
				}
			}
		}

	}

	public int getRate()
	{
		return rate;
	}

	public void setRate(int rate)
	{
		this.rate = rate;
	}

	public EnumFilter getType()
	{
		return type;
	}

	public void setType(EnumFilter type)
	{
		this.type = type;
	}

	public boolean isValidRegion(int row, int col, int rows, int cols)
	{
		int rateMid = (this.rate / 2);
		return (row > (rateMid - 1)) && (col > (rateMid - 1)) && (row < (rows - rateMid)) && (col < (cols - rateMid));
	}

	public int calculateMaskResult(BufferedImage subimage)
	{
		int newValue = -1;

		if (this.type.equals(EnumFilter.KUWAHARA))
		{
			KuwaharaFilter kuwaharaFilter = new KuwaharaFilter(this.getRate());
			newValue = kuwaharaFilter.calculateFilterResult(subimage);
		}
		else
		{

			List<Integer> imagePixels = new ArrayList<Integer>();

			for (int i = 0; i < subimage.getHeight(); i++)
			{
				for (int j = 0; j < subimage.getWidth(); j++)
				{
					int pixel = subimage.getRGB(j, i);
					// imagem em tom de cinza, todos os componentes RGB possuem o mesmo valor
					pixel = (pixel & 0x000000ff);
					// multiplicando pelo peso da mascara
					pixel *= this.getWeights()[i][j];
					imagePixels.add(pixel);
				}
			}

			if (this.type.equals(EnumFilter.MEDIAN))
			{
				imagePixels = this.sortPixels(imagePixels);
			}

			newValue = this.getResult(imagePixels, this.type);
		}
		if (newValue == -1)
		{
			throw new RuntimeException("Failed to calculate the new pixel value");
		}

		return newValue;
	}

	private int getResult(List<Integer> imagePixels, EnumFilter type)
	{

		int newValue = 0;

		if (type.equals(EnumFilter.MEDIAN))
		{
			int len = imagePixels.size() / 2;
			newValue = imagePixels.get(len + 1);
		}
		else if (type.equals(EnumFilter.MEAN))
		{
			int sum = imagePixels.stream().mapToInt(i -> i.intValue()).sum();
			newValue = sum / imagePixels.size();
		}

		return newValue;
	}

	private List<Integer> sortPixels(List<Integer> imagePixels)
	{
		int[] elements = imagePixels.stream().mapToInt(i -> i).toArray();

		for (int i = 0; i < elements.length; i++)
		{
			for (int j = 1; j < elements.length - i; j++)
			{
				if (elements[j - 1] > elements[j])
				{
					int current = elements[j];
					elements[j] = elements[j - 1];
					elements[j - 1] = current;
				}
			}
		}

		return Arrays.stream(elements).boxed().collect(Collectors.toList());
	}

}
