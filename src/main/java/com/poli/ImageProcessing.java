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
import com.poli.model.Filter;

public class ImageProcessing
{
    private String imagePath;
    private BufferedImage originalImage;
    private BufferedImage newImage;
    private Filter filter;

    public ImageProcessing(String imagePath) throws IOException
    {
        this.setImagePath(imagePath);
        this.loadImage(this.getImagePath());
    }

    /**
     * Aplica o filtro passa baixa mediana
     * 
     * @param maskRate
     *            taxa que define o tamanho da mascara, deve ser impar
     */
    public void applyMedianFilter(int maskRate)
    {
        this.filter = new Filter(this.newImage);
        this.newImage = this.filter.applyMeanFilter(maskRate);
    }

    /**
     * Aplica o filtro passa baixa media
     * 
     * @param maskRate
     *            taxa que define o tamanho da mascara, deve ser impar
     */
    public void applyMeanFilter(int maskRate)
    {
        this.filter = new Filter(this.newImage);
        this.newImage = this.filter.applyMeanFilter(maskRate);
    }

    /**
     * Aplica o filtro passa baixa kuwahara
     * 
     * @param maskRate
     *            taxa que define o tamanho da mascara, deve ser impar
     */
    public void applyKuwaharaFilter(int maskRate)
    {
        this.filter = new Filter(this.newImage);
        this.newImage = this.filter.applyKuwaharaFilter(maskRate);
    }

    /**
     * Aplica o filtro passa alta de alto reforço
     * 
     * @param originalUseRate
     *            constante que determina o quanto da imagem origal será utilizada
     * @param lowPassFilter
     *            filtro passa baixa que será utilizado
     */
    public void applyHighBoostFilter(double originalUseRate, EnumFilter lowPassFilter)
    {
        this.filter = new Filter(this.newImage);
        this.newImage = this.filter.applyHighBoostFilter(originalUseRate, lowPassFilter);
    }
    
    public void applyIdealHighPassFilter(int diameter)
    {
        this.filter = new Filter(this.newImage);
        this.newImage = this.filter.applyIdealHighPassFilter(diameter);
    }
    
    public void convert2GrayScale()
    {
        BufferedImage grayImage = new BufferedImage(this.getOriginalImage().getWidth(),
                this.getOriginalImage().getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(this.getOriginalImage(), grayImage);
        this.setNewImage(grayImage);
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

}
