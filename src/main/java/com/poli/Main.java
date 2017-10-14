package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import com.poli.model.segmentation.threshold.util.ThresholdType;

public class Main
{

    public static void main(String[] args)
    {
        ClassLoader classLoader = Main.class.getClassLoader();

        File imageFile = new File(classLoader.getResource("3_1.jpg").getFile());
        // File imageFile = new File(classLoader.getResource("3.jpg").getFile());

        // String destinyPath = "dentiny path";
        // String sourceImage = "source/1.jpg";

        try
        {
            String sourceImagePath = URLDecoder.decode(imageFile.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile.getParent(), "UTF-8");

            ImageProcessing imgProces = new ImageProcessing(sourceImagePath);

            /**
             * Chamada para o metodo que gera a solução da 1ª imagem
             */
            // imgProces.detectImageBorderWithSobelOperator(ThresholdType.HISTOGRAM_GROUP, false);

            /**
             * Chamada para o metodo que gera a solução da 2ª imagem
             */
            // imgProces.detectImageBorderWithSobelOperator(ThresholdType.OTSU, true);

            /**
             * Chamada para o metodo que gera a solução da 3ª imagem Caso queira a mão com a area interna em preto, usar
             * a segunda função no resultado da primeira e a terceira no resultado da segunda. Caso não queria, executar
             * só a primeria função
             */
            // imgProces.binarizeImage(ThresholdType.ADAPTATIVE, false);
            // imgProces.detectImageBorderWithSobelOperator(ThresholdType.OTSU, true);
            // imgProces.applyMedianFilter(3);
            /**
             * Chamada para o metodo que gera a solução da 4ª imagem
             */
            // imgProces.detectImageBorderWithSobelOperator(ThresholdType.OTSU, false);

            /**
             * Utils
             */
            // imgProces.showImageHistogram();
            // imgProces.saveImage(resourcePath + "/3_1_.jpg");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
