package com.poli.model.morphology.logic.operation;

import com.poli.model.morphology.logic.BooleanOperation;

public class XandOperation extends BooleanOperation
{

    public XandOperation()
    {
    }

    /**
     * Como a operação lógica XAND não existe por definição, mas por indução, seu resultado seria identico à operação
     * booleana XNOR, será utilizado sua tabela verdade (1 0 0 1) .
     */
    @Override
    public int getOperation(int firstValue, int secondValue)
    {
        return ((firstValue == 0 && secondValue == 0) || (firstValue == 255 && secondValue == 255) ? 0 : 255);
    }

}
