package com.poli.model.segmentation.detection.edge;

import com.poli.model.Image;
import com.poli.model.segmentation.threshold.HistogramGroupThreshold;
import com.poli.model.segmentation.threshold.AdaptativeThreshold;
import com.poli.model.segmentation.threshold.GlobalThreshold;
import com.poli.model.segmentation.threshold.OtsuThreshold;
import com.poli.model.segmentation.threshold.util.ImageThreshold;
import com.poli.model.segmentation.threshold.util.ThresholdType;

public class SobelOperator extends EdgeDetection
{
    private SobelMask mask;
    private ImageThreshold imageThreshold;

    public SobelOperator(Image image, ThresholdType threshold, boolean use2ClassThreshold)
    {
        super(image, threshold);
        this.setImageThreshold(use2ClassThreshold);
    }

    private void setImageThreshold(boolean use2ClassThreshold)
    {
        if (this.getThresholdType().equals(ThresholdType.OTSU))
        {
            this.imageThreshold = new OtsuThreshold(this.getImage(), use2ClassThreshold);
        }
        else if (this.getThresholdType().equals(ThresholdType.GLOBAL))
        {
            this.imageThreshold = new GlobalThreshold(this.getImage(), 5);
        }
        else if (this.getThresholdType().equals(ThresholdType.HISTOGRAM_GROUP))
        {
            this.imageThreshold = new HistogramGroupThreshold(this.getImage());
        }
        else if (this.getThresholdType().equals(ThresholdType.ADAPTATIVE))
        {
            this.imageThreshold = new AdaptativeThreshold(this.getImage());
        }
    }

    @Override
    public Image detectEdges()
    {
        int threshold = this.imageThreshold.getThreshold();
        this.mask = new SobelMask(3, 0);

        int rate = 3;
        int rateMid = (rate - 1) / 2;

        for (int row = 0; row < this.getImage().getRows(); row++)
        {
            for (int col = 0; col < this.getImage().getCols(); col++)
            {
                if (this.mask.isValidRegion(row, col, this.getImage().getRows(), this.getImage().getCols()))
                {
                    double leftOperator = this.mask
                            .getLeftMaskResult(this.getImage().getSubimage(row - rateMid, col - rateMid, rate, rate));

                    double rightOperator = this.mask
                            .getRightMaskResult(this.getImage().getSubimage(row - rateMid, col - rateMid, rate, rate));

                    double result = Math.sqrt(Math.pow(leftOperator, 2) + Math.pow(rightOperator, 2));

                    int color = (int)result;

                    if (result >= threshold)
                    {
                        color = 255;
                    }
                    else
                    {
                        color = 0;
                    }

                    this.getNewImage().setPixel(row, col, color);
                }
                else
                {
                    // Tratamento de para linha e coluna inicial e final da imagem
                    this.getNewImage().setPixel(row, col, 0);
                }
            }
        }

        return this.getNewImage();
    }
}
