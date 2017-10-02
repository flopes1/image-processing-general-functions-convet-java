package com.poli.model;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.poli.model.util.Histogram;

public class Image
{

    private BufferedImage image;
    private Histogram histogram;

    public Image(BufferedImage image)
    {
        this.image = image;
    }

    public int getPixel(int row, int col)
    {
        int pixel = this.image.getRGB(col, row);
        return (pixel & 0x000000ff);
    }

    public void setPixel(int row, int col, int value)
    {
        value = this.normalizeValue(value);
        Color color = new Color(value, value, value);
        this.image.setRGB(col, row, color.getRGB());
    }

    private int normalizeValue(int value)
    {
        value = value > 255 ? 255 : value;
        value = value < 0 ? 0 : value;
        return value;
    }

    public void showImage()
    {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(this.image)));
        frame.pack();
        frame.setVisible(true);
    }

    public void convert2GrayScale()
    {
        BufferedImage grayImage = new BufferedImage(this.image.getWidth(), this.image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(this.image, grayImage);
        this.image = grayImage;
    }

    public BufferedImage getSource()
    {
        return this.image;
    }

    public int getCols()
    {
        return this.image.getWidth();
    }

    public int getRows()
    {
        return this.image.getHeight();
    }

    public Image getSubimage(int row, int col, int width, int height)
    {
        BufferedImage cropedImage = this.image.getSubimage(col, row, width, height);
        return new Image(cropedImage);
    }

    public Image cloneImage()
    {
        BufferedImage image = this.image.getSubimage(0, 0, this.image.getWidth(), this.image.getHeight());
        return new Image(image);
    }

    public void showHistogram()
    {
        this.histogram = new Histogram(this);
        this.histogram.display();
    }

}
