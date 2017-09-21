package com.poli;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.MEDIAN);
        this.applyImageFilter();
    }

    public void applyMeanFilter(int maskRate)
    {

        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.MEAN);
        this.applyImageFilter();
    }

    public void applyLaplaceFilter(int rate)
    {
        this.mask = new Mask(rate, EnumFilter.LAPLACIAN);
        this.applyImageFilter();
    }

    public void applyKuwaharaFilter(int maskRate)
    {
        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e/ou impar");
        }

        this.mask = new Mask(maskRate, EnumFilter.KUWAHARA);
        this.applyImageFilter();
    }

    @Deprecated
    public void applyUnsharpMasking()
    {
        // this.applyHighBoostFilter(1);
    }

    public void applyHighBoostFilter(double increaseRate, EnumFilter lowPassFilter)
    {
        if (lowPassFilter.equals(EnumFilter.MEDIAN))
        {
            this.applyMedianFilter(3);
        }
        else if (lowPassFilter.equals(EnumFilter.KUWAHARA))
        {
            this.applyKuwaharaFilter(3);
        }
        else if (lowPassFilter.equals(EnumFilter.MEAN))
        {
            this.applyMeanFilter(3);
        }

        int[][] highPassFilter = this.getHighPassFilter(increaseRate);

        List<Integer> list = this.convert2List(highPassFilter);
        double max = list.stream().mapToInt(i -> i).max().getAsInt();
        double min = list.stream().mapToInt(i -> i).min().getAsInt();

        for (int row = 0; row < this.newImage.getHeight(); row++)
        {
            for (int col = 0; col < this.newImage.getWidth(); col++)
            {
                int original = this.originalImage.getRGB(col, row);
                original = (original & 0x000000ff);

                int highValue = highPassFilter[col][row];

                int resultPixel = (int) ((increaseRate) * original - highValue);

                resultPixel = this.mask.normalizeValue(resultPixel, list, max, min);

                Color color = new Color(resultPixel, resultPixel, resultPixel);
                this.newImage.setRGB(col, row, color.getRGB());

            }
        }

    }

    private List<Integer> convert2List(int[][] highPassFilter)
    {

        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < highPassFilter.length; i++)
        {
            for (int j = 0; j < highPassFilter[i].length; j++)
            {
                list.add(highPassFilter[i][j]);
            }
        }

        return list;
    }

    private int[][] getHighPassFilter(double increaseRate)
    {
        int[][] highPass = new int[this.originalImage.getWidth()][this.originalImage.getHeight()];

        for (int row = 0; row < this.newImage.getHeight(); row++)
        {
            for (int col = 0; col < this.newImage.getWidth(); col++)
            {
                int original = this.originalImage.getRGB(col, row);
                original = (original & 0x000000ff);

                int lowPass = this.newImage.getRGB(col, row);
                lowPass = (lowPass & 0x000000ff);

                int value = (int) (lowPass - original);
                highPass[col][row] = value;
            }
        }

        return highPass;
    }

    private void applyImageFilter()
    {
        int rate = this.getMask().getRate();
        int rateMid = (rate - 1) / 2;

        BufferedImage padding = this.getPeddingImg();

        for (int row = 0; row < padding.getHeight(); row++)
        {
            for (int col = 0; col < padding.getWidth(); col++)
            {
                if (this.mask.isValidRegion(row, col, padding.getHeight(), padding.getWidth()))
                {
                    int result = this.mask
                            .calculateMaskResult(padding.getSubimage(col - rateMid, row - rateMid, rate, rate));
                    Color color = new Color(result, result, result);
                    padding.setRGB(col, row, color.getRGB());
                }
            }
        }

        this.newImage = this.removePadding(padding);
    }

    private BufferedImage removePadding(BufferedImage padding)
    {
        int paddingRate = this.mask.getRate();
        paddingRate = (paddingRate - 1) / 2;

        return padding.getSubimage(paddingRate, paddingRate, this.newImage.getWidth(), this.newImage.getHeight());
    }

    private BufferedImage getPeddingImg()
    {
        int rate = this.getMask().getRate();
        int rateMid = (rate - 1) / 2;

        BufferedImage padding = new BufferedImage(this.newImage.getWidth() + rate - 1,
                this.newImage.getHeight() + rate - 1, BufferedImage.TYPE_BYTE_GRAY);

        for (int row = 0; row < this.newImage.getHeight() + rate - 1; row++)
        {
            for (int col = 0; col < this.newImage.getWidth() + rate - 1; col++)
            {
                if (this.mask.isValidRegion(row, col, this.newImage.getHeight() + rate - 1,
                        this.newImage.getWidth() + rate - 1))
                {
                    int pixel = this.newImage.getRGB(col - rateMid, row - rateMid);
                    pixel = (pixel & 0x000000ff);
                    // pixel = 150;
                    Color color = new Color(pixel, pixel, pixel);
                    padding.setRGB(col, row, color.getRGB());
                }
                else
                {
                    Color color = new Color(0, 0, 0);
                    padding.setRGB(col, row, color.getRGB());
                }

            }
        }

        return padding;
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
