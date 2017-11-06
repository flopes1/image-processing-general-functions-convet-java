package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import com.poli.model.Image;
import com.poli.model.morphology.MorphologicalOperation;
import com.poli.model.morphology.common.EnumMorphologyConnection;
import com.poli.model.morphology.common.EnumMorphologyOperation;
import com.poli.model.morphology.logic.LogicOperation;
import com.poli.model.morphology.set.SetOperation;
import com.poli.model.util.ImageUtils;

public class Main
{

    public static void main(String[] args)
    {
        ClassLoader classLoader = Main.class.getClassLoader();

        File imageFile1 = new File(classLoader.getResource("oppening-8.gif").getFile());

        // String destinyPath = "dentiny path";
        // String sourceImage = "source/1.jpg";

        try
        {
            String sourceImage1Path = URLDecoder.decode(imageFile1.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile1.getParent(), "UTF-8");

            MorphologicalOperation morphologicalOperationTransform = new SetOperation(sourceImage1Path,
                    EnumMorphologyOperation.EXTRACTION, EnumMorphologyConnection.EIGHT);
            ((SetOperation) morphologicalOperationTransform).applyOperation();

            morphologicalOperationTransform.saveResult(resourcePath + "/extract-8.gif");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
