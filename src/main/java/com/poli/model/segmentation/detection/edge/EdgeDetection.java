package com.poli.model.segmentation.detection.edge;

import java.awt.image.BufferedImage;

import com.poli.model.Image;
import com.poli.model.segmentation.threshold.util.ThresholdType;

public abstract class EdgeDetection
{
    private ThresholdType thresholdType;
    private Image image;
    private Image newImage;

    public EdgeDetection(Image image, ThresholdType threshold)
    {
        this.image = image;
        this.newImage = this.setNewImage();
        this.thresholdType = threshold;
    }

    public abstract Image detectEdges();

    public ThresholdType getThresholdType()
    {
        return thresholdType;
    }

    private Image setNewImage()
    {
        BufferedImage img = new BufferedImage(this.image.getCols(), this.image.getRows(),
                BufferedImage.TYPE_BYTE_BINARY);

        return new Image(img);
    }

    public Image getImage()
    {
        return image;
    }

    public Image getNewImage()
    {
        return newImage;
    }

}
