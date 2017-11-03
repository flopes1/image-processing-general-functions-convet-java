package com.poli.model.morphology.set.operation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import com.poli.model.Image;
import com.poli.model.morphology.set.SpacialTransformOperation;

public class RegionFillingTransformOperation extends SpacialTransformOperation
{

    private DilationTransformOperation dilationTransformOperation;

    public RegionFillingTransformOperation()
    {
        this.dilationTransformOperation = new DilationTransformOperation();
    }

    /**
     * Essa implementação requer que a imagem de entrada seja a imagem obtida após a extração de fronteira
     * 
     * @param originalImage
     *            imagem com fronteiras
     * @param structuringElement
     * @return
     */
    @Override
    public Image applyTransformation(Image originalImage, int[][] structuringElement)
    {
        Image partialImage = new Image(originalImage.cloneImage());
        partialImage.clean();

        Point initialPoint = this.getFirstRolePoint(originalImage);
        ArrayList<Point> borderPoints = this.getBorderPoints(originalImage);

        partialImage.setPixel(initialPoint.x, initialPoint.y, 0);
        Image previousImage = new Image(partialImage.cloneImage());

        while (true)
        {
            partialImage = this.dilationTransformOperation.applyTransformation(partialImage, structuringElement);
            partialImage = this.inserctWithComplement(partialImage, borderPoints);

            if (partialImage.equals(previousImage))
            {
                break;
            }
            previousImage = partialImage.cloneImage();
        }

        return this.unionWithBorder(originalImage, partialImage);
    }

    private Image inserctWithComplement(Image partialImage, ArrayList<Point> borderPoints)
    {
        for (Point p : borderPoints)
        {
            partialImage.setPixel(p.x, p.y, 255);
        }

        return partialImage;
    }

    private Image unionWithBorder(Image originalImage, Image partialImage)
    {
        Image resultImage = new Image(originalImage.cloneImage());

        for (int row = 0; row < partialImage.getRows(); row++)
        {
            for (int col = 0; col < partialImage.getCols(); col++)
            {
                if (partialImage.getPixel(row, col) != 255)
                {
                    resultImage.setPixel(row, col, 0);
                }
            }
        }

        return resultImage;
    }

    private ArrayList<Point> getBorderPoints(Image image)
    {
        ArrayList<Point> borderPoints = new ArrayList<Point>();

        for (int row = 0; row < image.getRows(); row++)
        {
            for (int col = 0; col < image.getCols(); col++)
            {
                if (image.getPixel(row, col) != 255)
                {
                    borderPoints.add(new Point(row, col));
                }
            }
        }

        return borderPoints;
    }

    private Point getFirstRolePoint(Image image)
    {
        Point initialPoint = null;
        for (int row = 0; row < image.getRows(); row++)
        {
            for (int col = 0; col < image.getCols(); col++)
            {
                if (image.getPixel(row, col) == 0)
                {
                    if (row + 1 < image.getRows() && col + 1 < image.getCols())
                    {
                        if (image.getPixel(row + 1, col + 1) == 255)
                        {
                            initialPoint = new Point(row + 1, col + 1);
                            break;
                        }
                    }
                    else if (col + 1 < image.getCols())
                    {
                        if (image.getPixel(row, col + 1) == 255)
                        {
                            initialPoint = new Point(row, col + 1);
                            break;
                        }
                    }
                    else if (row + 1 < image.getRows())
                    {
                        if (image.getPixel(row + 1, col) == 255)
                        {
                            initialPoint = new Point(row + 1, col);
                            break;
                        }
                    }
                }
            }
            if (initialPoint != null)
            {
                break;
            }
        }
        return initialPoint;
    }

    private Image getComplementImage(Image image)
    {
        Image complementImage = new Image(image.cloneImage());

        for (int row = 0; row < image.getRows(); row++)
        {
            for (int col = 0; col < image.getCols(); col++)
            {
                if (image.getPixel(row, col) != 255)
                {
                    complementImage.setPixel(row, col, 255);
                }
                else
                {
                    complementImage.setPixel(row, col, 0);
                }
            }
        }

        return complementImage;
    }

}
