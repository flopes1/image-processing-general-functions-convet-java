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

        File imageFile = new File(classLoader.getResource("1.jpg").getFile());
        // File imageFile = new File(classLoader.getResource("3.jpg").getFile());

        // String destinyPath = "dentiny path";
        // String sourceImage = "source/1.jpg";

        try
        {
            String sourceImagePath = URLDecoder.decode(imageFile.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile.getParent(), "UTF-8");

            ImageProcessing imgProces = new ImageProcessing(sourceImagePath);

            /**
             * Chamada para o metodo que detecta as bordas da imagem
             */
            // imgProces.applyMaxFilter(5);
            imgProces.detectImageBorderWithSobelOperator(ThresholdType.ADAPTATIVE);
            // imgProces.showImageHistogram();
            // imgProces.showNewImage();
            imgProces.saveImage(resourcePath + "/1_global_adaptative_otsu.jpg");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
