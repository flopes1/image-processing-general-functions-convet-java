package com.poli.model.segmentation.detection.edge;

import com.poli.model.Image;

public class SobelMask
{
    private int[][] leftWeight;
    private int[][] rightWeight;
    private int rate;
    private double angle;

    public SobelMask(int rate, double angle)
    {
        this.rate = rate;
        this.angle = angle;
        this.setWeights();
    }

    private void setWeights()
    {
        if (this.rate == 3)
        {
            if (this.angle == 0)
            {
                this.leftWeight = new int[][]
                    {
                            { -1, -2, -1 },
                            { 0, 0, 0 },
                            { 1, 2, 1 } };
                this.rightWeight = new int[][]
                    {
                            { -1, 0, 1 },
                            { -2, 0, 2 },
                            { -1, 0, 1 } };
            }
            else if (this.angle == 45)
            {
                // TODO se der tempo implementar usando angulo
            }
            else if (this.angle == -45)
            {
                // TODO se der tempo implementar usando angulo
            }
        }
    }

    public double getLeftMaskResult(Image image)
    {
        return this.calculateMaskResult(image, this.leftWeight);
    }

    public double getRightMaskResult(Image image)
    {
        return this.calculateMaskResult(image, this.rightWeight);
    }

    private double calculateMaskResult(Image image, int[][] weights)
    {
        double result = 0;

        for (int i = 0; i < image.getRows(); i++)
        {
            for (int j = 0; j < image.getCols(); j++)
            {
                int pixel = image.getPixel(i, j);

                result += pixel * weights[i][j];
            }
        }

        return result;
    }

    public boolean isValidRegion(int row, int col, int rows, int cols)
    {
        int rateMid = (this.rate - 1) / 2;
        return (row > (rateMid - 1) && col > (rateMid - 1)) && (row < (rows - (rateMid))) && (col < (cols - (rateMid)));
    }
}
