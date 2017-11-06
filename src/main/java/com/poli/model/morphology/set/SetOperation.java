package com.poli.model.morphology.set;

import com.poli.model.Image;
import com.poli.model.morphology.MorphologicalOperation;
import com.poli.model.morphology.common.EnumMorphologyConnection;
import com.poli.model.morphology.common.EnumMorphologyOperation;
import com.poli.model.morphology.common.StructuringElements;
import com.poli.model.morphology.set.operation.ClosingTransformOperation;
import com.poli.model.morphology.set.operation.DilationTransformOperation;
import com.poli.model.morphology.set.operation.ErosionTransformOperation;
import com.poli.model.morphology.set.operation.ExtractionTransformOperation;
import com.poli.model.morphology.set.operation.OpenningTransformOperation;
import com.poli.model.morphology.set.operation.ReductionTransformOperation;
import com.poli.model.morphology.set.operation.RegionFillingTransformOperation;

public class SetOperation extends MorphologicalOperation
{

    private EnumMorphologyConnection connectionType;

    public SetOperation(String image, EnumMorphologyOperation morphologyOperation,
            EnumMorphologyConnection connectionType)
    {
        super(image, morphologyOperation);
        this.setConnectionType(connectionType);
        this.buildSpacialTransform(morphologyOperation);
    }

    private void buildSpacialTransform(EnumMorphologyOperation morphologyOperation)
    {
        if (EnumMorphologyOperation.EROSION.equals(morphologyOperation))
        {
            this.setSpacialTransformOperation(new ErosionTransformOperation());
        }
        else if (EnumMorphologyOperation.DILATION.equals(morphologyOperation))
        {
            this.setSpacialTransformOperation(new DilationTransformOperation());
        }
        else if (EnumMorphologyOperation.CLOSING.equals(morphologyOperation))
        {
            this.setSpacialTransformOperation(new ClosingTransformOperation());
        }
        else if (EnumMorphologyOperation.OPENNING.equals(morphologyOperation))
        {
            this.setSpacialTransformOperation(new OpenningTransformOperation());
        }
        else if (EnumMorphologyOperation.EXTRACTION.equals(morphologyOperation))
        {
            this.setSpacialTransformOperation(new ExtractionTransformOperation());
        }
        else if (EnumMorphologyOperation.FILLING.equals(morphologyOperation))
        {
            this.setSpacialTransformOperation(new RegionFillingTransformOperation());
        }
        else if (EnumMorphologyOperation.REDUCTION.equals(morphologyOperation))
        {
             this.setSpacialTransformOperation(new ReductionTransformOperation());
        }
    }

    private void setConnectionType(EnumMorphologyConnection connectionType)
    {
        this.connectionType = connectionType;
    }

    public void applyOperation()
    {
        this.applyOperation(this.getOriginalImage());
    }

    @Override
    public void applyOperation(Image otherImage)
    {
        int[][] structuringElement = null;

        if (EnumMorphologyConnection.FOUR.equals(this.connectionType))
        {
            structuringElement = StructuringElements.fourConnected;
        }
        else
        {
            structuringElement = StructuringElements.eightConnected;
        }

        Image newImage = this.getSpacialTransformOperation().applyTransformation(this.getOriginalImage(),
                structuringElement);
        this.setNewImage(newImage);
    }

}
