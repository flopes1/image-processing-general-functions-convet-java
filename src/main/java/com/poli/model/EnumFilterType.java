package com.poli.model;

public class EnumFilterType
{

    public enum EnumFilter
    {
        MEDIAN, MEAN, MODE, KUWAHARA, LAPLACIAN, HIGH_BOOST
    };

    public enum Type
    {
        HIGH_PASS, LOW_PASS, ADAPTATIVE
    };

    public static Type getEnumType(EnumFilter filter)
    {

        if (filter.equals(EnumFilter.MEDIAN) || filter.equals(EnumFilter.MEAN) || filter.equals(EnumFilter.MODE))
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

        return null;

    }

}