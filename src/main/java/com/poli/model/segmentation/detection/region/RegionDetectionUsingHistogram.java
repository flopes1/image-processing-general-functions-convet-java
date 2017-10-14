package com.poli.model.segmentation.detection.region;

import java.awt.image.BufferedImage;

import com.poli.model.Image;
import com.poli.model.segmentation.threshold.OtsuThreshold;

public class RegionDetectionUsingHistogram extends RegionDetection
{

    public RegionDetectionUsingHistogram(Image image)
    {
        super(image);
    }

    @Override
    public Image detectRegion()
    {
        BufferedImage img = new BufferedImage(this.getOriginalImage().getCols(), getOriginalImage().getRows(),
                BufferedImage.TYPE_BYTE_BINARY);

        Image newImage = new Image(img);

        Image smothImage = this.getFilter().applyMaxFilter(7);

        this.setImageThreshold(new OtsuThreshold(smothImage));
        // int threshold = this.getImageThreshold().getThreshold();
        smothImage = this.getImageThreshold().binarizeImage();
        //int middleThreshold = threshold / 2;
        //int lowerThreshold = threshold / 3;

        for (int row = 0; row < smothImage.getRows(); row++)
        {
            for (int col = 0; col < smothImage.getCols(); col++)
            {
                int pixel = smothImage.getPixel(row, col);

                int color = pixel;

                if (pixel > 230 || pixel < 20)
                {
                    // color = 0;
                }
                else
                {
                    // color = 255;
                }
                newImage.setPixel(row, col, color);
            }
        }

        return newImage;
    }

}
