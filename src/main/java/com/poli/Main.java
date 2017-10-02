package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

public class Main
{

    public static void main(String[] args)
    {
        ClassLoader classLoader = Main.class.getClassLoader();

        File imageFile = new File(classLoader.getResource("1_random_0.jpg").getFile());

        // String destinyPath = "dentiny path";
        // String sourceImage = "source/1.jpg";

        try
        {
            String sourceImagePath = URLDecoder.decode(imageFile.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile.getParent(), "UTF-8");

            ImageProcessing imgProces = new ImageProcessing(sourceImagePath);

            // imgProces.addRandomNoiseZero(25);
            // imgProces.addRandomNoise0Or255(15);
            // imgProces.addGaussianNoise(10, 50);

            // imgProces.showImageHistogram();
            imgProces.applyMaxFilter(3);

            imgProces.saveImage(resourcePath + "/1_random_0_max.jpg");
            // imgProces.saveImage(resourcePath + "/3_random_0.jpg");
            // imgProces.saveImage(resourcePath + "/3_random_0_255.jpg");
            // imgProces.saveImage(resourcePath + "/3_random_gaussian.jpg");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
