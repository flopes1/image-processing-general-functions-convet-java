package com.poli.model.morphology.logic.operation;

import com.poli.model.morphology.logic.BooleanOperation;

public class OrOperation extends BooleanOperation
{

    public OrOperation()
    {
        super();
    }

    @Override
    public int getOperation(int firstValue, int secondValue)
    {
        return (firstValue == 0 || secondValue == 0) ? 0 : 255;
    }

}
