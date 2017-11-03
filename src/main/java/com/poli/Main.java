package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import com.poli.model.morphology.MorphologicalOperation;
import com.poli.model.morphology.common.EnumMorphologyConnection;
import com.poli.model.morphology.common.EnumMorphologyOperation;
import com.poli.model.morphology.set.SetOperation;

public class Main
{

    public static void main(String[] args)
    {
        ClassLoader classLoader = Main.class.getClassLoader();

        File imageFile1 = new File(classLoader.getResource("extraction-4.png").getFile());
        File imageFile2 = new File(classLoader.getResource("imagem2.png").getFile());

        // String destinyPath = "dentiny path";
        // String sourceImage = "source/1.jpg";

        try
        {
            String sourceImage1Path = URLDecoder.decode(imageFile1.getPath(), "UTF-8");
            String sourceImage2Path = URLDecoder.decode(imageFile2.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile1.getParent(), "UTF-8");

            // Image otherImage = ImageUtils.loadImage(sourceImage2Path);

            // Para obter a operação booleana desejada basta trocar o enum do segundo parametro
            // MorphologicalOperation morphologicalOperation = new LogicOperation(sourceImage1Path,
            // EnumMorphologyOperation.AND);
            // morphologicalOperation.applyOperation(otherImage);

            // morphologicalOperation.showNewImage();
            // morphologicalOperation.saveResult(resourcePath + "/AND.png");

            MorphologicalOperation morphologicalOperation = new SetOperation(sourceImage1Path,
                    EnumMorphologyOperation.FILLING, EnumMorphologyConnection.FOUR);
            ((SetOperation) morphologicalOperation).applyOperation();
            morphologicalOperation.saveResult(resourcePath + "/filling-4.png");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
