package com.poli.model.morphology.set.operation;

import com.poli.model.Image;
import com.poli.model.morphology.set.SpacialTransformOperation;

public class OpenningTransformOperation extends SpacialTransformOperation
{
    // Erosão seguido por dilatação
    private ErosionTransformOperation erosionTransformOperation;
    private DilationTransformOperation dilationTransformOperation;

    public OpenningTransformOperation()
    {
        super();
        this.erosionTransformOperation = new ErosionTransformOperation();
        this.dilationTransformOperation = new DilationTransformOperation();
    }

    @Override
    public Image applyTransformation(Image originalImage, int[][] structuringElement)
    {
        Image erosionResult = this.erosionTransformOperation.applyTransformation(originalImage, structuringElement);
        return this.dilationTransformOperation.applyTransformation(erosionResult, structuringElement);
    }

}
