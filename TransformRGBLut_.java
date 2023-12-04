import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.LUT;

public class TransformRGBLut_ implements PlugIn {

	public void run(String arg) {

		ImagePlus imagem = IJ.getImage();

		// Obter o processador da imagem para manipulação
		ImageProcessor processador = imagem.getProcessor();

		int largura = processador.getWidth();
		int altura = processador.getHeight();

		// Criar LUTs para vermelho, verde e azul
		byte[] redLUT = new byte[256];
		byte[] greenLUT = new byte[256];
		byte[] blueLUT = new byte[256];

		for (int i = 0; i < 256; i++) {
			redLUT[i] = (byte) i;
			greenLUT[i] = 0;
			blueLUT[i] = 0;
		}

		LUT lutVermelha = new LUT(redLUT, greenLUT, blueLUT);

		for (int i = 0; i < 256; i++) {
			redLUT[i] = 0;
			greenLUT[i] = (byte) i;
			blueLUT[i] = 0;
		}

		LUT lutVerde = new LUT(redLUT, greenLUT, blueLUT);

		for (int i = 0; i < 256; i++) {
			redLUT[i] = 0;
			greenLUT[i] = 0;
			blueLUT[i] = (byte) i;
		}

		LUT lutAzul = new LUT(redLUT, greenLUT, blueLUT);

		// Criar ImageProcessor para os canais vermelho, verde e azul
		ImageProcessor processadorVermelho = new ByteProcessor(largura, altura);
		ImageProcessor processadorVerde = new ByteProcessor(largura, altura);
		ImageProcessor processadorAzul = new ByteProcessor(largura, altura);

		// Iterar sobre cada pixel da imagem
		for (int x = 0; x < largura; x++) {
			for (int y = 0; y < altura; y++) {
				int[] rgb = new int[3];

				processador.getPixel(x, y, rgb);

				// Aplicar LUTs aos canais
				processadorVermelho.putPixel(x, y, lutVermelha.getRed(rgb[0] & 0xFF));
				processadorVerde.putPixel(x, y, lutVerde.getGreen(rgb[1] & 0xFF));
				processadorAzul.putPixel(x, y, lutAzul.getBlue(rgb[2] & 0xFF));
			}
		}

		// Criar ImagePlus para os canais vermelho, verde e azul
		ImagePlus canalVermelho = new ImagePlus("Vermelho", processadorVermelho);
		ImagePlus canalVerde = new ImagePlus("Verde", processadorVerde);
		ImagePlus canalAzul = new ImagePlus("Azul", processadorAzul);

		// Exibir os canais
		canalVermelho.show();
		canalVerde.show();
		canalAzul.show();
		
		// cria uma LUT que mapeia os valores dos pixels para tons de vermelho e demais canais
		canalVermelho.setLut(LUT.createLutFromColor(Color.RED));
		canalVerde.setLut(LUT.createLutFromColor(Color.GREEN));
		canalAzul.setLut(LUT.createLutFromColor(Color.BLUE));	
	}
}
