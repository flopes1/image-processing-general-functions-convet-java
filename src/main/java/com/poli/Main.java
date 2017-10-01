package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

public class Main
{

    public static void main(String[] args)
    {
        ClassLoader classLoader = Main.class.getClassLoader();

        File imageFile = new File(classLoader.getResource("1.png").getFile());

        // String destinyPath = "dentiny path";
        // String sourceImage = "source/1.jpg";

        try
        {
            String sourceImagePath = URLDecoder.decode(imageFile.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile.getParent(), "UTF-8");

            ImageProcessing imgProces = new ImageProcessing(sourceImagePath);


            imgProces.saveImage(resourcePath + "/1_.png");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
