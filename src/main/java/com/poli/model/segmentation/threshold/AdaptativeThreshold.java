package com.poli.model.segmentation.threshold;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.poli.model.Image;
import com.poli.model.segmentation.threshold.util.AdaptativeThreholdOperation;
import com.poli.model.segmentation.threshold.util.ImageThreshold;
import com.poli.model.util.ArrayUtils;

public class AdaptativeThreshold extends ImageThreshold
{
    private int windowSize;
    private AdaptativeThreholdOperation operationType;

    public AdaptativeThreshold(Image image)
    {
        super(image);
    }

    public AdaptativeThreshold(Image image, AdaptativeThreholdOperation operation, int windowSize)
    {
        super(image);
        this.windowSize = windowSize;
        this.operationType = operation;
    }

    @Override
    public int getThreshold()
    {
        throw new RuntimeException("Adaptative threshold does not have a local threshold value");
    }

    @Override
    public Image binarizeImage()
    {
        BufferedImage img = new BufferedImage(this.getImage().getCols(), this.getImage().getRows(),
                BufferedImage.TYPE_BYTE_BINARY);

        Image resultImage = new Image(img);

        int h = this.getImage().getRows();
        int w = this.getImage().getCols();
        int totalPixels = h * w;

        int S = w / 2;
        int s2 = S / 2;
        double thresh = 0.0;
        int localSum = 0;

        int[][] integralImage = new int[h][w];

        // calculo da integral da imagem
        for (int col = 0; col < w; col++)
        {
            localSum = 0;
            for (int row = 0; row < h; row++)
            {
                // integralImage[row][col] = this.getImageSum(0, row, 0, col);
                localSum += this.getImage().getPixel(row, col);

                if (col == 0)
                {
                    integralImage[row][col] = localSum;
                }
                else
                {
                    integralImage[row][col] = integralImage[row][col - 1] + localSum;
                }
            }
        }

        for (int col = 0; col < w; col++)
        {
            for (int row = 0; row < h; row++)
            {
                int y0 = Math.max(row - s2, 0);
                int y1 = Math.min(row + s2, h - 1);
                int x0 = Math.max(col - s2, 0);
                int x1 = Math.min(col + s2, w - 1);

                int count = (y1 - y0) * (x1 - x0);

                int sum = integralImage[y1][x1] - integralImage[y0][x1] - integralImage[y1][x0] + integralImage[y0][x0];

                if (this.getImage().getPixel(row, col) * count < (sum * (100.0 - thresh) / 100.0))
                {
                    resultImage.setPixel(row, col, 255);
                }
                else
                {
                    resultImage.setPixel(row, col, 0);
                }
            }
        }

        return resultImage;
    }

    private int getImageSum(int i, int frow, int j, int fcol)
    {
        int sum = 0;

        for (int col = j; col < fcol; col++)
        {
            for (int row = i; row < frow; row++)
            {
                sum += this.getImage().getPixel(row, col);
            }
        }

        return sum;
    }

    private int calculateOperation(Image subimage)
    {

        ArrayList<Integer> pixels = new ArrayList<Integer>();

        for (int col = 0; col < subimage.getCols(); col++)
        {
            for (int row = 0; row < subimage.getRows(); row++)
            {
                pixels.add(subimage.getPixel(row, col));
            }
        }

        int result = 0;

        if (this.operationType.equals(AdaptativeThreholdOperation.MEAN))
        {
            result = ArrayUtils.getMeanValue(pixels);
        }

        return result;
    }

    // public boolean isValidRegion(int row, int col, int rows, int cols)
    // {
    // int rateMid = (this.windowSize - 1) / 2;
    // return (row > (rateMid - 1) && col > (rateMid - 1)) && (row < (rows - (rateMid))) && (col < (cols - (rateMid)));
    // }

}
