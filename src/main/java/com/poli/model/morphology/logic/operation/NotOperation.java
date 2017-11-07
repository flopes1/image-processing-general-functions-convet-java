package com.poli.model.morphology.logic.operation;

import com.poli.model.morphology.logic.BooleanOperation;

public class NotOperation extends BooleanOperation
{

    public NotOperation()
    {
    }

    @Override
    public int getOperation(int firstValue, int secondValue)
    {
        return firstValue < 150 ? 255 : 0;
    }

}
