package com.poli.model.representation.type.chain;

import java.awt.Point;
import java.util.ArrayList;

import com.poli.model.Image;
import com.poli.model.morphology.common.StructuringElements;
import com.poli.model.morphology.set.operation.ExtractionTransformOperation;
import com.poli.model.morphology.set.operation.OpenningTransformOperation;
import com.poli.model.representation.ImageRepresentation;
import com.poli.model.representation.common.EnumRepresentationType;

public class ChainCodeRepresentation extends ImageRepresentation
{

    private ArrayList<ChainPoint> chainBorderElements;
    private ArrayList<Point> imageBorderElements;
    private EnumChainDirectionType enumChainDirectionType;

    private OpenningTransformOperation openningTransformOperation;
    private ExtractionTransformOperation extractionTransformOperation;

    public ChainCodeRepresentation(Image originalImage, EnumChainDirectionType enumChainDirectionType)
    {
        super(originalImage, EnumRepresentationType.CHAIN_CODE);
        this.enumChainDirectionType = enumChainDirectionType;
        this.chainBorderElements = new ArrayList<ChainPoint>();
        this.imageBorderElements = new ArrayList<Point>();
        this.openningTransformOperation = new OpenningTransformOperation();
        this.extractionTransformOperation = new ExtractionTransformOperation();
    }

    @Override
    public void generateImageRepresentation()
    {
        Image image = this.calculateImageBorderPoints();

        int gridSpliterX = (int) (this.getOriginalImage().getRows() * 0.1);
        int gridSpliterY = (int) (this.getOriginalImage().getCols() * 0.1);

        for (int i = gridSpliterX; i < image.getRows(); i += gridSpliterX)
        {
            for (int j = gridSpliterY; j < image.getCols(); j += gridSpliterY)
            {
                for (int row = (i - gridSpliterX); row < i; row++)
                {
                    for (int col = (j - gridSpliterY); col < j; col++)
                    {
                        if (image.getPixel(row, col) == 0)
                        {
                            Point newPoint = null;

                            if (this.imageBorderElements.isEmpty())
                            {
                                newPoint = this.normalizeAndGenerateNewPointSimple(row, col, i, j, gridSpliterX,
                                        gridSpliterY);
                                this.imageBorderElements.add(newPoint);
                            }
                            else
                            {
                                Point previousPoint = this.imageBorderElements.get(this.imageBorderElements.size() - 2);

                                newPoint = this.normalizeAndGenerateNewPoint(row, col, i, j, gridSpliterX, gridSpliterY,
                                        previousPoint, this.enumChainDirectionType);

                                if (!newPoint.equals(previousPoint))
                                {
                                    this.imageBorderElements.add(newPoint);
                                }
                            }
                        }
                    }
                }
            }
        }
        this.buildSamplingImage();
    }

    private Point normalizeAndGenerateNewPoint(int row, int col, int i, int j, int gridSpliterX, int gridSpliterY,
            Point previousPoint, EnumChainDirectionType enumChainDirectionType)
    {
        Point newPoint = null;

        Point aux = this.normalizeAndGenerateNewPointSimple(row, col, i, j, gridSpliterX, gridSpliterY);

        if (EnumChainDirectionType.EIGHT_DIRETION.equals(enumChainDirectionType))
        {
            newPoint = aux;
        }
        else
        {

        }

        return newPoint;
    }

    private Point normalizeAndGenerateNewPointSimple(int row, int col, int i, int j, int gridSpliterX, int gridSpliterY)
    {
        int x = 0;
        int y = 0;

        int minX = i - gridSpliterX;
        int maxX = i;

        int minY = j - gridSpliterY;
        int maxY = j;

        if ((row - minX) > (maxX - row))
        {
            x = maxX;
        }
        else
        {
            x = minX;
        }

        if ((col - minY) > (maxY - col))
        {
            y = maxY;
        }
        else
        {
            y = minY;
        }

        return new Point(x, y);
    }

    private void buildSamplingImage()
    {
        for (Point point : this.imageBorderElements)
        {
            this.getResultImage().setPixel(point.x, point.y, 0);
        }
    }

    private Image calculateImageBorderPoints()
    {
        Image image = this.getOriginalImage().cloneImage();
        image = this.openningTransformOperation.applyTransformation(image, StructuringElements.eightConnected);
        image = this.extractionTransformOperation.applyTransformation(image, StructuringElements.eightConnected);

        return image;

        /*
         * Point initialPoint = this.getImageInitialBorderPoint(image); Point currentPoint = new Point(initialPoint.x,
         * initialPoint.y); this.imageBorderElements.add(initialPoint); while (true) { Point nextChainPoint =
         * this.getAdjacentClockWisePoint(image, currentPoint); if (nextChainPoint.equals(initialPoint)) { break; }
         * this.imageBorderElements.add(nextChainPoint); currentPoint = new Point(nextChainPoint.x, nextChainPoint.y); }
         */
    }

    private Point getAdjacentClockWisePoint(Image image, Point currentPoint)
    {
        int currentX = currentPoint.x;
        int currentY = currentPoint.y;

        Point newPoint = null;

        if (image.getPixel(currentX - 1, currentY) == 0)
        {
            newPoint = new Point(currentX - 1, currentY);
        }
        else if (image.getPixel(currentX - 1, currentY + 1) == 0)
        {
            newPoint = new Point(currentX - 1, currentY + 1);
        }
        else if (image.getPixel(currentX, currentY + 1) == 0)
        {
            newPoint = new Point(currentX, currentY + 1);
        }
        else if (image.getPixel(currentX + 1, currentY + 1) == 0)
        {
            newPoint = new Point(currentX + 1, currentY + 1);
        }
        else if (image.getPixel(currentX + 1, currentY) == 0)
        {
            newPoint = new Point(currentX + 1, currentY);
        }
        else if (image.getPixel(currentX + 1, currentY - 1) == 0)
        {
            newPoint = new Point(currentX + 1, currentY - 1);
        }
        else if (image.getPixel(currentX, currentY - 1) == 0)
        {
            newPoint = new Point(currentX, currentY - 1);
        }
        else if (image.getPixel(currentX - 1, currentY - 1) == 0)
        {
            newPoint = new Point(currentX - 1, currentY - 1);
        }

        return newPoint;
    }

    private Point getImageInitialBorderPoint(Image image)
    {
        for (int row = 0; row < image.getRows(); row++)
        {
            for (int col = 0; col < image.getCols(); col++)
            {
                if (image.getPixel(row, col) == 0)
                {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }

}
