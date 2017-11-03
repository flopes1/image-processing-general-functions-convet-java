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
import com.poli.model.segmentation.detection.edge.EdgeDetection;
import com.poli.model.segmentation.detection.edge.SobelOperator;
import com.poli.model.segmentation.detection.region.RegionDetection;
import com.poli.model.segmentation.detection.region.RegionDetectionUsingHistogram;
import com.poli.model.segmentation.threshold.AdaptativeThreshold;
import com.poli.model.segmentation.threshold.GlobalThreshold;
import com.poli.model.segmentation.threshold.HistogramGroupThreshold;
import com.poli.model.segmentation.threshold.OtsuThreshold;
import com.poli.model.segmentation.threshold.util.ImageThreshold;
import com.poli.model.segmentation.threshold.util.ThresholdType;

public class ImageProcessing
{
    private String imagePath;
    private Image originalImage;
    private Image newImage;
    private Filter filter;
    private EdgeDetection edgeDetection;
    private ImageThreshold imageThreshold;
    private RegionDetection regionDetection;

    public ImageProcessing(String imagePath) throws IOException
    {
        this.setImagePath(imagePath);
        this.loadImage(this.getImagePath());
    }
    
    public void detectImageRegionsUsingHistogram()
    {
        this.regionDetection = new RegionDetectionUsingHistogram(this.newImage);
        this.newImage = this.regionDetection.detectRegion();
    }

    public void binarizeImage(ThresholdType type, boolean use2ClassThreshold)
    {
        if (type.equals(ThresholdType.OTSU))
        {
            this.imageThreshold = new OtsuThreshold(this.originalImage, use2ClassThreshold);
        }
        else if (type.equals(ThresholdType.GLOBAL))
        {
            this.imageThreshold = new GlobalThreshold(this.originalImage, 5);
        }
        else if (type.equals(ThresholdType.HISTOGRAM_GROUP))
        {
            this.imageThreshold = new HistogramGroupThreshold(this.originalImage);
        }
        else if (type.equals(ThresholdType.ADAPTATIVE))
        {
            this.imageThreshold = new AdaptativeThreshold(this.originalImage);
        }

        Image newImage = this.imageThreshold.binarizeImage();
        this.newImage = null;
        this.newImage = newImage;
    }

    /**
     * Aplica a detec��o de bordas na imagem utilizando o operador sobel. O resultado � uma imagem binarizada cujo
     * threshold � calculado de acordo com o parametro
     * 
     * @param type
     *            tipo do algoritmo de analise do threshold
     */
    public void detectImageBorderWithSobelOperator(ThresholdType type, boolean use2ClassThreshold)
    {
        this.edgeDetection = new SobelOperator(this.originalImage, type, use2ClassThreshold);
        this.newImage = this.edgeDetection.detectEdges();
    }

    /**
     * Adiciona ruido na imagem de acordo com uma porcentagem. O novo valor do pixel estar� dentro do range passado
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
     * Adiciona ruido na imagem de acordo com a porcentagem. O novo valor do pixel ser� 0
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
            throw new IllegalArgumentException("Entradas inv�lidas!");
        }

        NoiseGenerator noiseGenerator = new NoiseGenerator(new RandomNoise(type, chance, initialRange, finalRange));

        this.newImage = noiseGenerator.addNoise(this.newImage);
    }

    /**
     * Adiciona ruido em cada pixel da imagem de acordo com um valor aleatorio de uma fun��o gaussiana
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
     * Aplica o filtro passa alta de alto refor�o
     * 
     * @param originalUseRate
     *            constante que determina o quanto da imagem origal ser� utilizada
     * @param lowPassFilter
     *            filtro passa baixa que ser� utilizado
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
