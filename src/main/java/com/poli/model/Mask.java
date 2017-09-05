package com.poli.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Mask
{

    private int rate;
    private EnumFilterType type;

    public Mask(int rate, EnumFilterType type)
    {
        this.setRate(rate);
        this.setType(type);
    }

    public int getRate()
    {
        return rate;
    }

    public void setRate(int rate)
    {
        this.rate = rate;
    }

    public EnumFilterType getType()
    {
        return type;
    }

    public void setType(EnumFilterType type)
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

        List<Integer> imagePixels = new ArrayList<Integer>();

        for (int i = 0; i < subimage.getHeight(); i++)
        {
            for (int j = 0; j < subimage.getWidth(); j++)
            {
                int pixel = subimage.getRGB(j, i);
                // imagem em tom de cinza, todos os componentes RGB possuem o mesmo valor
                pixel = (pixel & 0x000000ff);
                imagePixels.add(pixel);
            }
        }

        imagePixels = this.sortPixels(imagePixels);

        return this.getResult(imagePixels, this.type);
    }

    private int getResult(List<Integer> imagePixels, EnumFilterType type)
    {
        int len = imagePixels.size() / 2;

        int newValue = 0;

        if (type.equals(EnumFilterType.MEDIAN))
        {
            newValue = imagePixels.get(len + 1);
        }
        else if (type.equals(EnumFilterType.MEAN))
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
