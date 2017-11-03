package com.poli.model.morphology.logic.operation;

import com.poli.model.morphology.logic.BooleanOperation;

public class XandOperation extends BooleanOperation
{

    public XandOperation()
    {
    }

    /**
     * Como a opera��o l�gica XAND n�o existe por defini��o, mas por indu��o, seu resultado seria identico � opera��o
     * booleana XNOR, ser� utilizado sua tabela verdade (1 0 0 1) .
     */
    @Override
    public int getOperation(int firstValue, int secondValue)
    {
        return ((firstValue == 0 && secondValue == 0) || (firstValue == 255 && secondValue == 255) ? 0 : 255);
    }

}
