package com.poli.model.representation;

import com.poli.model.Image;
import com.poli.model.representation.common.EnumRepresentationType;

public abstract class ImageRepresentation
{

    private Image originalImage;
    private Image resultImage;
    private EnumRepresentationType enumRepresentationType;

    public ImageRepresentation(Image originalImage, EnumRepresentationType enumRepresentationType)
    {
        this.originalImage = originalImage;
        this.enumRepresentationType = enumRepresentationType;
        this.setResultImage();
    }

    public abstract void generateImageRepresentation();

    private void setResultImage()
    {
        this.resultImage = new Image(this.originalImage.cloneImage());
        this.resultImage.clean();
    }

    public Image getResultImage()
    {
        return resultImage;
    }

    public void setResultImage(Image resultImage)
    {
        this.resultImage = resultImage;
    }

    public Image getOriginalImage()
    {
        return originalImage;
    }

    public EnumRepresentationType getEnumRepresentationType()
    {
        return enumRepresentationType;
    }

}
