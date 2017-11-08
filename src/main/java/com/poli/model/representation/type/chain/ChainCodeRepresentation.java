package com.poli.model.representation.type.chain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.poli.model.Image;
import com.poli.model.morphology.common.StructuringElements;
import com.poli.model.morphology.set.operation.ClosingTransformOperation;
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
    private ClosingTransformOperation closingTransformOperation;

    public ChainCodeRepresentation(Image originalImage, EnumChainDirectionType enumChainDirectionType)
    {
        super(originalImage, EnumRepresentationType.CHAIN_CODE);
        this.enumChainDirectionType = enumChainDirectionType;
        this.chainBorderElements = new ArrayList<ChainPoint>();
        this.imageBorderElements = new ArrayList<Point>();
        this.openningTransformOperation = new OpenningTransformOperation();
        this.extractionTransformOperation = new ExtractionTransformOperation();
        this.closingTransformOperation = new ClosingTransformOperation();
    }

    @Override
    public void generateImageRepresentation(boolean preProcess)
    {
        Image image = this.calculateImageBorderPoints(preProcess);

        int gridSpliterX = (int) (this.getOriginalImage().getRows() * 0.01);
        int gridSpliterY = (int) (this.getOriginalImage().getCols() * 0.01);
        this.chainBorderElements.clear();
        for (int i = 0; i < image.getRows(); i += gridSpliterX)
        {
            for (int j = 0; j < image.getCols(); j += gridSpliterY)
            {
                ChainPoint first = this.searchFirstPointInQuadrant(image, i, i + ((float) gridSpliterX / 2.0) - 1.0, j,
                        j + ((float) gridSpliterY / 2.0) - 1.0, 1);
                if (first != null && !this.existInSamplingPoints(first))
                {
                    this.chainBorderElements.add(first);
                }
                ChainPoint second = this.searchFirstPointInQuadrant(image, i, i + ((float) gridSpliterX / 2.0) - 1.0,
                        j + ((float) gridSpliterY / 2.0), j + gridSpliterY, 2);
                if (second != null && !this.existInSamplingPoints(second))
                {
                    this.chainBorderElements.add(second);
                }
                ChainPoint third = this.searchFirstPointInQuadrant(image, i + ((float) gridSpliterX / 2.0),
                        i + gridSpliterX, j, j + ((float) gridSpliterY / 2.0) - 1.0, 3);
                if (third != null && !this.existInSamplingPoints(third))
                {
                    this.chainBorderElements.add(third);
                }
                ChainPoint fourth = this.searchFirstPointInQuadrant(image, i + ((float) gridSpliterX / 2.0),
                        i + gridSpliterX, j + ((float) gridSpliterY / 2.0), j + gridSpliterY, 4);
                if (fourth != null && !this.existInSamplingPoints(fourth))
                {
                    this.chainBorderElements.add(fourth);
                }
                if (first != null && fourth != null && second == null && third == null)
                {
                    second = new ChainPoint(i, j + gridSpliterY);
                    this.chainBorderElements.add(second);
                }
                else if (second != null && third != null && first == null && fourth == null)
                {
                    fourth = new ChainPoint(i + gridSpliterX, j + gridSpliterY);
                    this.chainBorderElements.add(fourth);
                }
            }
        }
        this.buildSamplingImage();
    }

    private boolean existInSamplingPoints(ChainPoint point)
    {
        for (ChainPoint samplerPoint : this.chainBorderElements)
        {
            if (point.equals(samplerPoint))
            {
                return true;
            }
        }
        return false;
    }

    private ChainPoint searchFirstPointInQuadrant(Image image, double xInitial, double xFinal, double yInitial,
            double yFinal, int quadrant)
    {

        int xEnd = (int) (xFinal >= image.getRows() ? image.getRows() : xFinal);
        int yEnd = (int) (yFinal >= image.getCols() ? image.getCols() : yFinal);

        for (int i = (int) xInitial; i < xEnd; i++)
        {
            for (int j = (int) yInitial; j < yEnd; j++)
            {
                if (image.getPixel(i, j) == 0)
                {
                    if (quadrant == 1)
                    {
                        return new ChainPoint((int) xInitial, (int) yInitial);
                    }
                    else if (quadrant == 2)
                    {
                        return new ChainPoint((int) xInitial, (int) yFinal);
                    }
                    else if (quadrant == 3)
                    {
                        return new ChainPoint((int) xFinal, (int) yInitial);
                    }
                    else if (quadrant == 4)
                    {
                        return new ChainPoint((int) xFinal, (int) yFinal);
                    }
                }
            }
        }

        return null;
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
        for (ChainPoint point : this.chainBorderElements)
        {
            this.getResultImage().setPixel(point.x, point.y, 0);
        }

        if (this.enumChainDirectionType.equals(EnumChainDirectionType.EIGHT_DIRETION))
        {
            this.formatImageOutput2EightConnect();
        }

        this.joinPointsResultImage();
    }

    private void joinPointsResultImage()
    {
        List<ChainPoint> processedPoints = new ArrayList<ChainPoint>();

        ChainPoint initialPoint = this.getImageInitialBorderPoint(this.getResultImage());
        initialPoint.setDirection(-1);

        ChainPoint previousPoint = initialPoint;

        while (true)
        {
            ChainPoint nextPoint = this.getNextPoint(previousPoint, processedPoints, this.getResultImage(),
                    this.enumChainDirectionType);

            if (nextPoint.equals(initialPoint))
            {
                break;
            }
            System.err.println(nextPoint.toString());
            previousPoint = nextPoint;
        }

        ChainPoint first = processedPoints.get(0);

        for (int i = 0; i < processedPoints.size(); i++)
        {
            ChainPoint next = processedPoints.get(i);
            this.drawLine(first, next, this.getResultImage());
            first = next;
        }

        // for(int row = 0; row < this.getResultImage().getRows(); row++)
        // {
        // for(int col = 0; col < this.getResultImage().getCols(); col++)
        // {
        // if(this.getResultImage().getPixel(row, col) == 0)
        // {
        // ChainPoint newPoint = new ChainPoint(row, col, 0);
        // }
        // }
        // }

    }

    private void drawLine(ChainPoint previousPoint, ChainPoint nextPoint, Image resultImage)
    {

        if (previousPoint.x == nextPoint.x)
        {
            int initialY, finalY;
            if (previousPoint.y < nextPoint.y)
            {
                initialY = previousPoint.y;
                finalY = nextPoint.y;
            }
            else
            {
                initialY = nextPoint.y;
                finalY = previousPoint.y;
            }

            for (int j = initialY; j < finalY; j++)
            {
                resultImage.setPixel(previousPoint.x, j, 0);
            }
        }
        else if (previousPoint.y == nextPoint.y)
        {
            int initialX, finalX;

            if (previousPoint.x < nextPoint.x)
            {
                initialX = previousPoint.x;
                finalX = nextPoint.x;
            }
            else
            {
                initialX = nextPoint.x;
                finalX = previousPoint.x;
            }

            for (int i = initialX; i < finalX; i++)
            {
                resultImage.setPixel(i, previousPoint.y, 0);
            }

        }
        else
        {
            int initialX, finalX, initialY, finalY;

            if (previousPoint.x < nextPoint.x)
            {
                initialX = previousPoint.x;
                finalX = nextPoint.x;
            }
            else
            {
                initialX = nextPoint.x;
                finalX = previousPoint.x;
            }

            if (previousPoint.y < nextPoint.y)
            {
                initialY = previousPoint.y;
                finalY = nextPoint.y;
            }
            else
            {
                initialY = nextPoint.y;
                finalY = previousPoint.y;
            }
            int j = initialY;
            for (int i = initialX; i < finalX; i++)
            {
                resultImage.setPixel(i, j++, 0);
            }

        }

    }

    private ChainPoint getNextPoint(ChainPoint previousPoint, List<ChainPoint> processedPoints, Image resultImage,
            EnumChainDirectionType enumChainDirectionType2)
    {

        ChainPoint next = null;

        int xRate = (int) (resultImage.getRows() * 0.01) + 1;
        int yRate = (int) (resultImage.getCols() * 0.01) + 1;

        // search in down up
        for (int i = previousPoint.x - 1; i > previousPoint.x - xRate; i--)
        {
            if (i > -1 && resultImage.getPixel(i, previousPoint.y) == 0)
            {
                next = new ChainPoint(i, previousPoint.y, 2);
                if (processedPoints.contains(next) || previousPoint.getDirection() == 6)
                {
                    break;
                }
                processedPoints.add(next);
                return next;
            }
        }

        if (EnumChainDirectionType.EIGHT_DIRETION.equals(enumChainDirectionType2))
        {
            // serch diagonal 3-2
            for (int i = previousPoint.x - 1; i > previousPoint.x - xRate; i--)
            {
                for (int j = previousPoint.y + 1; j < previousPoint.y + yRate; j++)
                {
                    if (i > -1 && j < resultImage.getCols() && resultImage.getPixel(i, j) == 0)
                    {
                        next = new ChainPoint(i, j, 3);
                        if (processedPoints.contains(next) || previousPoint.getDirection() == 7)
                        {
                            break;
                        }
                        processedPoints.add(next);
                        return next;
                    }
                }
            }
        }

        // search in right direction
        for (int j = previousPoint.y + 1; j < previousPoint.y + yRate; j++)
        {
            if (j < resultImage.getCols() && resultImage.getPixel(previousPoint.x, j) == 0)
            {
                next = new ChainPoint(previousPoint.x, j, 4);
                if (processedPoints.contains(next) || previousPoint.getDirection() == 0)
                {
                    break;
                }
                processedPoints.add(next);
                return next;
            }
        }

        if (EnumChainDirectionType.EIGHT_DIRETION.equals(enumChainDirectionType2))
        {
            // TODO search diagonal 1-4
            for (int i = previousPoint.x + 1; i < previousPoint.x + xRate; i++)
            {
                for (int j = previousPoint.y + 1; j < previousPoint.y + yRate; j++)
                {
                    if (j < resultImage.getCols() && i < resultImage.getRows() && resultImage.getPixel(i, j) == 0)
                    {
                        next = new ChainPoint(i, j, 5);
                        if (processedPoints.contains(next) || previousPoint.getDirection() == 1)
                        {
                            break;
                        }
                        processedPoints.add(next);
                        return next;
                    }
                }
            }
        }

        // search up to down
        for (int i = previousPoint.x + 1; i < previousPoint.x + xRate; i++)
        {
            if (i < resultImage.getRows() && resultImage.getPixel(i, previousPoint.y) == 0)
            {
                next = new ChainPoint(i, previousPoint.y, 6);
                if (processedPoints.contains(next) || previousPoint.getDirection() == 2)
                {
                    break;
                }
                processedPoints.add(next);
                return next;
            }
        }

        if (EnumChainDirectionType.EIGHT_DIRETION.equals(enumChainDirectionType2))
        {
            // search diagonal 2-3
            for (int i = previousPoint.x + 1; i < previousPoint.x + xRate; i++)
            {
                for (int j = previousPoint.y - 1; j > previousPoint.y - yRate; j--)
                {
                    if (j > -1 && i < resultImage.getRows() && resultImage.getPixel(i, j) == 0)
                    {
                        next = new ChainPoint(i, j, 7);
                        if (processedPoints.contains(next) || previousPoint.getDirection() == 3)
                        {
                            break;
                        }
                        processedPoints.add(next);
                        return next;
                    }
                }
            }
        }

        // search in left direction
        for (int j = previousPoint.y - 1; j > previousPoint.y - yRate; j--)
        {
            if (j > -1 && resultImage.getPixel(previousPoint.x, j) == 0)
            {
                next = new ChainPoint(previousPoint.x, j, 0);
                if (processedPoints.contains(next) || previousPoint.getDirection() == 4)
                {
                    break;
                }
                processedPoints.add(next);
                return next;
            }
        }

        if (EnumChainDirectionType.EIGHT_DIRETION.equals(enumChainDirectionType2))
        {
            // TODO serch diagonal 3-1
            for (int i = previousPoint.x - 1; i > previousPoint.x - xRate; i--)
            {
                for (int j = previousPoint.y - 1; j > previousPoint.y - yRate; j--)
                {
                    if (j > -1 && i > -1 && resultImage.getPixel(i, j) == 0)
                    {
                        next = new ChainPoint(i, j, 1);
                        if (processedPoints.contains(next) || previousPoint.getDirection() == 5)
                        {
                            break;
                        }
                        processedPoints.add(next);
                        return next;
                    }
                }
            }
        }

        if (processedPoints.contains(next))
        {
            ChainPoint aux = this.searchFirstAvaliablePoint(previousPoint, processedPoints, resultImage);
            if (aux == null)
            {
                throw new RuntimeException("Dead lock");
            }
            next = aux;
        }

        return next;
    }

    private ChainPoint searchFirstAvaliablePoint(ChainPoint previousPoint, List<ChainPoint> processedPoints,
            Image resultImage)
    {
        int xThresh = (int) (this.getResultImage().getRows() * 0.1);
        int yThresh = (int) (this.getResultImage().getCols() * 0.1);

        // find up
        for (int i = previousPoint.x; i > previousPoint.x - xThresh; i--)
        {
            if (i > -1 && resultImage.getPixel(i, previousPoint.y) == 0)
            {
                ChainPoint aux = new ChainPoint(i, previousPoint.y, 2);
                if (!processedPoints.contains(aux))
                {
                    processedPoints.add(aux);
                    return aux;
                }
            }
        }

        if (EnumChainDirectionType.EIGHT_DIRETION.equals(this.enumChainDirectionType))
        {
            // serch diagonal 3-2
            for (int i = previousPoint.x - 1; i > previousPoint.x - xThresh; i--)
            {
                for (int j = previousPoint.y + 1; j < previousPoint.y + yThresh; j++)
                {
                    if (i > -1 && j < resultImage.getCols() && resultImage.getPixel(i, j) == 0)
                    {
                        ChainPoint aux = new ChainPoint(i, j, 3);
                        if (!processedPoints.contains(aux))
                        {
                            processedPoints.add(aux);
                            return aux;
                        }
                    }
                }
            }
        }

        // find right
        for (int j = previousPoint.y; j < previousPoint.y + yThresh; j++)
        {
            if (j < resultImage.getCols() && resultImage.getPixel(previousPoint.x, j) == 0)
            {
                ChainPoint aux = new ChainPoint(previousPoint.x, j, 4);
                if (!processedPoints.contains(aux))
                {
                    processedPoints.add(aux);
                    return aux;
                }
            }
        }

        if (EnumChainDirectionType.EIGHT_DIRETION.equals(this.enumChainDirectionType))
        {
            // TODO search diagonal 1-4
            for (int i = previousPoint.x + 1; i < previousPoint.x + xThresh; i++)
            {
                for (int j = previousPoint.y + 1; j < previousPoint.y + yThresh; j++)
                {
                    if (j < resultImage.getCols() && i < resultImage.getRows() && resultImage.getPixel(i, j) == 0)
                    {
                        ChainPoint aux = new ChainPoint(i, j, 5);
                        if (!processedPoints.contains(aux))
                        {
                            processedPoints.add(aux);
                            return aux;
                        }
                    }
                }
            }
        }

        // find down
        for (int i = previousPoint.x; i < previousPoint.x + xThresh; i++)
        {
            if (i < resultImage.getRows() && resultImage.getPixel(i, previousPoint.y) == 0)
            {
                ChainPoint aux = new ChainPoint(i, previousPoint.y, 6);
                if (!processedPoints.contains(aux))
                {
                    processedPoints.add(aux);
                    return aux;
                }
            }
        }

        if (EnumChainDirectionType.EIGHT_DIRETION.equals(this.enumChainDirectionType))
        {
            // search diagonal 2-3
            for (int i = previousPoint.x + 1; i < previousPoint.x + xThresh; i++)
            {
                for (int j = previousPoint.y - 1; j > previousPoint.y - yThresh; j--)
                {
                    if (j > -1 && i < resultImage.getRows() && resultImage.getPixel(i, j) == 0)
                    {
                        ChainPoint aux = new ChainPoint(i, j, 7);
                        if (!processedPoints.contains(aux))
                        {
                            processedPoints.add(aux);
                            return aux;
                        }
                    }
                }
            }
        }

        // find left
        for (int j = previousPoint.y; j > previousPoint.y - yThresh; j--)
        {
            if (j > -1 && resultImage.getPixel(previousPoint.x, j) == 0)
            {
                ChainPoint aux = new ChainPoint(previousPoint.x, j, 2);
                if (!processedPoints.contains(aux))
                {
                    processedPoints.add(aux);
                    return aux;
                }
            }
        }

        if (EnumChainDirectionType.EIGHT_DIRETION.equals(this.enumChainDirectionType))
        {
            // TODO serch diagonal 3-1
            for (int i = previousPoint.x - 1; i > previousPoint.x - xThresh; i--)
            {
                for (int j = previousPoint.y - 1; j > previousPoint.y - yThresh; j--)
                {
                    if (j > -1 && i > -1 && resultImage.getPixel(i, j) == 0)
                    {
                        ChainPoint aux = new ChainPoint(i, j, 1);
                        if (!processedPoints.contains(aux))
                        {
                            processedPoints.add(aux);
                            return aux;
                        }
                    }
                }
            }
        }

        return null;
    }

    private void formatImageOutput2EightConnect()
    {
        int xRate = (int) (this.getOriginalImage().getRows() * 0.01);
        int yRate = (int) (this.getOriginalImage().getCols() * 0.01);

        for (int row = this.getResultImage().getRows() - 1; row >= 0; row--)
        {
            for (int col = 0; col < this.getResultImage().getCols(); col++)
            {
                if (this.getResultImage().getPixel(row, col) == 0)
                {
                    boolean containsRight = this.searchRightPixel(row, col, yRate);
                    boolean containsLeft = this.searchLeftPixel(row, col, yRate);
                    if (containsRight || containsLeft)
                    {
                        boolean containsUp = this.searchUpperPixel(row, col, xRate);
                        if (containsUp)
                        {
                            this.getResultImage().setPixel(row, col, 255);
                        }
                    }
                }
            }
        }
    }

    private boolean searchLeftPixel(int row, int col, int yRate)
    {
        for (int j = col - 1; j > col - yRate - 1; j--)
        {
            if (j > 0 && this.getResultImage().getPixel(row, j) == 0)
            {
                return true;
            }
        }
        return false;
    }

    private boolean searchUpperPixel(int row, int col, int xRate)
    {
        for (int i = row - 1; i > row - xRate - 1; i--)
        {
            if (i >= 0 && this.getResultImage().getPixel(i, col) == 0)
            {
                return true;
            }
        }
        return false;
    }

    private boolean searchRightPixel(int row, int col, int yRate)
    {
        for (int j = col + 1; j < col + yRate + 1; j++)
        {
            if (j < this.getResultImage().getCols() && this.getResultImage().getPixel(row, j) == 0)
            {
                return true;
            }
        }
        return false;
    }

    private Image calculateImageBorderPoints(boolean preProcess)
    {
        Image image = this.getOriginalImage().cloneImage();
        if (preProcess)
        {
            image = this.openningTransformOperation.applyTransformation(image, StructuringElements.eightConnected);
        }

        ChainPoint initialPoint = this.getImageInitialBorderPoint(image);
        this.chainBorderElements.add(initialPoint);

        ChainPoint bPoint = new ChainPoint(initialPoint.x, initialPoint.y, 0);

        while (true)
        {
            ChainPoint nextChainPoint = this.getNextBorderPoint(image, bPoint);

            if (nextChainPoint == null)
            {
                throw new IllegalArgumentException("Imagem descontinua");
            }

            if (nextChainPoint.equals(initialPoint))
            {
                break;
            }

            this.chainBorderElements.add(nextChainPoint);
            // System.err.println(nextChainPoint.toString());
            bPoint = new ChainPoint(nextChainPoint.x, nextChainPoint.y, nextChainPoint.getDirection());
        }

        image.clean();

        for (ChainPoint point : this.chainBorderElements)
        {
            image.setPixel(point.x, point.y, 0);
        }

        return image;
    }

    private ChainPoint getNextBorderPoint(Image image, ChainPoint bPoint)
    {
        ChainPoint newPoint = null;
        int x = bPoint.x;
        int y = bPoint.y;
        int dir = bPoint.getDirection();

        switch (dir)
        {
            case 0:
                newPoint = this.checkLeftNeighbor(image, newPoint, x, y);
                break;
            case 1:
                newPoint = this.checkCornerLeftNeighbor(image, newPoint, x, y);
                break;
            case 2:
                newPoint = this.checkUpperNeighbor(image, newPoint, x, y);
                break;
            case 3:
                newPoint = this.checkCornerRightNeighbor(image, newPoint, x, y);
                break;
            case 4:
                newPoint = this.checkRightNeighbor(image, newPoint, x, y);
                break;
            case 5:
                newPoint = this.checkDownRightNeighbor(image, newPoint, x, y);
                break;
            case 6:
                newPoint = this.checkDownNeighbor(image, newPoint, x, y);
                break;
            case 7:
                newPoint = this.checkDownLeftNeighbor(image, newPoint, x, y);
                break;
        }

        return newPoint;
    }

    private ChainPoint checkDownLeftNeighbor(Image image, ChainPoint newPoint, int x, int y)
    {
        if (image.getPixel(x + 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y - 1, 6);
        }
        else if (image.getPixel(x, y - 1) == 0)
        {
            newPoint = new ChainPoint(x, y - 1, 7);
        }
        else if (image.getPixel(x - 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y - 1, 0);
        }
        else if (image.getPixel(x - 1, y) == 0)
        {
            newPoint = new ChainPoint(x - 1, y, 1);
        }
        else if (image.getPixel(x - 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y + 1, 2);
        }
        else if (image.getPixel(x, y + 1) == 0)
        {
            newPoint = new ChainPoint(x, y + 1, 3);
        }
        else if (image.getPixel(x + 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y + 1, 4);
        }
        else if (image.getPixel(x + 1, y) == 0)
        {
            newPoint = new ChainPoint(x + 1, y, 5);
        }
        return newPoint;
    }

    private ChainPoint checkDownNeighbor(Image image, ChainPoint newPoint, int x, int y)
    {
        if (image.getPixel(x + 1, y) == 0)
        {
            newPoint = new ChainPoint(x + 1, y, 5);
        }
        else if (image.getPixel(x + 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y - 1, 6);
        }
        else if (image.getPixel(x, y - 1) == 0)
        {
            newPoint = new ChainPoint(x, y - 1, 7);
        }
        else if (image.getPixel(x - 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y - 1, 0);
        }
        else if (image.getPixel(x - 1, y) == 0)
        {
            newPoint = new ChainPoint(x - 1, y, 1);
        }
        else if (image.getPixel(x - 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y + 1, 2);
        }
        else if (image.getPixel(x, y + 1) == 0)
        {
            newPoint = new ChainPoint(x, y + 1, 3);
        }
        else if (image.getPixel(x + 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y + 1, 4);
        }
        return newPoint;
    }

    private ChainPoint checkDownRightNeighbor(Image image, ChainPoint newPoint, int x, int y)
    {
        if (image.getPixel(x + 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y + 1, 4);
        }
        else if (image.getPixel(x + 1, y) == 0)
        {
            newPoint = new ChainPoint(x + 1, y, 5);
        }
        else if (image.getPixel(x + 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y - 1, 6);
        }
        else if (image.getPixel(x, y - 1) == 0)
        {
            newPoint = new ChainPoint(x, y - 1, 7);
        }
        else if (image.getPixel(x - 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y - 1, 0);
        }
        else if (image.getPixel(x - 1, y) == 0)
        {
            newPoint = new ChainPoint(x - 1, y, 1);
        }
        else if (image.getPixel(x - 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y + 1, 2);
        }
        else if (image.getPixel(x, y + 1) == 0)
        {
            newPoint = new ChainPoint(x, y + 1, 3);
        }
        return newPoint;
    }

    private ChainPoint checkRightNeighbor(Image image, ChainPoint newPoint, int x, int y)
    {
        if (image.getPixel(x, y + 1) == 0)
        {
            newPoint = new ChainPoint(x, y + 1, 3);
        }
        else if (image.getPixel(x + 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y + 1, 4);
        }
        else if (image.getPixel(x + 1, y) == 0)
        {
            newPoint = new ChainPoint(x + 1, y, 5);
        }
        else if (image.getPixel(x + 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y - 1, 6);
        }
        else if (image.getPixel(x, y - 1) == 0)
        {
            newPoint = new ChainPoint(x, y - 1, 7);
        }
        else if (image.getPixel(x - 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y - 1, 0);
        }
        else if (image.getPixel(x - 1, y) == 0)
        {
            newPoint = new ChainPoint(x - 1, y, 1);
        }
        else if (image.getPixel(x - 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y + 1, 2);
        }
        return newPoint;
    }

    private ChainPoint checkCornerRightNeighbor(Image image, ChainPoint newPoint, int x, int y)
    {
        if (image.getPixel(x - 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y + 1, 2);
        }
        else if (image.getPixel(x, y + 1) == 0)
        {
            newPoint = new ChainPoint(x, y + 1, 3);
        }
        else if (image.getPixel(x + 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y + 1, 4);
        }
        else if (image.getPixel(x + 1, y) == 0)
        {
            newPoint = new ChainPoint(x + 1, y, 5);
        }
        else if (image.getPixel(x + 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y - 1, 6);
        }
        else if (image.getPixel(x, y - 1) == 0)
        {
            newPoint = new ChainPoint(x, y - 1, 7);
        }
        else if (image.getPixel(x - 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y - 1, 0);
        }
        else if (image.getPixel(x - 1, y) == 0)
        {
            newPoint = new ChainPoint(x - 1, y, 1);
        }
        return newPoint;
    }

    private ChainPoint checkUpperNeighbor(Image image, ChainPoint newPoint, int x, int y)
    {
        if (image.getPixel(x - 1, y) == 0)
        {
            newPoint = new ChainPoint(x - 1, y, 1);
        }
        else if (image.getPixel(x - 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y + 1, 2);
        }
        else if (image.getPixel(x, y + 1) == 0)
        {
            newPoint = new ChainPoint(x, y + 1, 3);
        }
        else if (image.getPixel(x + 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y + 1, 4);
        }
        else if (image.getPixel(x + 1, y) == 0)
        {
            newPoint = new ChainPoint(x + 1, y, 5);
        }
        else if (image.getPixel(x + 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y - 1, 6);
        }
        else if (image.getPixel(x, y - 1) == 0)
        {
            newPoint = new ChainPoint(x, y - 1, 7);
        }
        else if (image.getPixel(x - 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y - 1, 0);
        }
        return newPoint;
    }

    private ChainPoint checkCornerLeftNeighbor(Image image, ChainPoint newPoint, int x, int y)
    {
        if (image.getPixel(x - 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y - 1, 0);
        }
        else if (image.getPixel(x - 1, y) == 0)
        {
            newPoint = new ChainPoint(x - 1, y, 1);
        }
        else if (image.getPixel(x - 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y + 1, 2);
        }
        else if (image.getPixel(x, y + 1) == 0)
        {
            newPoint = new ChainPoint(x, y + 1, 3);
        }
        else if (image.getPixel(x + 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y + 1, 4);
        }
        else if (image.getPixel(x + 1, y) == 0)
        {
            newPoint = new ChainPoint(x + 1, y, 5);
        }
        else if (image.getPixel(x + 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y - 1, 6);
        }
        else if (image.getPixel(x, y - 1) == 0)
        {
            newPoint = new ChainPoint(x, y - 1, 7);
        }
        return newPoint;
    }

    private ChainPoint checkLeftNeighbor(Image image, ChainPoint newPoint, int x, int y)
    {
        if (image.getPixel(x, y - 1) == 0)
        {
            newPoint = new ChainPoint(x, y - 1, 7);
        }
        else if (image.getPixel(x - 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y - 1, 0);
        }
        else if (image.getPixel(x - 1, y) == 0)
        {
            newPoint = new ChainPoint(x - 1, y, 1);
        }
        else if (image.getPixel(x - 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x - 1, y + 1, 2);
        }
        else if (image.getPixel(x, y + 1) == 0)
        {
            newPoint = new ChainPoint(x, y + 1, 3);
        }
        else if (image.getPixel(x + 1, y + 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y + 1, 4);
        }
        else if (image.getPixel(x + 1, y) == 0)
        {
            newPoint = new ChainPoint(x + 1, y, 5);
        }
        else if (image.getPixel(x + 1, y - 1) == 0)
        {
            newPoint = new ChainPoint(x + 1, y - 1, 6);
        }
        return newPoint;
    }

    private ChainPoint getImageInitialBorderPoint(Image image)
    {
        for (int row = 0; row < image.getRows(); row++)
        {
            for (int col = 0; col < image.getCols(); col++)
            {
                if (image.getPixel(row, col) == 0)
                {
                    return new ChainPoint(row, col, 0);
                }
            }
        }
        return null;
    }

}
