import java.awt.Color;

import ij.*;
import ij.plugin.PlugIn;
import ij.process.*;

public class GerarRGB_ implements PlugIn {

	public void run(String arg) {
		// Abrir imagens dos canais ( método estático que pega o caminho da imagem
		// atraves de uma caixa de dialogo)
		String vermelhoIMG = IJ.getFilePath("Entre com o canal vermelho");
		String verdeIMG = IJ.getFilePath("Entre com o canal verde");
		String azulIMG = IJ.getFilePath("Entre com o canal azul");

		// Abre uma imagem e retorna como um objeto ImagePlus.
		ImagePlus canalVermelho = IJ.openImage(vermelhoIMG);
		ImagePlus canalVerde = IJ.openImage(verdeIMG);
		ImagePlus canalAzul = IJ.openImage(azulIMG);

		// Obter os ImageProcessors dos canais pegando a imagem ou a fatia da imagem com o getProcessor
		// Os processadores de imagem fornecem métodos para manipular os pixels da imagem.
		ImageProcessor processadorVermelho = canalVermelho.getProcessor();
		ImageProcessor processadorVerde = canalVerde.getProcessor();
		ImageProcessor processadorAzul = canalAzul.getProcessor();

		// A largura e altura da imagem são obtidas do canal vermelho (poderia ser de qualquer outro canal).
		// Não é necessário pegar de todos os canais, porque elas terão o mesmo tamanho
		int largura = processadorVermelho.getWidth();
		int altura = processadorVermelho.getHeight();

		// Criar um novo ImageProcessor para a imagem final com as dimensões que a nova
		// imagem terá
		ColorProcessor processadorNovaImagemRGB = new ColorProcessor(largura, altura);

		// Preencher o ImageProcessor com os valores dos canais
		for (int x = 0; x < largura; x++) {
			for (int y = 0; y < altura; y++) {
				int valorVerde = processadorVerde.getPixel(x, y);
				int valorVermelho = processadorVermelho.getPixel(x, y);
				int valorAzul = processadorAzul.getPixel(x, y);

				// classe nativa do Java para trabalhar com cor
				Color cor = new Color(valorVermelho, valorVerde, valorAzul);

				// retorna o valor RGB inteiro
				int rgb = cor.getRGB();

				// atualiza os valores do pixel da nova imagem
				processadorNovaImagemRGB.putPixel(x, y, rgb);
				
			}
		}

		// Criar um novo ImagePlus para a imagem final
		ImagePlus imagemFinalRGB = new ImagePlus("Imagem Final", processadorNovaImagemRGB);

		// Exibir a imagem final
		imagemFinalRGB.show();
	}
}
