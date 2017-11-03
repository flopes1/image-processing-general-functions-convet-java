package com.poli.model.morphology.set;

import com.poli.model.Image;

public abstract class SpacialTransformOperation
{

    public SpacialTransformOperation()
    {
    }

    public abstract Image applyTransformation(Image originalImage, int[][] structuringElement);

}
