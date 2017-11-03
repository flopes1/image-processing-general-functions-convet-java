package com.poli.model.morphology.set.operation;

import com.poli.model.Image;
import com.poli.model.morphology.set.SpacialTransformOperation;

public class ExtractionTransformOperation extends SpacialTransformOperation
{
    private ErosionTransformOperation erosionTransformOperation;

    /**
     * Como a lista de exercícios não especifica se a extração é de fronteiro ou de componentes conexos, escolhi a
     * primeira opção para implementar.
     */
    public ExtractionTransformOperation()
    {
        this.erosionTransformOperation = new ErosionTransformOperation();
    }

    /**
     * A extração de fronteira, cosiste na subtração da imagem original pela sua versão após sofrer erosão
     */
    @Override
    public Image applyTransformation(Image originalImage, int[][] structuringElement)
    {
        Image resultImage = new Image(originalImage.cloneImage());

        Image erosionResult = this.erosionTransformOperation.applyTransformation(originalImage, structuringElement);

        for (int row = 0; row < originalImage.getRows(); row++)
        {
            for (int col = 0; col < originalImage.getCols(); col++)
            {
                if (originalImage.getPixel(row, col) < 255 && erosionResult.getPixel(row, col) == 255)
                {
                    resultImage.setPixel(row, col, 0);
                }
                else
                {
                    resultImage.setPixel(row, col, 255);
                }
            }
        }

        return resultImage;
    }

}
