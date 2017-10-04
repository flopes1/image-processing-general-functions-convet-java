package com.poli.model.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.poli.model.Image;
import com.poli.model.filter.EnumFilterType.EnumFilter;
import com.poli.model.filter.EnumFilterType.Type;

public class Mask
{

    private int rate;
    private EnumFilter type;
    private int[][] weights;
    private double q;

    public Mask(int rate, EnumFilter type)
    {
        this.setRate(rate);
        this.setType(type);
        this.setWeights();
    }

    public Mask(int rate, EnumFilter type, double q)
    {
        this(rate, type);
        this.q = q;
    }

    public int[][] getWeights()
    {
        return weights;
    }

    public void setWeights()
    {
        this.weights = new int[rate][rate];

        if (EnumFilter.GAUSSIAN.equals(this.type))
        {

            for (int i = 0; i < this.rate; i++)
            {
                for (int j = 0; j < this.rate; j++)
                {
                    if (i == 0 && j == 0 || i == this.rate - 1 && j == this.rate - 1 || i == 0 && j == this.rate - 1
                            || j == 0 && i == this.rate - 1)
                    {
                        this.weights[i][j] = 1;
                    }
                    else if (i == ((this.rate - 1) / 2) && j == ((this.rate - 1) / 2))
                    {
                        this.weights[i][j] = 4;
                    }
                    else
                    {
                        this.weights[i][j] = 2;
                    }
                }
            }

        }
        else if (EnumFilterType.getEnumType(this.getType()).equals(Type.LOW_PASS))
        {
            for (int i = 0; i < this.rate; i++)
            {
                for (int j = 0; j < this.rate; j++)
                {
                    this.weights[i][j] = 1;
                }
            }
        }
        else if (EnumFilterType.getEnumType(this.getType()).equals(Type.HIGH_PASS))
        {

            for (int i = 0; i < this.rate; i++)
            {
                for (int j = 0; j < this.rate; j++)
                {
                    if (((this.rate - 1) / 2) == i && i == j)
                    {
                        this.weights[i][j] = (this.rate * 2) - 1;
                    }
                    else if (i == ((this.rate - 1) / 2) || j == ((this.rate - 1) / 2))
                    {
                        this.weights[i][j] = -1;
                    }
                    else
                    {
                        this.weights[i][j] = 0;
                    }
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
        int rateMid = (this.rate - 1) / 2;
        return (row > (rateMid - 1) && col > (rateMid - 1)) && (row < (rows - (rateMid))) && (col < (cols - (rateMid)));
    }

    public int calculateMaskResult(Image image)
    {
        int newValue = -1;

        if (this.type.equals(EnumFilter.KUWAHARA))
        {
            KuwaharaFilter kuwaharaFilter = new KuwaharaFilter(this.getRate());
            newValue = kuwaharaFilter.calculateFilterResult(image);
        }
        else
        {
            List<Integer> imagePixels = new ArrayList<Integer>();

            for (int i = 0; i < image.getRows(); i++)
            {
                for (int j = 0; j < image.getCols(); j++)
                {
                    int pixel = image.getPixel(i, j);

                    pixel *= this.weights[j][i];
                    imagePixels.add(pixel);
                }
            }

            newValue = this.getResult(imagePixels);
        }
        if (newValue == -1)
        {
            throw new RuntimeException("Failed to calculate the new pixel value");
        }

        return newValue;
    }

    private int getResult(List<Integer> imagePixels)
    {
        int newValue = 0;

        if (this.type.equals(EnumFilter.MEDIAN))
        {
            imagePixels = this.sortPixels(imagePixels);
            int len = imagePixels.size() / 2;
            newValue = imagePixels.get(len + 1);
        }
        else if (this.type.equals(EnumFilter.MEAN))
        {
            int sum = imagePixels.stream().mapToInt(i -> i.intValue()).sum();
            newValue = sum / imagePixels.size();
        }
        else if (this.type.equals(EnumFilter.LAPLACIAN))
        {
            int sum = imagePixels.stream().mapToInt(i -> i.intValue()).sum();
            newValue = sum;
            newValue = this.normalizeValue(newValue);
        }
        else if (this.type.equals(EnumFilter.MAX))
        {
            imagePixels = this.sortPixels(imagePixels);
            newValue = imagePixels.get(imagePixels.size() - 1);
        }
        else if (this.type.equals(EnumFilter.MIN))
        {
            imagePixels = this.sortPixels(imagePixels);
            newValue = imagePixels.get(0);
        }
        else if (this.type.equals(EnumFilter.HARMONIC_MEAN))
        {
            double sum = imagePixels.stream().mapToDouble(i -> i.doubleValue() == 0 ? 0 : (1.0 / i.doubleValue()))
                    .sum();
            newValue = (int) ((double) imagePixels.size() / sum);
        }
        else if (this.type.equals(EnumFilter.CONTRA_HARMONIC_MEAN))
        {
            double sumUpper = imagePixels.stream().mapToDouble(i -> Math.pow(i.doubleValue(), this.q + 1)).sum();
            double sumLower = imagePixels.stream().mapToDouble(i -> Math.pow(i.doubleValue(), this.q)).sum();
            newValue = (int) (sumUpper / sumLower);
        }
        else if (this.type.equals(EnumFilter.GEOMETRIC_MEAN))
        {
            double prod = imagePixels.stream().reduce(1, (a, b) -> a * b);
            newValue = (int) Math.pow(prod, 1.0 / (double) imagePixels.size());
        }
        else if (this.type.equals(EnumFilter.POINT_MEAN))
        {
            imagePixels = this.sortPixels(imagePixels);
            int max = imagePixels.get(imagePixels.size() - 1);
            int min = imagePixels.get(0);

            newValue = (int) ((max + min) / 2);
        }
        else if (this.type.equals(EnumFilter.GAUSSIAN))
        {
            int sumWeights = 0;

            for (int i = 0; i < this.weights[0].length; i++)
            {
                for (int j = 0; j < this.weights.length; j++)
                {
                    sumWeights += this.weights[i][j];
                }
            }

            int values = imagePixels.stream().mapToInt(i -> i.intValue()).sum();
            newValue = values / sumWeights;
        }

        return newValue;
    }

    public int normalizeValue(int newValue)
    {
        int value = newValue;// (int) (((double) newValue - min) * (double) 255 / (max - min));

        if (value > 255)
        {
            value = 255;
        }
        else if (value < 0)
        {
            value = 0;
        }

        return value;
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
