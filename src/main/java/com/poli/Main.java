package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import com.poli.model.EnumFilterType.EnumFilter;

public class Main
{

    public static void main(String[] args)
    {

        ClassLoader classLoader = Main.class.getClassLoader();
        File imageFile = new File(classLoader.getResource("1.png").getFile());

        // String sourceImage = "source/1.jpg";
        // String destinyPath = "dentiny path";

        try
        {

            String sourceImagePath = URLDecoder.decode(imageFile.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile.getParent(), "UTF-8");

            ImageProcessing imgProces = new ImageProcessing(sourceImagePath);
            
            imgProces.applyIdealHighPassFilter(10);

            imgProces.saveImage(resourcePath + "/1_.png");

        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
