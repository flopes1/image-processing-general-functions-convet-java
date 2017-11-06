package com.poli.model.util;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.poli.model.Image;

public class ImageUtils
{

    private ImageUtils()
    {
    }

    public static Image loadImage(String imagePath) throws IOException
    {
        File imageFile = new File(imagePath);

        if (!imageFile.exists())
        {
            throw new IllegalArgumentException("The image does not exists");
        }

        return new Image(ImageIO.read(imageFile));
    }

    public static void saveImage(Image image, String path)
    {
        String extension = path.substring(path.lastIndexOf('.') + 1);

        File outputfile = new File(path);
        try
        {
            ImageIO.write(image.getSource(), extension, outputfile);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
