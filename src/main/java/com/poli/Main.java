package com.poli;

import java.io.IOException;

public class Main
{

	public static void main(String[] args)
	{

		String imagePath = Main.class.getClassLoader().getResource("1.jpg").getPath();
		// String projectRootPath = System.getProperty("user.dir");
		// String destinyPath = imagePath.substring(0, imagePath.lastIndexOf('/'));
		String destinyPath = "D:/Dev/eclipse/workspace/image-processing/src/main/resources";

		try
		{
			// Nos filtros mediano e Kuwahara o parametro e o tamanho da mascara
			// EX: 3 : a mascara ira ter um tamanho 3x3
			// No filtro high boost 
			ImageProcessing imgProces = new ImageProcessing(imagePath);
			// imgProces.applyMedianFilter(3);
			// imgProces.applyHighBoostFilter(0.85);
			imgProces.applyKuwaharaFilter(9);
			imgProces.showNewImage();
			//imgProces.showOriginalImage();
			// imgProces.saveImage(destinyPath + "/1_.jpg");

		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}

	}

}
