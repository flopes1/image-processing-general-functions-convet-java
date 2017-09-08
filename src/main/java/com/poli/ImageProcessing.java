package com.poli;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.poli.model.EnumFilterType.EnumFilter;
import com.poli.model.Mask;

public class ImageProcessing
{

    private String imagePath;
    private BufferedImage originalImage;
    private BufferedImage newImage;
    private Mask mask;

    public ImageProcessing(String imagePath) throws IOException
    {
        this.setImagePath(imagePath);
        this.loadImage(this.getImagePath());
    }

    public void convert2GrayScale()
    {
        BufferedImage grayImage = new BufferedImage(this.getOriginalImage().getWidth(),
                this.getOriginalImage().getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(this.getOriginalImage(), grayImage);
        this.setNewImage(grayImage);
    }

    /**
     * Aplica o filtro mediano com mascara definido pelo parametro passado
     * 
     * @param maskRate
     *            taxa que define o grau da mascara, deve ser impar
     */
    public void applyMedianFilter(int maskRate)
    {

        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.MEDIAN);
        this.applyImageFilter();
    }

    public void applyHighBoostFilter(double d)
    {
        for (int row = 0; row < this.getNewImage().getHeight(); row++)
        {
            for (int col = 0; col < this.getNewImage().getWidth(); col++)
            {
                int firstImagePixel = this.getOriginalImage().getRGB(col, row);
                firstImagePixel = (firstImagePixel & 0x000000ff);
                int secondImagePixel = this.getNewImage().getRGB(col, row);
                secondImagePixel = (secondImagePixel & 0x000000ff);

                // int resultPixel = 0;
                //
                // if (op.equals(EnumOperationType.MINUS))
                // {
                //
                // }
                // else if (op.equals(EnumOperationType.PLUS))
                // {
                // resultPixel = firstImagePixel + secondImagePixel;
                // }
                //
                // if (resultPixel < 0)
                // {
                // resultPixel = 0;
                // }

                if (secondImagePixel > firstImagePixel)
                {
                    try
                    {
                        int resultPixel = (int)(firstImagePixel + (d * (secondImagePixel - firstImagePixel)));
                        if (resultPixel > 255)
                        {
                            resultPixel = 255;
                        }
                        Color color = new Color(resultPixel, resultPixel, resultPixel);
                        this.getNewImage().setRGB(col, row, color.getRGB());
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                }

            }
        }

    }

    private void applyImageFilter()
    {
        for (int row = 0; row < this.newImage.getHeight(); row++)
        {
            for (int col = 0; col < this.newImage.getWidth(); col++)
            {
                if (this.getMask().isValidRegion(row, col, this.newImage.getHeight(), this.newImage.getWidth()))
                {
                    int rate = this.getMask().getRate();
                    int rateMid = rate / 2;

                    int result = this.getMask().calculateMaskResult(
                            this.getNewImage().getSubimage(col - rateMid, row - rateMid, rate, rate));
                    Color color = new Color(result, result, result);
                    this.getNewImage().setRGB(col, row, color.getRGB());
                }

            }
        }
    }

    public void showOriginalImage()
    {
        this.showImage(this.getOriginalImage());
    }

    public void showNewImage()
    {
        this.showImage(this.getNewImage());
    }

    private void showImage(BufferedImage image)
    {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

    public void saveImage(String path) throws IOException
    {
        String extension = path.substring(path.lastIndexOf('.') + 1);

        File outputfile = new File(path);
        ImageIO.write(this.getNewImage(), extension, outputfile);
    }

    private void loadImage(String imagePath) throws IOException
    {
        File imageFile = new File(imagePath);

        if (!imageFile.exists())
        {
            throw new IllegalArgumentException("The image does not exists");
        }

        this.setOriginalImage(ImageIO.read(imageFile));
        this.setNewImage(ImageIO.read(imageFile));
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public BufferedImage getOriginalImage()
    {
        return originalImage;
    }

    public void setOriginalImage(BufferedImage image)
    {
        this.originalImage = image;
    }

    public BufferedImage getNewImage()
    {
        return newImage;
    }

    public void setNewImage(BufferedImage image)
    {
        this.newImage = image;
    }

    public Mask getMask()
    {
        return this.mask;
    }

    public void setMask(Mask mask)
    {
        this.mask = mask;
    }

}
