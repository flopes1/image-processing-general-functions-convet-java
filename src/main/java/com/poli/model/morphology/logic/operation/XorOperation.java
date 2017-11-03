package com.poli.model.morphology.logic.operation;

import com.poli.model.morphology.logic.BooleanOperation;

public class XorOperation extends BooleanOperation
{

    public XorOperation()
    {
    }

    @Override
    public int getOperation(int firstValue, int secondValue)
    {
        return ((firstValue == 0 && secondValue == 255) || (firstValue == 255 && secondValue == 0)) ? 0 : 255;
    }

}
