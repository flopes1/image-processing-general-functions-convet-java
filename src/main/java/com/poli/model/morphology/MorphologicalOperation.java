package com.poli.model.morphology;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.poli.model.Image;
import com.poli.model.morphology.common.EnumMorphologyOperation;
import com.poli.model.morphology.logic.BooleanOperation;
import com.poli.model.morphology.set.SpacialTransformOperation;
import com.poli.model.util.ImageUtils;

public abstract class MorphologicalOperation
{
    private Image originalImage;
    private Image newImage;
    private BooleanOperation booleanOperation;
    private SpacialTransformOperation spacialTransformOperation;
    private EnumMorphologyOperation morphologyOperation;

    public MorphologicalOperation(String image, EnumMorphologyOperation morphologyOperation)
    {
        this.loadImage(image);
        this.morphologyOperation = morphologyOperation;
    }

    public MorphologicalOperation(Image image)
    {
        this.originalImage = image;
    }

    public abstract void applyOperation(Image otherImage);

    public SpacialTransformOperation getSpacialTransformOperation()
    {
        return spacialTransformOperation;
    }

    public void setSpacialTransformOperation(SpacialTransformOperation spacialTransformOperation)
    {
        this.spacialTransformOperation = spacialTransformOperation;
    }

    public void setNewImage(Image image)
    {
        this.newImage = image;
    }

    public Image getOriginalImage()
    {
        return this.originalImage;
    }

    public Image getNewImage()
    {
        return newImage;
    }

    public BooleanOperation getBooleanOperation()
    {
        return booleanOperation;
    }

    public void setBooleanOperation(BooleanOperation booleanOperation)
    {
        this.booleanOperation = booleanOperation;
    }

    public EnumMorphologyOperation getMorphologyOperation()
    {
        return morphologyOperation;
    }

    private void loadImage(String imagePath)
    {
        try
        {
            Image image = ImageUtils.loadImage(imagePath);
            this.setOriginalImage(image);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void setOriginalImage(Image originalImage)
    {
        this.originalImage = originalImage;
    }

    public void showOriginalImage()
    {
        this.originalImage.showImage();
    }

    public void showNewImage()
    {
        this.newImage.showImage();
    }

    public void saveResult(String path)
    {
        String extension = path.substring(path.lastIndexOf('.') + 1);

        File outputfile = new File(path);
        try
        {
            ImageIO.write(this.newImage.getSource(), extension, outputfile);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

}
