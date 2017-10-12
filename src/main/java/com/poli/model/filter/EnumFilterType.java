package com.poli.model.filter;

public class EnumFilterType
{

    public enum EnumFilter
    {
        MEDIAN, MEAN, MODE, KUWAHARA, LAPLACIAN, HIGH_BOOST, DIAMETER, BUTTERWORTH, HARMONIC_MEAN, CONTRA_HARMONIC_MEAN, POINT_MEAN, GEOMETRIC_MEAN, MAX, MIN, GAUSSIAN, SOBEL
    };

    public enum Type
    {
        HIGH_PASS, LOW_PASS, ADAPTATIVE, DETECTION
    };

    public static Type getEnumType(EnumFilter filter)
    {

        if (filter.equals(EnumFilter.MEDIAN) || filter.equals(EnumFilter.MEAN) || filter.equals(EnumFilter.MODE)
                || filter.equals(EnumFilter.HARMONIC_MEAN) || filter.equals(EnumFilter.CONTRA_HARMONIC_MEAN)
                || filter.equals(EnumFilter.MAX) || filter.equals(EnumFilter.GEOMETRIC_MEAN)
                || filter.equals(EnumFilter.MIN) || filter.equals(EnumFilter.POINT_MEAN)
                || filter.equals(EnumFilter.GAUSSIAN))
        {
            return Type.LOW_PASS;
        }
        else if (filter.equals(EnumFilter.KUWAHARA))
        {
            return Type.ADAPTATIVE;
        }
        else if (filter.equals(EnumFilter.LAPLACIAN))
        {
            return Type.HIGH_PASS;
        }
        else if (filter.equals(EnumFilter.SOBEL))
        {
            return Type.DETECTION;
        }

        return null;

    }

}