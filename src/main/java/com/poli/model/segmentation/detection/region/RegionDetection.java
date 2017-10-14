package com.poli.model.segmentation.detection.region;

import com.poli.model.Image;
import com.poli.model.filter.Filter;
import com.poli.model.segmentation.detection.edge.EdgeDetection;
import com.poli.model.segmentation.detection.edge.SobelOperator;
import com.poli.model.segmentation.threshold.util.ImageThreshold;
import com.poli.model.segmentation.threshold.util.ThresholdType;

public abstract class RegionDetection
{
    private EdgeDetection edgeDetection;
    private ImageThreshold imageThreshold;
    private Filter filter;
    private Image originalImage;

    public RegionDetection(Image image)
    {
        this.originalImage = image;
        this.edgeDetection = new SobelOperator(image, ThresholdType.OTSU, false);
        this.filter = new Filter(this.originalImage, this.originalImage.cloneImage());
    }

    public abstract Image detectRegion();

    public EdgeDetection getEdgeDetection()
    {
        return edgeDetection;
    }

    public ImageThreshold getImageThreshold()
    {
        return imageThreshold;
    }

    public Filter getFilter()
    {
        return filter;
    }

    public Image getOriginalImage()
    {
        return originalImage;
    }

    public void setImageThreshold(ImageThreshold imageThreshold)
    {
        this.imageThreshold = imageThreshold;
    }
    
}
