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

            // filtro e parametros utilizados para gerar a primeira imagem
            imgProces.applyHighBoostFilter(1, EnumFilter.MEDIAN);

            // filtro e parametros utilizados para gerar a segunda imagem
            //imgProces.applyHighBoostFilter(1, EnumFilter.MEDIAN);

            imgProces.saveImage(resourcePath + "/1__.png");

        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
