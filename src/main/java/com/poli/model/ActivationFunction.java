package com.poli.model;

import com.poli.model.EnumFilterType.EnumFilter;
import com.poli.model.EnumFilterType.Type;

public class ActivationFunction
{

    private EnumFilter filter;
    private Type type;

    public ActivationFunction(EnumFilter filter, Type type)
    {
        this.filter = filter;
        this.type = type;
    }

    public ComplexNumber activate(ComplexNumber number, double distance, int diameter)
    {
        double H = this.getActivationFunction(distance, diameter);

        return number.mult(H);
    }

    private double getActivationFunction(double distance, int diameter)
    {
        double result = 0;

        if (this.filter.equals(EnumFilter.DIAMETER))
        {
            if (this.type.equals(Type.HIGH_PASS))
            {
                result = distance <= diameter ? 0 : 1;
            }
            else if (this.type.equals(Type.LOW_PASS))
            {
                result = distance <= diameter ? 1 : 0;
            }

        }
        return result;
    }

}
