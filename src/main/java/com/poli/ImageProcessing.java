package com.poli;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.poli.model.Image;
import com.poli.model.filter.EnumFilterType.EnumFilter;
import com.poli.model.filter.Filter;
import com.poli.model.filter.noise.EnumNoise;
import com.poli.model.filter.noise.GaussianNoise;
import com.poli.model.filter.noise.NoiseGenerator;
import com.poli.model.filter.noise.RandomNoise;
import com.poli.model.segmentation.EdgeDetection;
import com.poli.model.segmentation.SobelOperator;
import com.poli.model.segmentation.threshold.util.ThresholdType;

public class ImageProcessing
{
    private String imagePath;
    private Image originalImage;
    private Image newImage;
    private Filter filter;
    private EdgeDetection edgeDetection;

    public ImageProcessing(String imagePath) throws IOException
    {
        this.setImagePath(imagePath);
        this.loadImage(this.getImagePath());
    }

    /**
     * Aplica a detecção de bordas na imagem utilizando o operador sobel. O resultado é uma imagem binarizada cujo
     * threshold é calculado de acordo com o parametro
     * 
     * @param type
     *            tipo do algoritmo de analise do threshold
     */
    public void detectImageBorderWithSobelOperator(ThresholdType type)
    {
        this.edgeDetection = new SobelOperator(this.originalImage, type);
        this.newImage = this.edgeDetection.detectEdges();
    }

    /**
     * Adiciona ruido na imagem de acordo com uma porcentagem. O novo valor do pixel estará dentro do range passado
     * 
     * @param chance
     *            porcentagem de chance do ruido aparecer [0-100]%
     * @param initialValue
     *            valor inicial para escolha aleatoria do novo valor
     * @param finalValue
     *            valor final para escolha aleatoria do novo valor
     */
    public void addRandomNoise0Or255(int chance)
    {
        this.addNoise(EnumNoise.RANDOM, chance, 0, 255);
    }

    /**
     * Adiciona ruido na imagem de acordo com a porcentagem. O novo valor do pixel será 0
     * 
     * @param chance
     */
    public void addRandomNoiseZero(int chance)
    {
        this.addNoise(EnumNoise.RANDOM, chance, 0, 0);
    }

    private void addNoise(EnumNoise type, int chance, int initialRange, int finalRange)
    {
        if (chance < 0 || chance > 100)
        {
            throw new IllegalArgumentException("Entradas inválidas!");
        }

        NoiseGenerator noiseGenerator = new NoiseGenerator(new RandomNoise(type, chance, initialRange, finalRange));

        this.newImage = noiseGenerator.addNoise(this.newImage);
    }

    /**
     * Adiciona ruido em cada pixel da imagem de acordo com um valor aleatorio de uma função gaussiana
     */
    public void addGaussianNoise(int mean, int standardDeviation)
    {
        NoiseGenerator noiseGenerator = new NoiseGenerator(
                new GaussianNoise(EnumNoise.GAUSSIAN, mean, standardDeviation));
        this.newImage = noiseGenerator.addNoise(this.newImage);
    }

    public void showImageHistogram()
    {
        this.newImage.showHistogram();
    }

    /**
     * Aplica o filtro passa baixa mediana
     * 
     * @param maskRate
     *            taxa que define o tamanho da mascara, deve ser impar
     */
    public void applyMedianFilter(int maskRate)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyMedianFilter(maskRate);
    }

    public void applyMaxFilter(int maskRate)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyMaxFilter(maskRate);
    }

    public void applyMinFilter(int maskRate)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyMinFilter(maskRate);
    }

    public void applyHarmonicMeanFilter(int maskRate)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyHarmonicMeanFilter(maskRate);
    }

    public void applyContraHarmonicMeanFilter(int maskRate, double q)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyContraHarmonicMeanFilter(maskRate, q);
    }

    @Deprecated
    public void applyGeometricMeanFilter(int maskRate)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyGeometricMeanFilter(maskRate);
    }

    public void applyPointMeanFilter(int maskRate)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyPointMeanFilter(maskRate);
    }

    public void applyGaussianFilter(int maskRate)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyGaussianFilter(maskRate);
    }

    /**
     * Aplica o filtro passa baixa media
     * 
     * @param maskRate
     *            taxa que define o tamanho da mascara, deve ser impar
     */
    public void applyMeanFilter(int maskRate)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
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
        this.filter = new Filter(this.originalImage, this.newImage);
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
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyHighBoostFilter(originalUseRate, lowPassFilter);
    }

    public void applyIdealHighPassFilter(int diameter)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyIdealHighPassFilter(diameter);
    }

    public void applyButterworthHighPassFilter(int diameter)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyButterworthHighPassFilter(diameter, 2);
    }

    public void applyButterworthLowPassFilter(int diameter)
    {
        this.filter = new Filter(this.originalImage, this.newImage);
        this.newImage = this.filter.applyButterworthLowPassFilter(diameter);
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
        this.setNewImage();
    }

    public Image getNewImage()
    {
        return newImage;
    }

    public void setNewImage()
    {
        this.newImage = this.originalImage.cloneImage();
    }

}
