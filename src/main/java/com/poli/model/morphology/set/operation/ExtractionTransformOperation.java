package com.poli.model.morphology.set.operation;

import com.poli.model.Image;
import com.poli.model.morphology.set.SpacialTransformOperation;

public class ExtractionTransformOperation extends SpacialTransformOperation
{
    private ErosionTransformOperation erosionTransformOperation;

    /**
     * Como a lista de exerc�cios n�o especifica se a extra��o � de fronteiro ou de componentes conexos, escolhi a
     * primeira op��o para implementar.
     */
    public ExtractionTransformOperation()
    {
        this.erosionTransformOperation = new ErosionTransformOperation();
    }

    /**
     * A extra��o de fronteira, cosiste na subtra��o da imagem original pela sua vers�o ap�s sofrer eros�o
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
