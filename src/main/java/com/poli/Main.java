package com.poli;

import java.io.IOException;

public class Main
{

    public static void main(String[] args)
    {

        String imagePath = Main.class.getClassLoader().getResource("1.jpg").getPath();
        String projectRootPath = System.getProperty("user.dir");

        try
        {
            ImageProcessing imgProces = new ImageProcessing(imagePath);
            // imgProces.showImage();
            imgProces.applyMedianFilter(3);
            imgProces.saveImage(projectRootPath + "\\_1.jpg");

        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
