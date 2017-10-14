package com.poli.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayUtils
{

    private ArrayUtils()
    {
    }

    public static List<Integer> sortPixels(List<Integer> input)
    {
        int[] elements = input.stream().mapToInt(i -> i).toArray();

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

    public static int getMedianValue(List<Integer> input)
    {
        List<Integer> ordered = ArrayUtils.sortPixels(input);
        return ordered.get((input.size() - 1) / 2);
    }

    public static int getMeanValue(List<Integer> input)
    {
        int sum = input.stream().mapToInt(i -> i.intValue()).sum();
        return (int) (sum / (int) input.size());
    }

    public static int getMinValue(List<Integer> input)
    {
        int min = 256;

        for (Integer integer : input)
        {
            if (min > integer)
            {
                min = integer;
            }
        }

        return min;
    }

    public static int getMaxValue(List<Integer> input)
    {
        int max = 0;

        for (Integer integer : input)
        {
            if (integer > max)
            {
                max = integer;
            }
        }

        return max;
    }

    public static int getLocalizedMean(List<Integer> input)
    {
        input = ArrayUtils.sortPixels(input);
        double result = (input.get(0) + input.get(input.size() - 1)) / 2.0;
        return (int) result;
    }

    public static double getModeValue(ArrayList<Integer> selectedGroup)
    {
        int maxValue = 0, maxCount = 0;

        for (int i = 0; i < selectedGroup.size(); ++i)
        {
            int count = 0;
            for (int j = 0; j < selectedGroup.size(); ++j)
            {
                if (selectedGroup.get(j) == selectedGroup.get(i))
                {
                    ++count;
                }
            }
            if (count > maxCount)
            {
                maxCount = count;
                maxValue = selectedGroup.get(i);
            }
        }

        return maxValue;
    }

}
