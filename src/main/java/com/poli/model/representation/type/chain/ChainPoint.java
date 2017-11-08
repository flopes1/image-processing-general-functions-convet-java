package com.poli.model.representation.type.chain;

import java.awt.Point;

public class ChainPoint extends Point
{

    private static final long serialVersionUID = -4439933598676697182L;

    private int direction;

    public ChainPoint(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public ChainPoint(int x, int y, int direction)
    {
        this(x, y);
        this.direction = direction;
    }

    public int getDirection()
    {
        return direction;
    }
    
    public void setDirection(int direction)
    {
        this.direction = direction;
    }

    @Override
    public String toString()
    {
        return "(" + this.x + " x " + this.y + ") - Direction " + this.direction;
    }

}
