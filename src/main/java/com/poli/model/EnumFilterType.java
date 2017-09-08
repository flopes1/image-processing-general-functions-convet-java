package com.poli.model;

public class EnumFilterType
{

    public enum EnumFilter
    {
        MEDIAN, MEAN, MODE
    };

    public enum Type
    {
        HIGH_PASS, LOW_PASS
    };

    public static Type getEnumType(EnumFilter filter)
    {

        if (filter.equals(EnumFilter.MEDIAN) || filter.equals(EnumFilter.MEAN) || filter.equals(EnumFilter.MODE))
        {
            return Type.LOW_PASS;
        }

        return null;

    }

}