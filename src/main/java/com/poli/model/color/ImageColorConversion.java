package com.poli.model.color;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.poli.model.Image;

public class ImageColorConversion
{

    public ImageColorConversion()
    {
    }

    public static Image grayScale2rgb(Image inputImage)
    {
        BufferedImage outputImage = new BufferedImage(inputImage.getCols(), inputImage.getRows(),
                BufferedImage.TYPE_INT_RGB);

        HashMap<Integer, Integer> pixelDist = inputImage.getPixelDistribuction();
        ArrayList<Integer> values = new ArrayList<Integer>(pixelDist.keySet());
        Collections.sort(values);

        HashMap<String, Integer> palet = buidColorPalet(values, pixelDist);

        for (int row = 0; row < inputImage.getRows(); row++)
        {
            for (int col = 0; col < inputImage.getCols(); col++)
            {
                try
                {
                    int gray = inputImage.getPixel(row, col);

                    // String color = "BLACK";
                    //
                    // if (gray > 240)
                    // {
                    // color = "WHITE";
                    // }
                    // else if (gray > 180 && gray < 200)
                    // {
                    // color = "YELLOW";
                    // }
                    // else if (gray > 170 && gray < 184)
                    // {
                    // color = "GREEN";
                    // }
                    // else if (gray < 60 && gray > 45)
                    // {
                    // color = "RED";
                    // }
                    // else if (gray < 100 && gray > 90)
                    // {
                    // color = "BLUE";
                    // }
                    String color = getColorOfValue(palet, gray);
                    int rgb = getRgbFromString(color);
                    outputImage.setRGB(col, row, rgb);
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }

        return new Image(outputImage);
    }

    private static String getColorOfValue(HashMap<String, Integer> palet, int gray)
    {
        int closest = -100;
        for (Integer value : palet.values())
        {
            if (Math.abs(gray - value) < Math.abs(gray - closest))
            {
                closest = value;
            }
        }

        for (String key : palet.keySet())
        {
            if (palet.get(key) == closest)
            {
                return key;
            }
        }

        return "BLACK";
    }

    private static HashMap<String, Integer> buidColorPalet(ArrayList<Integer> values,
            HashMap<Integer, Integer> pixelDist)
    {
        int previous = -1;

        HashMap<String, Integer> palet = new HashMap<String, Integer>();

        int index = 0;

        for (Integer key : values)
        {
            int value = pixelDist.get(key);
            if (previous == -1 || (key * 0.75 > previous && value > 100))
            {
                previous = key;
                if (index < COLOR_PALET.length)
                {
                    palet.put(COLOR_PALET[index], previous);
                    index++;
                }
            }

        }

        return palet;
    }

    private static int getRgbFromString(String color)
    {
        int red = 0, green = 0, blue = 0;

        switch (color)
        {
            case "BLACK":
                break;
            case "RED":
                red = 255;
                break;
            case "BLUE":
                blue = 255;
                break;
            case "GREEN":
                green = 255;
                break;
            case "YELLOW":
                red = 255;
                green = 255;
                break;
            case "WHITE":
                red = 255;
                blue = 255;
                green = 255;
                break;
        }
        return ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 0);
    }

    private static final String[] COLOR_PALET = new String[]
    { "BLACK", "RED", "BLUE", "YELLOW", "WHITE" };

}
