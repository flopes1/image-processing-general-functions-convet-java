package com.poli.model.compress;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.poli.model.Image;

public class CompressImage
{

    private Image image;

    public CompressImage(Image image)
    {
        this.image = image.cloneImage();
    }

    public byte[] compressWithoutLoss()
    {
        byte[] image = new byte[this.image.getCols() * this.image.getRows()];

        int index = 0;
        for (int col = 0; col < this.image.getCols(); col++)
        {
            for (int row = 0; row < this.image.getRows(); row++)
            {
                int pixelValue = this.image.getPixel(row, col);
                image[index] = (byte) pixelValue;
                index++;
            }
        }

        ByteArrayOutputStream encodedImage = new ByteArrayOutputStream();

        int rows = this.image.getRows();
        int cols = this.image.getCols();

        try
        {
            ByteBuffer b = ByteBuffer.allocate(4);
            b.putInt(rows);
            encodedImage.write(b.array());
            b = ByteBuffer.allocate(4);
            b.putInt(cols);
            encodedImage.write(b.array());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        byte previous = image[0];

        int runLen = 1;

        for (int i = 1; i < image.length; i++)
        {
            byte actualByte = image[i];
            if (previous == actualByte)
            {
                runLen++;
            }
            else
            {
                encodedImage.write((byte) runLen);
                encodedImage.write((byte) previous);
                runLen = 1;
                previous = actualByte;
            }
        }
        encodedImage.write((byte) runLen);
        encodedImage.write((byte) previous);

        return encodedImage.toByteArray();
    }

    public Image compressWithLoss()
    {
        return this.compressWithLoss(4);
    }

    private Image compressWithLoss(int numberOfBits)
    {
        ColorModel colorModel = this.generateColorModel(numberOfBits);

        BufferedImage resultImage = new BufferedImage(this.image.getCols(), this.image.getRows(),
                BufferedImage.TYPE_BYTE_BINARY, (IndexColorModel) colorModel);
        Image result = new Image(resultImage);

        for (int col = 0; col < this.image.getCols(); col++)
        {
            for (int row = 0; row < this.image.getRows(); row++)
            {
                int pixelValue = this.image.getPixel(row, col);
                result.setPixel(row, col, pixelValue);
            }
        }

        return result;
    }

    /**
     * Metodo que cria a nova paleta de cores da imagem, gerando 2^numberOfBits níveis de cinza
     * 
     * @param numberOfBits
     * @return
     */
    private ColorModel generateColorModel(int numberOfBits)
    {
        int size = (int) Math.pow(2, numberOfBits);
        byte[] r = new byte[size];

        for (int i = 0; i < size; i++)
        {
            r[i] = (byte) (i * 17);
        }

        return new IndexColorModel(numberOfBits, size, r, r, r);
    }

    public Image descompressWithoutLoss(String path)
    {
        Image descompressedImage = null;
        Path fileLocation = Paths.get(path);
        try
        {
            byte[] data = Files.readAllBytes(fileLocation);

            int rows = this.getInteger(data, 0, 4);
            int cols = this.getInteger(data, 4, 8);

            BufferedImage result = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_GRAY);
            descompressedImage = new Image(result);
            int index = 8;

            for (int col = 0; col < cols; col++)
            {
                for (int row = 0; row < rows; row++)
                {
                    int qtd = data[index] & 0xff;
                    index++;
                    int value = data[index] & 0xff;
                    index++;
                    if (qtd > 1)
                    {
                        while (qtd > 0)
                        {
                            try
                            {
                                descompressedImage.setPixel(row++, col, value);
                                qtd--;
                            }
                            catch (Exception e)
                            {
                                row = 0;
                                col++;
                                System.out.println(e);
                            }
                        }
                        row--;
                    }
                    else
                    {
                        descompressedImage.setPixel(row, col, value);
                    }
                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        descompressedImage.convert2GrayScale();
        return descompressedImage;
    }

    private int getInteger(byte[] data, int i, int j)
    {
        byte[] b = new byte[j - i];
        int index = 0;
        while (i < j)
        {
            b[index] = data[i];
            index++;
            i++;
        }
        BigInteger integer = new BigInteger(b);

        return integer.intValue();
    }

}
