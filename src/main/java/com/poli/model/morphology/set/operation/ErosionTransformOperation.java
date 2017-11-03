package com.poli.model.morphology.set.operation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.poli.model.Image;
import com.poli.model.morphology.set.SpacialTransformOperation;

public class ErosionTransformOperation extends SpacialTransformOperation
{

    public ErosionTransformOperation()
    {
        super();
    }

    @Override
    public Image applyTransformation(Image originalImage, int[][] structuringElement)
    {
        Image resultImage = new Image(originalImage.cloneImage());

        for (int row = 0; row < originalImage.getRows(); row++)
        {
            for (int col = 0; col < originalImage.getCols(); col++)
            {
                if (originalImage.getPixel(row, col) < 255)
                {
                    boolean isSubConjunt = this.checkSetSubconjunt(structuringElement, originalImage, row, col);

                    if (isSubConjunt)
                    {
                        resultImage.setPixel(row, col, 0);
                    }
                    else
                    {
                        resultImage.setPixel(row, col, 255);
                    }
                }
                else
                {
                    resultImage.setPixel(row, col, 255);
                }

            }
        }

        return resultImage;
    }

    private boolean checkSetSubconjunt(int[][] structuringElement, Image originalImage, int row, int col)
    {
        boolean isSubSet = true;

        int strucElemLen = structuringElement.length;
        int strucElemCenter = strucElemLen / 2;

        Point[] blackPoints = this.getBlackPoints(structuringElement, strucElemCenter);

        for (int i = 0; i < blackPoints.length; i++)
        {
            Point p = blackPoints[i];
            int checkX = row + p.x;
            int checkY = col + p.y;
            if (checkX < 0 || checkY < 0 || checkX >= originalImage.getRows() || checkY >= originalImage.getCols())
            {
                continue;
            }
            if (originalImage.getPixel(checkX, checkY) == 255)
            {
                isSubSet = false;
                break;
            }

        }

        return isSubSet;
    }

    private Point[] getBlackPoints(int[][] structuringElement, int center)
    {
        List<Point> pointsList = new ArrayList<Point>();

        for (int i = 0; i < structuringElement.length; i++)
        {
            for (int j = 0; j < structuringElement[i].length; j++)
            {
                if (structuringElement[i][j] == 0)
                {
                    Point point = new Point(i - center, j - center);
                    pointsList.add(point);
                }
            }
        }

        return pointsList.toArray(new Point[pointsList.size()]);
    }

}
