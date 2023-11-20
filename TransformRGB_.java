import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class TransformRGB_ implements PlugIn {

	public void run(String arg) {

		ImagePlus imagem = IJ.getImage();
		
		// Obter o processador da imagem para manipulação
		ImageProcessor processador = imagem.getProcessor();
		
		int largura = processador.getWidth();
		int altura = processador.getHeight();

		// Criar processadores para os canais vermelho, verde e azul
		// ByteProcessor é para quando você precisa trabalhar com imagens em tons de cinza 
		ImageProcessor processadorVermelho = new ByteProcessor(largura, altura);
		ImageProcessor processadorVerde = new ByteProcessor(largura, altura);
		ImageProcessor processadorAzul = new ByteProcessor(largura, altura);

		// Iterar sobre cada pixel da imagem
		for (int x = 0; x < largura; x++) {
			for (int y = 0; y < altura; y++) {
				int[] rgb = new int[3];  // Cria um array para armazenar os valores dos canais RGB
				
				processador.getPixel(x, y, rgb); // pega os valores atuais da posicao x,y
				
				processadorVermelho.putPixel(x, y, rgb[0]); // Atribuir os valores dos canais aos ImageProcessors correspondentes
				processadorVerde.putPixel(x, y, rgb[1]);
				processadorAzul.putPixel(x, y, rgb[2]);
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

	}

}
