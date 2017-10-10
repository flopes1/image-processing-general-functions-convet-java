package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import com.poli.model.filter.EnumFilterType.EnumFilter;
import com.poli.model.filter.noise.GaussianNoise;
import com.poli.model.segmentation.OtsuThreshold;

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

            OtsuThreshold ot = new OtsuThreshold(imgProces.getOriginalImage());
            System.out.println(ot.getOtsuThreshold());

            // imgProces.showNewImage();
            // imgProces.saveImage(resourcePath + "/1_gaussian(5x5)_median_high.jpg");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
