package com.poli.model.morphology.logic.operation;

import com.poli.model.morphology.logic.BooleanOperation;

public class AndOperation extends BooleanOperation
{

    public AndOperation()
    {
    }

    @Override
    public int getOperation(int firstValue, int secondValue)
    {
        return (firstValue == 0 && secondValue == 0) ? 0 : 255;
    }

}
