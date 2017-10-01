package com.poli;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.poli.model.Image;
import com.poli.model.filter.EnumFilterType.EnumFilter;
import com.poli.model.filter.Filter;

public class ImageProcessing
{
    private String imagePath;
    private Image originalImage;
    private Image newImage;
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

    public void applyButterworthHighPassFilter(int diameter)
    {
        this.filter = new Filter(this.newImage);
        this.newImage = this.filter.applyButterworthHighPassFilter(diameter, 2);
    }

    public void showOriginalImage()
    {
        this.originalImage.showImage();
    }

    public void showNewImage()
    {
        this.newImage.showImage();
    }

    public void saveImage(String path) throws IOException
    {
        String extension = path.substring(path.lastIndexOf('.') + 1);

        File outputfile = new File(path);
        ImageIO.write(this.newImage.getSource(), extension, outputfile);
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

    public Image getOriginalImage()
    {
        return originalImage;
    }

    public void setOriginalImage(BufferedImage image)
    {
        this.originalImage = new Image(image);
    }

    public Image getNewImage()
    {
        return newImage;
    }

    public void setNewImage(BufferedImage image)
    {
        this.newImage = new Image(image);
    }

}
