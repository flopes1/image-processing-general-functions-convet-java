package com.poli.model.segmentation.threshold.util;

import com.poli.model.Image;

public abstract class ImageThreshold
{
    private Image image;

    public ImageThreshold(Image image)
    {
        this.image = image;
    }

    public abstract int getThreshold();

    public Image getImage()
    {
        return this.image;
    }

    public abstract Image binarizeImage();

}
