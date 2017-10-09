package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import com.poli.model.filter.EnumFilterType.EnumFilter;

public class Main
{

    public static void main(String[] args)
    {
        ClassLoader classLoader = Main.class.getClassLoader();

        File imageFile = new File(classLoader.getResource("1_gaussian(5x5)_median.jpg").getFile());
        // File imageFile = new File(classLoader.getResource("3.jpg").getFile());

        // String destinyPath = "dentiny path";
        // String sourceImage = "source/1.jpg";

        try
        {
            String sourceImagePath = URLDecoder.decode(imageFile.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile.getParent(), "UTF-8");

            ImageProcessing imgProces = new ImageProcessing(sourceImagePath);

            /**
             * Chamadas de funções que colocam ruido.
             */

            // imgProces.addRandomNoiseZero(25);
            // imgProces.addRandomNoise0Or255(15);
            // imgProces.addGaussianNoise(10, 50);// media, desvio

            /**
             * Chamada de função que plota o histograma da imagem alterada
             */

            // imgProces.showImageHistogram();

            /**
             * Chamadas de função que aplicam filtros (filtros com mascara quadrada a partir do parametro)
             */

            // imgProces.applyButterworthLowPassFilter(10); // dominio da frequencia demora 1h em média
            // imgProces.applyHighBoostFilter(1, EnumFilter.MEDIAN);
            // imgProces.applyMedianFilter(3);
            // imgProces.applyGaussianFilter(5);
            // imgProces.applyContraHarmonicMeanFilter(3, 1.5);
            // imgProces.applyHarmonicMeanFilter(3);
            // imgProces.applyMeanFilter(3);
            // imgProces.applyMaxFilter(3);
            // imgProces.applyMinFilter(3);
            // imgProces.applyPointMeanFilter(3);

            /**
             * Chamadas de funções para imprimir e salvar imagens
             */
            // imgProces.showNewImage();
            // imgProces.saveImage(resourcePath + "/1_gaussian(5x5)_median_high.jpg");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
