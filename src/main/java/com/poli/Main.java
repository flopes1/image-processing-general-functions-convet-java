package com.poli;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.lang.model.element.Modifier;

import com.poli.model.Image;
import com.poli.model.morphology.MorphologicalOperation;
import com.poli.model.morphology.common.EnumMorphologyConnection;
import com.poli.model.morphology.common.EnumMorphologyOperation;
import com.poli.model.morphology.logic.LogicOperation;
import com.poli.model.morphology.set.SetOperation;
import com.poli.model.representation.ImageRepresentation;
import com.poli.model.representation.type.chain.ChainCodeRepresentation;
import com.poli.model.representation.type.chain.EnumChainDirectionType;
import com.poli.model.segmentation.threshold.util.ThresholdType;
import com.poli.model.util.ImageUtils;

public class Main
{

    public static void main(String[] args)
    {
        ClassLoader classLoader = Main.class.getClassLoader();

        File imageFile1 = new File(classLoader.getResource("imagem 1.gif").getFile());
        File imageFile2 = new File(classLoader.getResource("imagem_2_binarizada_adaptativo.png").getFile());

        // String destinyPath = "dentiny path";
        // String sourceImage = "source/1.jpg";

        try
        {
            String sourceImage1Path = URLDecoder.decode(imageFile1.getPath(), "UTF-8");
            String sourceImage2Path = URLDecoder.decode(imageFile2.getPath(), "UTF-8");
            String resourcePath = URLDecoder.decode(imageFile1.getParent(), "UTF-8");

            /**
             * Código para gerar as versão 8 e 4 conectado da imagem 1, para alterar entre as versões basta trocar o
             * enum (segundo parametro)
             */
            // Image inputImage = ImageUtils.loadImage(sourceImage1Path);
            // ImageRepresentation imageRepresentation = new ChainCodeRepresentation(inputImage,
            // EnumChainDirectionType.EIGHT_DIRETION);
            // imageRepresentation.generateImageRepresentation(true);

            /**
             * Código para gerar as versão 8 e 4 conectado da imagem 2, para alterar entre as versões basta trocar o
             * enum (segundo parametro). A imagem de entrada foi binarizada utilizando o threshold adaptativo da atividade
             * de segmentação.
             */
            Image inputImage2 = ImageUtils.loadImage(sourceImage1Path);
            ImageRepresentation imageRepresentation2 = new ChainCodeRepresentation(inputImage2,
                    EnumChainDirectionType.EIGHT_DIRETION);
            imageRepresentation2.generateImageRepresentation(true);

            ImageUtils.saveImage(imageRepresentation2.getResultImage(),
                    resourcePath + "/imagem1_chain8.png");

        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

    }

}
