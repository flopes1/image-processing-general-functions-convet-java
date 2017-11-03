package com.poli.model;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;

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

    public Image(Image image)
    {
        this.image = image.getSource();
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
        ColorModel cm = this.image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = this.image.copyData(this.image.getRaster().createCompatibleWritableRaster());
        BufferedImage cloneImage = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        return new Image(cloneImage);
    }

    public HashMap<Integer, Integer> getPixelDistribuction()
    {
        HashMap<Integer, Integer> pixelDistribution = new HashMap<Integer, Integer>();

        for (int i = 0; i < 256; i++)
        {
            pixelDistribution.put(i, 0);
        }

        for (int i = 0; i < this.getRows(); i++)
        {
            for (int j = 0; j < this.getCols(); j++)
            {
                int currentPixel = this.getPixel(i, j);

                int value = pixelDistribution.get(currentPixel);
                pixelDistribution.put(currentPixel, ++value);

            }
        }

        return pixelDistribution;
    }

    public void showHistogram()
    {
        this.histogram = new Histogram(this);
        this.histogram.display();
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean isEquals = true;

        if (obj instanceof Image)
        {
            Image otherImage = (Image) obj;
            if (otherImage.getRows() != this.getRows() || otherImage.getCols() != this.getCols())
            {
                isEquals = false;
            }
            else
            {
                for (int row = 0; row < this.getRows(); row++)
                {
                    for (int col = 0; col < this.getCols(); col++)
                    {
                        if (this.getPixel(row, col) != otherImage.getPixel(row, col))
                        {
                            isEquals = false;
                            break;
                        }
                    }
                    if (!isEquals)
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            isEquals = false;
        }

        return isEquals;
    }

    public void clean()
    {
        for (int row = 0; row < this.getRows(); row++)
        {
            for (int col = 0; col < this.getCols(); col++)
            {
                this.setPixel(row, col, 255);
            }
        }

    }

}
