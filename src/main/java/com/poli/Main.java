package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

public class Main
{

    public static void main(String[] args)
    {
        ClassLoader classLoader = Main.class.getClassLoader();

        Runnable first = new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub

                File imageFile = new File(classLoader.getResource("1.png").getFile());

                // String sourceImage = "source/1.jpg";
                // String destinyPath = "dentiny path";

                try
                {
                    String sourceImagePath = URLDecoder.decode(imageFile.getPath(), "UTF-8");
                    String resourcePath = URLDecoder.decode(imageFile.getParent(), "UTF-8");

                    ImageProcessing imgProces = new ImageProcessing(sourceImagePath);
                    // imgProces.applyIdealHighPassFilter(10);
                    imgProces.applyButterworthHighPassFilter(20);

                    imgProces.saveImage(resourcePath + "/1_.png");
                    System.err.println("-------------- Fim Thread 1 ----------------");
                }
                catch (IOException e)
                {
                    System.err.println(e.getMessage());
                }

            }
        };

        Thread fisrtThread = new Thread(first);
        fisrtThread.start();

        Runnable second = new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub

                File imageFile = new File(classLoader.getResource("2.png").getFile());

                // String sourceImage = "source/1.jpg";
                // String destinyPath = "dentiny path";

                try
                {
                    String sourceImagePath = URLDecoder.decode(imageFile.getPath(), "UTF-8");
                    String resourcePath = URLDecoder.decode(imageFile.getParent(), "UTF-8");

                    ImageProcessing imgProces = new ImageProcessing(sourceImagePath);
                    // imgProces.applyIdealHighPassFilter(5);
                    imgProces.applyButterworthHighPassFilter(25);

                    System.err.println("-------------- Fim Thread 2 ----------------");
                    imgProces.saveImage(resourcePath + "/2_.png");

                }
                catch (IOException e)
                {
                    System.err.println(e.getMessage());
                }

            }
        };

        Thread secondThread = new Thread(second);
        secondThread.start();

    }

}
