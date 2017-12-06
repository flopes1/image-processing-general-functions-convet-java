package com.poli.model.representation.type.polygonal;

import java.util.ArrayList;
import java.util.List;

import com.poli.model.Image;
import com.poli.model.representation.ImageRepresentation;
import com.poli.model.representation.common.EnumRepresentationType;
import com.poli.model.representation.type.chain.ChainCodeRepresentation;
import com.poli.model.representation.type.chain.ChainPoint;
import com.poli.model.representation.type.chain.EnumChainDirectionType;

public class PolygonalRepresentation extends ImageRepresentation
{

    private List<ChainPoint> chainBorderElements;
    private ImageRepresentation imageRepresentation;

    public PolygonalRepresentation(Image originalImage)
    {
        super(originalImage, EnumRepresentationType.POLYGONAL);
        this.chainBorderElements = new ArrayList<ChainPoint>();
    }

    @Override
    public void generateImageRepresentation(boolean preProcess)
    {
        this.imageRepresentation = new ChainCodeRepresentation(this.getOriginalImage(),
                EnumChainDirectionType.FOUR_DIRECTION);
        this.imageRepresentation.generateImageRepresentation(false);

        Image repImage = this.imageRepresentation.getResultImage();

        this.chainBorderElements = this.getBorderPoints(repImage);

        repImage.clean();

        this.chainBorderElements = this.getBorderVertices();

        ChainPoint first = this.chainBorderElements.get(0);

        for (int i = 1; i < this.chainBorderElements.size(); i++)
        {
            ChainPoint next = this.chainBorderElements.get(i);
            this.drawLine(first, next, repImage);
            // repImage.setPixel(next.x, next.y, 0);
            first = next;
        }

        this.setResultImage(repImage);
    }

    private List<ChainPoint> getBorderPoints(Image image)
    {
        List<ChainPoint> processedPoints = new ArrayList<ChainPoint>();

        ChainPoint initialPoint = this.getImageInitialBorderPoint(image);
        initialPoint.setDirection(-1);

        ChainPoint previousPoint = initialPoint;

        while (true)
        {
            ChainPoint nextPoint = this.getNextPoint(previousPoint, processedPoints, image,
                    EnumChainDirectionType.EIGHT_DIRETION);

            if (nextPoint.equals(initialPoint))
            {
                break;
            }
            System.err.println(nextPoint.toString());
            previousPoint = nextPoint;
        }

        return processedPoints;

        // ChainPoint first = processedPoints.get(0);

        // for (int i = 1; i < processedPoints.size(); i++)
        // {
        // ChainPoint next = processedPoints.get(i);
        // //this.drawLine(first, next, image);
        // first = next;
        // }

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
            // serch diagonal 4-1
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

        // find left
        for (int j = previousPoint.y; j > previousPoint.y - yThresh; j--)
        {
            if (j > -1 && resultImage.getPixel(previousPoint.x, j) == 0)
            {
                ChainPoint aux = new ChainPoint(previousPoint.x, j, 0);
                if (!processedPoints.contains(aux))
                {
                    processedPoints.add(aux);
                    return aux;
                }
            }
        }

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

        // TODO serch diagonal 4-1
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

        return null;
    }

    private ArrayList<ChainPoint> getImagePointers(Image repImage)
    {
        ArrayList<ChainPoint> points = new ArrayList<>();

        for (int i = 0; i < repImage.getRows(); i++)
        {
            for (int j = 0; j < repImage.getCols(); j++)
            {
                if (repImage.getPixel(i, j) == 0)
                {
                    points.add(new ChainPoint(i, j));
                }
            }
        }

        return points;
    }

    private Image calculateImageBorderPoints(boolean preProcess)
    {
        Image image = this.getOriginalImage().cloneImage();

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

        this.chainBorderElements = this.getBorderVertices();

        // for (ChainPoint point : this.chainBorderElements)
        // {
        // image.setPixel(point.x, point.y, 0);
        // }

        ChainPoint first = this.chainBorderElements.get(0);

        for (int i = 1; i < this.chainBorderElements.size(); i++)
        {
            ChainPoint next = this.chainBorderElements.get(i);
            this.drawLine(first, next, image);
            first = next;
        }

        return image;
    }

    private ArrayList<ChainPoint> getBorderVertices()
    {
        ArrayList<ChainPoint> vertices = new ArrayList<ChainPoint>();
        vertices.addAll(this.chainBorderElements);
        for (int i = 0; i < this.chainBorderElements.size(); i++)
        {
            if (i + 2 < this.chainBorderElements.size())
            {
                ChainPoint previous = this.chainBorderElements.get(i);
                ChainPoint actual = this.chainBorderElements.get(i + 1);
                ChainPoint next = this.chainBorderElements.get(i + 2);

                if (previous.x == actual.x && actual.x == next.x || previous.y == actual.y && actual.y == next.y)
                {
                    vertices.remove(actual);
                }
                // if (previous.x == actual.x && Math.abs(actual.x - next.x) > 0
                // || previous.y == actual.y && Math.abs(actual.y - next.y) > 0)
                // {
                // vertices.add(actual);
                // }
            }
        }

        return vertices;
    }

    private ChainPoint getImageInitialBorderPoint(Image image)
    {
        for (int row = 0; row < image.getRows(); row++)
        {
            for (int col = 0; col < image.getCols(); col++)
            {
                if (image.getPixel(row, col) == 0)
                {
                    return new ChainPoint(row, col, 4);
                }
            }
        }
        return null;
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
            int initialX = 0, finalX = 0, initialY = 0, finalY = 0;
            boolean left2right = false;
            boolean right2left = false;

            if (previousPoint.x < nextPoint.x && previousPoint.y < nextPoint.y)
            {
                initialX = previousPoint.x;
                finalX = nextPoint.x;
                initialY = previousPoint.y;
                finalY = nextPoint.y;
                left2right = true;
            }
            else if (previousPoint.x > nextPoint.x && previousPoint.y > nextPoint.y)
            {
                initialX = nextPoint.x;
                finalX = previousPoint.x;
                initialY = nextPoint.y;
                finalY = previousPoint.y;
                left2right = true;
            }

            if (left2right)
            {
                int j = initialY;
                for (int i = initialX; i < finalX; i++)
                {
                    if (j + 1 < this.getOriginalImage().getRows())
                        resultImage.setPixel(i, j++, 0);
                }
                return;
            }

            if (previousPoint.x < nextPoint.x && previousPoint.y > nextPoint.y)
            {
                initialX = previousPoint.x;
                finalX = nextPoint.x;
                initialY = previousPoint.y;
                finalY = nextPoint.y;
                right2left = true;
            }
            else if (nextPoint.x < previousPoint.x && previousPoint.y < nextPoint.y)
            {
                initialX = nextPoint.x;
                finalX = previousPoint.x;
                initialY = nextPoint.y;
                finalY = previousPoint.y;
                right2left = true;
            }

            if (right2left)
            {
                int j = initialY;
                for (int i = initialX; i < finalX; i++)
                {
                    if ((j - 1) >= 0)
                        resultImage.setPixel(i, j--, 0);
                }
            }

        }

    }

}
