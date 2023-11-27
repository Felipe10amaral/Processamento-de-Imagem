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
		if (this.imagem.getType() != ImagePlus.COLOR_RGB) {
			IJ.error("É necessário uma imagem do tipo RGB");
		}
		else {
			separarCanais();
		}	
	}
	
	private void separarCanais(){		
		int x, y, pixel[] = {0,0,0};
		int largura = this.imagem.getWidth(); 
		int altura =this.imagem.getHeight();
		
		ImageProcessor[] processador = this.getProcessors(largura, altura);		
		
		for (x = 0; x < largura; x++) {
			for (y = 0; y < altura; y++) {
				pixel = processador[3].getPixel(x, y, pixel);				
				processador[0].putPixel(x, y, pixel[0]);				
				processador[1].putPixel(x, y, pixel[1]);
				processador[2].putPixel(x, y, pixel[2]);				
			}
		}
		this.Lut();				
	}
	
	private ImageProcessor[] getProcessors(int largura, int altura) {
		ImageProcessor[] processors = new ImageProcessor[4];
		
		this.canalVermelho = IJ.createImage("RED", "8-bit", largura, altura, 1);
		this.canalVerde = IJ.createImage("GREEN", "8-bit", largura, altura, 1);
		this.canalAzul = IJ.createImage("BLUE", "8-bit", largura, altura, 1);
		
		processors[0] = canalVermelho.getProcessor();
		processors[1] = canalVerde.getProcessor();
		processors[2] = canalAzul.getProcessor();
		processors[3] = this.imagem.getProcessor();
		
		return processors;
	}
	
	private void Lut() {
		this.imagem.close();		
		this.canalVermelho.show();
		this.canalVerde.show();
		this.canalAzul.show();
		this.canalVermelho.setLut(LUT.createLutFromColor(Color.RED));
		this.canalVerde.setLut(LUT.createLutFromColor(Color.GREEN));
		this.canalAzul.setLut(LUT.createLutFromColor(Color.BLUE));			
	}

}
