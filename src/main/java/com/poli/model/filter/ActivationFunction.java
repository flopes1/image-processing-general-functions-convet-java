package com.poli.model.filter;

import com.poli.model.filter.EnumFilterType.EnumFilter;
import com.poli.model.filter.EnumFilterType.Type;
import com.poli.model.util.math.ComplexNumber;

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
        else if(this.filter.equals(EnumFilter.BUTTERWORTH))
        {
            double scalar = 0;
            if (this.type.equals(Type.HIGH_PASS))
            {
                scalar = (double)diameter / distance;
            }
            else if (this.type.equals(Type.LOW_PASS))
            {
                scalar = distance / (double)diameter;
            }

            int n = 2;
            double scalarPow = Math.pow(scalar, 2 * n);
            result = 1.0 / (1 + scalarPow);
        }
        
        return result;
    }

}
