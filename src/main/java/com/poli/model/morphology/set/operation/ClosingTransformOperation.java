package com.poli.model.morphology.set.operation;

import com.poli.model.Image;
import com.poli.model.morphology.set.SpacialTransformOperation;

public class ClosingTransformOperation extends SpacialTransformOperation
{

    // dilatação seguido por erosão
    private ErosionTransformOperation erosionTransformOperation;
    private DilationTransformOperation dilationTransformOperation;

    public ClosingTransformOperation()
    {
        super();
        this.erosionTransformOperation = new ErosionTransformOperation();
        this.dilationTransformOperation = new DilationTransformOperation();
    }

    @Override
    public Image applyTransformation(Image originalImage, int[][] structuringElement)
    {
        Image dilationResult = this.dilationTransformOperation.applyTransformation(originalImage, structuringElement);
        return this.erosionTransformOperation.applyTransformation(dilationResult, structuringElement);
    }

}
