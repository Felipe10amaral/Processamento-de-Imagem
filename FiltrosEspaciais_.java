import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.gui.GenericDialog;

import ij.IJ;
import ij.process.ImageProcessor;

public class FiltrosEspaciais_ implements PlugIn{	
	private GenericDialog interfaceGrafica;
	private ImagePlus imagem;
	private ImageProcessor processadorCopia, processadorOriginal;	
	
	public void run(String arg) {		
		imagem = IJ.getImage();
		processadorCopia = imagem.getProcessor();
		processadorOriginal = imagem.duplicate().getProcessor();	
		if (imagem.getType() != ImagePlus.GRAY8) {
			IJ.error("a imagem deve ser do tipo GRAY8");
		}
		else this.aplicarFiltro();		
	}	

	public String mostrarJanela(){
		String[] metodo = {"passa-baixa","passa-alta","borda"};
		this.interfaceGrafica = new GenericDialog("Filtros Lineares");		
		interfaceGrafica.addRadioButtonGroup("Métodos", metodo, 3, 1, metodo[0]);		
		interfaceGrafica.showDialog();		
		if (interfaceGrafica.wasOKed()) {			
			return interfaceGrafica.getNextRadioButton();
		}
		else return null;		
	}
	
	public void aplicarFiltro() {
		float[][] passaBaixa = {{0.11f,0.11f,0.11f},{0.11f,0.11f,0.11f},{0.11f,0.11f,0.11f}};
		float[][] passaAlta = {{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
		float[][] borda = {{1,1,1},{1,-2,-1},{1,-1,-1}}; 
		String metodo = this.mostrarJanela();
		switch (metodo) {
		
		case "passa-baixa":  // Suavizam a imagem ( Reduz os detalhes da imagem )
			calcfiltro(passaBaixa);
			break;
		case "passa-alta":  // Realçam a imagem elimina os valores mais baixos de tonalidade, realçando os valores mais altos
			calcfiltro(passaAlta);
			break;
		case "borda":
			calcfiltro(borda); // Detecta características, como bordas, linhas, curvas e manchas;
			break;
		default:
			IJ.log("Plugin cancelado");
			break;			
		}
		imagem.updateAndDraw();
	}
	
	public void calcfiltro(float[][] kernel) {
		int largura = imagem.getWidth(), altura = imagem.getHeight();
		double soma;
		
		int[][] pixelOrigem = new int[3][3]; //matriz para armazenar os valores dos pixels vizinhos enquanto o filtro está sendo aplicado à imagem.
		
		for (int x=1; x<largura-1; x++) { // percorrer os pixels da imagem, evitando os pixels na borda (largura - 1)
			for (int y=1; y<altura-1; y++) {
				soma = 0;
				
				for (int i=0;i<3;i++) // iterar sobre os pixels vizinhos 
					for (int j=0;j<3;j++) {
						pixelOrigem[i][j] = processadorOriginal.getPixel(x-1+i, y-1+j); //calcular a convolução entre o kernel do filtro e a região vizinha do pixel atual na imagem
						soma += (double) (pixelOrigem[i][j] * kernel[i][j]); //valor do pixel na região vizinha multiplicado pelo valor correspondente no kernel do filtro
					}				
				
				processadorCopia.putPixel(x, y, validarPixel((int) soma));	//  atualizar o valor do pixel na posição (x, y) na imagem de saída 			
			}
		}		
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