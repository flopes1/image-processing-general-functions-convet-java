package com.poli.model.representation.type.chain;

import java.awt.Point;
import java.util.ArrayList;

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
        this.setResultImage(image);
        /*
         * int gridSpliterX = (int) (this.getOriginalImage().getRows() * 0.01); int gridSpliterY = (int)
         * (this.getOriginalImage().getCols() * 0.01); this.chainBorderElements.clear(); for (int i = 0; i <
         * image.getRows(); i += gridSpliterX) { for (int j = 0; j < image.getCols(); j += gridSpliterY) { ChainPoint
         * first = this.searchFirstPointInQuadrant(image, i, i + ((float) gridSpliterX / 2.0) - 1.0, j, j + ((float)
         * gridSpliterY / 2.0) - 1.0, 1); if (first != null && !this.existInSamplingPoints(first)) {
         * this.chainBorderElements.add(first); } ChainPoint second = this.searchFirstPointInQuadrant(image, i, i +
         * ((float) gridSpliterX / 2.0) - 1.0, j + ((float) gridSpliterY / 2.0), j + gridSpliterY, 2); if (second !=
         * null && !this.existInSamplingPoints(second)) { this.chainBorderElements.add(second); } ChainPoint third =
         * this.searchFirstPointInQuadrant(image, i + ((float) gridSpliterX / 2.0), i + gridSpliterX, j, j + ((float)
         * gridSpliterY / 2.0) - 1.0, 3); if (third != null && !this.existInSamplingPoints(third)) {
         * this.chainBorderElements.add(third); } ChainPoint fourth = this.searchFirstPointInQuadrant(image, i +
         * ((float) gridSpliterX / 2.0), i + gridSpliterX, j + ((float) gridSpliterY / 2.0), j + gridSpliterY, 4); if
         * (fourth != null && !this.existInSamplingPoints(fourth)) { this.chainBorderElements.add(fourth); } if (first
         * != null && fourth != null && second == null && third == null) { second = new ChainPoint(i, j + gridSpliterY);
         * this.chainBorderElements.add(second); } else if (second != null && third != null && first == null && fourth
         * == null) { fourth = new ChainPoint(i + gridSpliterX, j + gridSpliterY); this.chainBorderElements.add(fourth);
         * } } } this.buildSamplingImage();
         */
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
        // image = this.closingTransformOperation.applyTransformation(image, StructuringElements.eightConnected);
        // image = this.extractionTransformOperation.applyTransformation(image, StructuringElements.eightConnected);

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
            System.err.println(nextChainPoint.toString());
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

    private boolean searchLeftDown = true;
    private boolean searchRightDown = false;
    private boolean searchRightUp = false;
    private boolean searchLeftUp = false;

    private Point getAdjacentClockWisePoint(Image image, Point currentPoint)
    {
        int currentX = currentPoint.x;
        int currentY = currentPoint.y;

        Point newBorderPoint = null;

        if (image.getPixel(currentX - 1, currentY) == 0)
        {
            newBorderPoint = new Point(currentX - 1, currentY);
            if (!this.imageBorderElements.contains(newBorderPoint))
            {
                return newBorderPoint;
            }
            else
            {
                newBorderPoint = null;
            }
        }

        if (image.getPixel(currentX, currentY + 1) == 0)
        {
            newBorderPoint = new Point(currentX, currentY + 1);
            if (!this.imageBorderElements.contains(newBorderPoint))
            {
                return newBorderPoint;
            }
            else
            {
                newBorderPoint = null;
            }
        }

        if (image.getPixel(currentX + 1, currentY) == 0)
        {
            newBorderPoint = new Point(currentX + 1, currentY);
            if (!this.imageBorderElements.contains(newBorderPoint))
            {
                return newBorderPoint;
            }
            else
            {
                newBorderPoint = null;
            }
        }

        if (image.getPixel(currentX, currentY - 1) == 0)
        {
            newBorderPoint = new Point(currentX, currentY - 1);
            if (!this.imageBorderElements.contains(newBorderPoint))
            {
                return newBorderPoint;
            }
            else
            {
                newBorderPoint = null;
            }
        }

        return newBorderPoint;
    }

    private boolean searchLeftUp(Image image, int currentX, int currentY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean searchRightUp(Image image, int currentX, int currentY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean searchLeftDown(Image image, int currentX, int currentY)
    {
        Point newPoint;
        boolean hasAdded = true;

        if (image.getPixel(currentX, currentY - 1) == 0)
        {
            newPoint = new Point(currentX, currentY - 1);
            this.imageBorderElements.add(newPoint);
            System.err.println(newPoint.toString());
        }
        else
        {
            if (image.getPixel(currentX - 1, currentY - 1) == 0)
            {
                if (image.getPixel(currentX, currentY - 1) == 0)
                {
                    newPoint = new Point(currentX - 1, currentY - 1);
                    this.imageBorderElements.add(newPoint);
                    System.err.println(newPoint.toString());
                    newPoint = new Point(currentX, currentY - 1);
                    this.imageBorderElements.add(newPoint);
                    System.err.println(newPoint.toString());
                }
                else
                {
                    newPoint = new Point(currentX - 1, currentY - 1);
                    this.imageBorderElements.add(newPoint);
                    System.err.println(newPoint.toString());
                }

            }
            else
            {

                if (image.getPixel(currentX + 1, currentY - 1) == 0)
                {
                    if (image.getPixel(currentX + 1, currentY) == 0)
                    {
                        newPoint = new Point(currentX + 1, currentY);
                        this.imageBorderElements.add(newPoint);
                        System.err.println(newPoint.toString());
                        newPoint = new Point(currentX + 1, currentY - 1);
                        this.imageBorderElements.add(newPoint);
                        System.err.println(newPoint.toString());
                    }
                    else
                    {
                        newPoint = new Point(currentX + 1, currentY - 1);
                        this.imageBorderElements.add(newPoint);
                        System.err.println(newPoint.toString());
                    }
                }
                else if (image.getPixel(currentX + 1, currentY) == 0)
                {
                    newPoint = new Point(currentX + 1, currentY);
                    this.imageBorderElements.add(newPoint);
                    System.err.println(newPoint.toString());
                }
                else
                {
                    hasAdded = false;
                }
            }
        }
        return hasAdded;
    }

    private boolean searchRightDown(Image image, int currentX, int currentY)
    {
        boolean hasAdded = true;

        Point newPoint;

        if (image.getPixel(currentX, currentY + 1) == 0)
        {
            newPoint = new Point(currentX, currentY + 1);
            this.imageBorderElements.add(newPoint);
            System.err.println(newPoint.toString());
        }
        else
        {
            if (image.getPixel(currentX - 1, currentY + 1) == 0)
            {
                if (image.getPixel(currentX - 1, currentY) == 0)
                {
                    newPoint = new Point(currentX - 1, currentY);
                    this.imageBorderElements.add(newPoint);
                    System.err.println(newPoint.toString());
                    newPoint = new Point(currentX - 1, currentY + 1);
                    this.imageBorderElements.add(newPoint);
                    System.err.println(newPoint.toString());
                }
                else
                {
                    newPoint = new Point(currentX - 1, currentY + 1);
                    this.imageBorderElements.add(newPoint);
                    System.err.println(newPoint.toString());
                }

            }
            else
            {

                if (image.getPixel(currentX + 1, currentY + 1) == 0)
                {
                    if (image.getPixel(currentX + 1, currentY) == 0)
                    {
                        newPoint = new Point(currentX + 1, currentY + 1);
                        this.imageBorderElements.add(newPoint);
                        System.err.println(newPoint.toString());
                        newPoint = new Point(currentX + 1, currentY);
                        this.imageBorderElements.add(newPoint);
                        System.err.println(newPoint.toString());
                    }
                    else
                    {
                        newPoint = new Point(currentX + 1, currentY + 1);
                        this.imageBorderElements.add(newPoint);
                        System.err.println(newPoint.toString());
                    }
                }
                else
                {
                    if (image.getPixel(currentX + 1, currentY) == 0)
                    {
                        newPoint = new Point(currentX + 1, currentY);
                        this.imageBorderElements.add(newPoint);
                        System.err.println(newPoint.toString());
                    }
                    else
                    {
                        hasAdded = false;
                    }
                }
            }
        }
        return hasAdded;
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
