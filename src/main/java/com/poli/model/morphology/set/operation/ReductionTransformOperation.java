package com.poli.model.morphology.set.operation;

import com.poli.model.Image;
import com.poli.model.morphology.set.SpacialTransformOperation;

public class ReductionTransformOperation extends SpacialTransformOperation
{

    private ErosionTransformOperation erosionTransformOperation;
    private OpenningTransformOperation openningTransformOperation;

    public ReductionTransformOperation()
    {
        this.erosionTransformOperation = new ErosionTransformOperation();
        this.openningTransformOperation = new OpenningTransformOperation();
    }

    /**
     * Como o tópico de redução, presente na lista de exercícios, poderia ser, por definição, erosão, afinamento ou
     * esqueleto da imagem, escolhi a ultima opção para implementar.
     * 
     * @param originalImage
     * @param structuringElement
     * @return
     */
    @Override
    public Image applyTransformation(Image originalImage, int[][] structuringElement)
    {
        // 1) Erosão K vezes, onde k + 1 produz um conjunto vazio.
        // 2) Abertura de cada imagem resultante de K
        // 3) Resultado final é a união de (1 - 2) para todo K

        Image resultImage = new Image(originalImage.cloneImage());
        resultImage.clean();

        Image kImage = new Image(originalImage.cloneImage());
        Image k1Image = new Image(originalImage.cloneImage());
        Image openningImage = new Image(originalImage.cloneImage());

        openningImage = this.openningTransformOperation.applyTransformation(kImage, structuringElement);
        resultImage = this.subtractImage(kImage, openningImage);

        while (true)
        {
            k1Image = this.erosionTransformOperation.applyTransformation(k1Image, structuringElement);

            if (!k1Image.isEmptySet())
            {
                kImage = k1Image.cloneImage();
                openningImage = this.openningTransformOperation.applyTransformation(kImage, structuringElement);
                this.unionImage(resultImage, this.subtractImage(kImage, openningImage));
            }
            else
            {
                break;
            }
        }

        return resultImage;
    }

    private void unionImage(Image imageA, Image imageB)
    {
        for (int row = 0; row < imageA.getRows(); row++)
        {
            for (int col = 0; col < imageA.getCols(); col++)
            {
                if (imageB.getPixel(row, col) != 255)
                {
                    imageA.setPixel(row, col, 0);
                }
            }
        }
    }

    private Image subtractImage(Image imageA, Image imageB)
    {
        Image subImage = new Image(imageA.cloneImage());
        subImage.clean();

        for (int row = 0; row < imageA.getRows(); row++)
        {
            for (int col = 0; col < imageA.getCols(); col++)
            {
                if (imageA.getPixel(row, col) != 255 && imageB.getPixel(row, col) == 255)
                {
                    subImage.setPixel(row, col, 0);
                }
                else
                {
                    subImage.setPixel(row, col, 255);
                }
            }
        }

        return subImage;
    }

}
