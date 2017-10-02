package com.poli.model.filter;

public class EnumFilterType
{

    public enum EnumFilter
    {
        MEDIAN, MEAN, MODE, KUWAHARA, LAPLACIAN, HIGH_BOOST, DIAMETER, BUTTERWORTH, HARMONIC_MEAN, COUNTER_HARMONIC_MEAN, MAX, MIN
    };

    public enum Type
    {
        HIGH_PASS, LOW_PASS, ADAPTATIVE
    };

    public static Type getEnumType(EnumFilter filter)
    {

        if (filter.equals(EnumFilter.MEDIAN) || filter.equals(EnumFilter.MEAN) || filter.equals(EnumFilter.MODE)
                || filter.equals(EnumFilter.HARMONIC_MEAN) || filter.equals(EnumFilter.COUNTER_HARMONIC_MEAN)
                || filter.equals(EnumFilter.MAX))
        {
            return Type.LOW_PASS;
        }
        else if (filter.equals(EnumFilter.KUWAHARA))
        {
            return Type.ADAPTATIVE;
        }
        else if (filter.equals(EnumFilter.LAPLACIAN) || filter.equals(EnumFilter.MIN))
        {
            return Type.HIGH_PASS;
        }

        return null;

    }

}