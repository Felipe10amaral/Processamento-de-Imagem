import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import ij.process.LUT;

public class SepararCanaisLUT_ implements PlugIn  {
private ImagePlus imagem, canalVermelho, canalVerde, canalAzul;
	
	public void run(String arg) {
		this.imagem = IJ.getImage();
		
		separarCanais();
	}
	
	private void separarCanais(){
		
		// é um array que armazenará os valores dos canais de cor (R, G, B) de cada pixel.
		int pixel[] = {0,0,0};
		int x, y; 
		int largura = this.imagem.getWidth(); 
		int altura =this.imagem.getHeight();
		
		ImageProcessor[] processador = this.getProcessors(largura, altura);		
		
		for (x = 0; x < largura; x++) {
			for (y = 0; y < altura; y++) {
				
				// Obtém os valores de R, G, B do pixel atual da imagem original e armazena no array pixel.
				pixel = processador[3].getPixel(x, y, pixel);
				
				// Atribui os valores dos canais R, G, B do pixel aos ImageProcessors dos canais de vermelho, verde e azul, respectivamente
				processador[0].putPixel(x, y, pixel[0]);				
				processador[1].putPixel(x, y, pixel[1]);
				processador[2].putPixel(x, y, pixel[2]);				
			}
		}
		//Chama o método Lut para aplicar tabelas de pesquisa de cores (LUTs) aos canais separados.
		this.Lut();				
	}
	
	private ImageProcessor[] getProcessors(int largura, int altura) {
		
		// Serão utilizados para representar a imagem original e os canais de vermelho, verde e azul.
		ImageProcessor[] processors = new ImageProcessor[4];
		
		// Cria uma nova imagem para representar o canal vermelho e demais canais
		this.canalVermelho = IJ.createImage("Vermelho", "8-bit", largura, altura, 1);
		this.canalVerde = IJ.createImage("Verde", "8-bit", largura, altura, 1);
		this.canalAzul = IJ.createImage("Azul", "8-bit", largura, altura, 1);
		
		processors[0] = canalVermelho.getProcessor();
		processors[1] = canalVerde.getProcessor();
		processors[2] = canalAzul.getProcessor();
	    
		// Obtém o ImageProcessor associado à imagem original e o armazena no array.
		processors[3] = this.imagem.getProcessor();
		
		// Retorna o array de ImageProcessors, que agora contém os processadores para a imagem original 
		// e os demais canais 
		return processors;
	}
	
	private void Lut() {
		
		// exibir canais RGB
		this.canalVermelho.show();
		this.canalVerde.show();
		this.canalAzul.show();
		
		// cria uma LUT que mapeia os valores dos pixels para tons de vermelho e demais canais
		this.canalVermelho.setLut(LUT.createLutFromColor(Color.RED));
		this.canalVerde.setLut(LUT.createLutFromColor(Color.GREEN));
		this.canalAzul.setLut(LUT.createLutFromColor(Color.BLUE));			
	}

}

