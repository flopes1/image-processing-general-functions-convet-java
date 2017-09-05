package com.poli;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.poli.model.EnumFilterType;
import com.poli.model.Mask;

public class ImageProcessing
{

    private String imagePath;
    private BufferedImage image;
    private Mask mask;

    public ImageProcessing(String imagePath) throws IOException
    {
        this.setImagePath(imagePath);
        this.loadImage(this.getImagePath());
    }

    public void convert2GrayScale()
    {
        BufferedImage grayImage = new BufferedImage(this.getImage().getWidth(), this.getImage().getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(this.getImage(), grayImage);
        this.setImage(grayImage);
    }

    /**
     * Aplica o filtro mediano com mascara definido pelo parametro passado
     * 
     * @param maskRate
     *            taxa que define o grau da mascara, deve ser impar
     */
    public void applyMedianFilter(int maskRate)
    {

        if (maskRate < 0 || (maskRate % 2) == 0)
        {
            throw new IllegalArgumentException("O parametro deve ser maior que zero e impar");
        }

        this.mask = new Mask(maskRate, EnumFilterType.MEDIAN);
        this.applyImageFilter();

    }

    private void applyImageFilter()
    {
        for (int row = 0; row < this.image.getHeight(); row++)
        {
            for (int col = 0; col < this.image.getWidth(); col++)
            {
                if (this.getMask().isValidRegion(row, col, this.image.getHeight(), this.image.getWidth()))
                {
                    int rateMid = this.mask.getRate() / 2;

                    int result = this.getMask().calculateMaskResult(this.getImage().getSubimage(col - rateMid,
                            row - rateMid, this.getMask().getRate(), this.getMask().getRate()));
                    Color color = new Color(result, result, result);
                    this.getImage().setRGB(col, row, color.getRGB());
                }

            }
        }
    }

    public void showImage()
    {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(this.getImage())));
        frame.pack();
        frame.setVisible(true);

    }

    public void saveImage(String path) throws IOException
    {
        // String fullPath = path.substring(0, path.lastIndexOf('\\') + 1) + "\\";
        String extension = path.substring(path.lastIndexOf('.') + 1);
        // String name = path.substring(path.lastIndexOf('\\') + 1, path.lastIndexOf('.'));

        File outputfile = new File(path);
        ImageIO.write(this.getImage(), extension, outputfile);
    }

    private void loadImage(String imagePath) throws IOException
    {
        File imageFile = new File(imagePath);

        if (!imageFile.exists())
        {
            throw new IllegalArgumentException("The image does not exists");
        }

        BufferedImage image = ImageIO.read(imageFile);
        this.setImage(image);
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public Mask getMask()
    {
        return this.mask;
    }

    public void setMask(Mask mask)
    {
        this.mask = mask;
    }

}
