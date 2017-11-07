package com.poli.model.morphology.logic;

import com.poli.model.Image;
import com.poli.model.morphology.MorphologicalOperation;
import com.poli.model.morphology.common.EnumMorphologyOperation;
import com.poli.model.morphology.logic.operation.AndOperation;
import com.poli.model.morphology.logic.operation.NotOperation;
import com.poli.model.morphology.logic.operation.OrOperation;
import com.poli.model.morphology.logic.operation.XandOperation;
import com.poli.model.morphology.logic.operation.XorOperation;

public class LogicOperation extends MorphologicalOperation
{

    public LogicOperation(String image, EnumMorphologyOperation morphologyOperation)
    {
        super(image, morphologyOperation);
        this.setBooleanOperation(morphologyOperation);
    }

    private void setBooleanOperation(EnumMorphologyOperation morphologyOperation)
    {
        if (EnumMorphologyOperation.AND.equals(morphologyOperation))
        {
            this.setBooleanOperation(new AndOperation());
        }
        else if (EnumMorphologyOperation.OR.equals(morphologyOperation))
        {
            this.setBooleanOperation(new OrOperation());
        }
        else if (EnumMorphologyOperation.XOR.equals(morphologyOperation))
        {
            this.setBooleanOperation(new XorOperation());
        }
        else if (EnumMorphologyOperation.XAND.equals(morphologyOperation))
        {
            this.setBooleanOperation(new XandOperation());
        }
        else if (EnumMorphologyOperation.NOT.equals(morphologyOperation))
        {
            this.setBooleanOperation(new NotOperation());
        }
    }

    @Override
    public void applyOperation(Image otherImage)
    {
        Image resultImage = new Image(this.getOriginalImage().cloneImage());

        for (int row = 0; row < resultImage.getRows(); row++)
        {
            for (int col = 0; col < resultImage.getCols(); col++)
            {
                int a = this.getOriginalImage().getPixel(row, col);
                int b = otherImage.getPixel(row, col);
                int result = this.getBooleanOperation().getOperation(a, b);
                resultImage.setPixel(row, col, result);
            }
        }

        this.setNewImage(resultImage);
    }

}
