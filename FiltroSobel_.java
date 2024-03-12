import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.IJ;
import ij.process.ImageProcessor;

public class FiltroSobel_ implements PlugIn{	
	private ImagePlus imagem;
	private ImageProcessor processadorVertical, processadorHorizontal, processadorOriginal, processadorAux;	
	
	public void run(String arg) {		
		imagem = IJ.getImage();
		processadorOriginal = imagem.getProcessor();
		processadorVertical = imagem.duplicate().getProcessor();
		processadorHorizontal = imagem.duplicate().getProcessor();
		processadorAux = imagem.duplicate().getProcessor();
		if (imagem.getType() != ImagePlus.GRAY8) {
			IJ.error("a imagem deve ser do tipo GRAY8");
		}
		else this.calcFiltro();	
	}	
	
	public void calcFiltro() {
		int[][] kernelVertical = {{-1,0,1},{-2,0,2},{-1,0,1}}; //kernel Sobel para detecção de bordas verticais
		int[][] kernelHorizontal = {{1,2,1},{0,0,0},{-1,-2,-1}}; // kernel Sobel para detecção de bordas horizontais
		int largura = imagem.getWidth();
		int altura = imagem.getHeight();
		ImagePlus vertical = IJ.createImage("Vertical", "8-bit", largura, altura, 1);
		ImagePlus horizontal = IJ.createImage("Horizontal", "8-bit", largura, altura, 1); // nova imagem para armazenar as bordas horizontais detectadas pelo filtro de Sobel.
		double somaVertical, somaHorizontal;
		int[][] pixelOrigem = new int[3][3]; // armazenar pixels vizinhos
		
		for (int x = 1; x < largura-1; x++) { // percorrer as colunas da imagem exceto as bordas.
			for (int y = 1; y < altura-1; y++) {
				somaHorizontal = 0; 
				somaVertical = 0;
				
				for (int i=0;i<3;i++)
					for (int j=0;j<3;j++) {
						pixelOrigem[i][j] = processadorOriginal.getPixel(x-1+i, y-1+j);
						somaVertical += (double) (pixelOrigem[i][j] * kernelVertical[i][j]); // Para cada pixel na vizinhança calcula a soma ponderada com os valores do kernel vertical e horizontal.
						somaHorizontal += (double) (pixelOrigem[i][j] * kernelHorizontal[i][j]);
					}				
				processadorVertical.putPixel(x, y, validarPixel((int) somaVertical));
				processadorHorizontal.putPixel(x, y, validarPixel((int) somaHorizontal));
				processadorAux.putPixel(x, y, validarPixel((int) Math.sqrt(Math.pow(somaVertical, 2) + Math.pow(somaHorizontal, 2))));
			}
		}
		
		// Define os processadores das imagens correspondentes.
		imagem.setProcessor(processadorAux);
		vertical.setProcessor(processadorVertical);
		horizontal.setProcessor(processadorHorizontal);
		
		imagem.setTitle("Vertical + Horizontal");
		imagem.updateAndDraw();
		
		// Mostra as imagens 
		vertical.show();
		horizontal.show();		
	}
	
	//Este método garante que os valores dos pixels estejam dentro da faixa válida (0 a 255)
		private int validarPixel(int pixel) {
			if (pixel > 255) {
				return 255;
			}
			else if (pixel < 0) {
				return 0;
			}
			else return pixel;
		}
}
	