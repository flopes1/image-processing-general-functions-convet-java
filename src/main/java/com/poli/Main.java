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

        File imageFile = new File(classLoader.getResource("3_ruidosa.jpg").getFile());

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
            // imgProces.addGaussianNoise(10, 50);

            /**
             * Chamada de função que plota o histograma da imagem alterada
             */

            // imgProces.showImageHistogram();

            /**
             * Chamadas de função que aplicam filtros
             */

            imgProces.applyButterworthLowPassFilter(10);
            // imgProces.applyHighBoostFilter(1, EnumFilter.MEDIAN);
            // imgProces.applyMedianFilter(5);
            // imgProces.applyGaussianFilter(3);
            // imgProces.applyContraHarmonicMeanFilter(3, -1.5);
            // imgProces.applyHarmonicMeanFilter(5);
            // imgProces.applyMeanFilter(3);
            // imgProces.applyMaxFilter(3);
            // imgProces.applyMinFilter(3);
            // imgProces.applyPointMeanFilter(3);

            // imgProces.showNewImage();
            imgProces.saveImage(resourcePath + "/3_ruidosa_.jpg");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
