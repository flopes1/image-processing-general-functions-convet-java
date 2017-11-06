package com.poli.model.representation.type.chain;

import java.awt.Point;

public class ChainPoint
{

    private Point point;
    private int direction;

    public ChainPoint(int x, int y, int direction)
    {
        this.point = new Point(x, y);
        this.direction = direction;
    }

    public Point getPoint()
    {
        return point;
    }

    public int getDirection()
    {
        return direction;
    }

}
