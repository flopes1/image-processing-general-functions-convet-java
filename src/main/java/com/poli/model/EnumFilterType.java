package com.poli.model;

public class EnumFilterType
{

    public enum EnumFilter
    {
        MEDIAN, MEAN, MODE, KUWAHARA
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
        else if(filter.equals(EnumFilter.KUWAHARA))
        {
        		return Type.ADAPTATIVE;
        }

        return null;

    }

}