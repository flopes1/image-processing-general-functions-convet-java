package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import com.poli.model.Image;
import com.poli.model.color.ImageColorConversion;
import com.poli.model.compress.CompressImage;
import com.poli.model.util.ImageUtils;

public class Main
{

    public static void main(String[] args)
    {
        ClassLoader classLoader = Main.class.getClassLoader();

        File imageFile1 = new File(classLoader.getResource("imagem 2.tif").getFile());

        try
        {
            String sourceImage1Path = URLDecoder.decode(imageFile1.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile1.getParent(), "UTF-8");

            /**
             * Todos os c�digos devem ser executados 1 de cada vez
             */

            /**
             * C�digo para resolu��o da 1 quest�o. O algoritmo implementado � o RLE o resultado da compress�o esta no
             * formato txt, logo em seguida a imagem � reconstruida usando o txt
             */
            Image image = ImageUtils.loadImage(sourceImage1Path);
            CompressImage compressImage = new CompressImage(image);
            byte[] encoded = compressImage.compressWithoutLoss();
            ImageUtils.saveImageAsByteArray(encoded, resourcePath + "/imagem 2_resultado.txt");
            Image descompressed = compressImage.descompressWithoutLoss(resourcePath + "/imagem 2_resultado.txt");
            ImageUtils.saveImage(descompressed, resourcePath + "/imagem 2_.tif");

            /**
             * C�digo para solu��o da 2 quest�o, passar como parametro a imagem 1 ou 2
             */
            Image image2 = ImageUtils.loadImage(sourceImage1Path);
            CompressImage compressImage2 = new CompressImage(image2);
            Image result = compressImage2.compressWithLoss();
            ImageUtils.saveImage(result, resourcePath + "/imagem 2_.tif");

            /**
             * C�digo para solu��o da 3 quest�o
             */
            // TODO

            /**
             * C�digo para resolu��o da 4 quest�o
             */
            Image image3 = ImageUtils.loadImage(sourceImage1Path);
            Image result3 = ImageColorConversion.grayScale2rgb(image3);
            // result.showImage();
            ImageUtils.saveImage(result3, resourcePath + "/imagem 4_resultado.jpg");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
