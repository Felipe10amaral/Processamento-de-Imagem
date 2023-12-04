import java.util.ArrayList;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class EscalaDeCinza_ implements PlugIn{
	private GenericDialog interfaceGrafica;
	private ImagePlus imagem, imagemCopia;
	private ImageProcessor processadorImagemOriginal, processadorCopiaImagem;
	
	private final String[] metodos = {"media" ,"luminancia analogica", "luminancia digital"};
	
	List<Integer> redPixels = new ArrayList<>();
    List<Integer> greenPixels = new ArrayList<>();
    List<Integer> bluePixels = new ArrayList<>();
	
	public void run(String arg) {
		imagem = IJ.getImage();
		imagemCopia = IJ.getImage().duplicate();
		processadorImagemOriginal = imagem.getProcessor();
		processadorCopiaImagem = imagemCopia.getProcessor();
		
		operacoes();
		

	}
	
	public String interfaceGrafica(){
		
		this.interfaceGrafica = new GenericDialog("Escala de Cinza");		
		interfaceGrafica.addRadioButtonGroup("Estrategias", metodos, 3, 1, metodos[0]);		
		interfaceGrafica.showDialog();
		if (interfaceGrafica.wasOKed()) {			
			return interfaceGrafica.getNextRadioButton();
		}
		else {
			return null;
		}
	}
	
	public void operacoes() {
		String metodo = interfaceGrafica();		
		switch (metodo){
			case "luminancia analogica":				
				luminanciaAnalogica();
				break;
			case "luminancia digital":				
				luminanciaDigital();
				break;	
			case "media":				
				media();
				break;
			default:
				IJ.log("Plugin Cancelado");
				break;
		}
	}
	
	public void luminanciaDigital() {
		Double wR = 0.2125;
        Double wG = 0.7154;
        Double wB = 0.072;
		
		 // Obter dimensões da imagem
	    int largura = processadorImagemOriginal.getWidth();
	    int altura = processadorImagemOriginal.getHeight();

	    //ImageProcessor luminAnalogProcessor = imagemCopia.getProcessor();

	    // Iterar através de cada pixel e aplicar a fórmula da luminância analógica
	    for (int i = 0; i < largura; i++) {
	        for (int j = 0; j < altura; j++) {
	            // Obter os valores dos canais de cor do pixel atual
	            int[] rgb = processadorCopiaImagem.getPixel(i, j, new int[3]);

	            // Calcular a luminância analógica usando os pesos
	            int pixel = (int) ((wR * rgb[0]) + (wG * rgb[1]) + (wB * rgb[2]));

	            // Normalizar o valor de intensidade para o intervalo de 0 a 255
	            pixel = Math.max(0, Math.min(255, pixel));

	            // Armazenar o resultado na imagem de luminância analógica
	            processadorCopiaImagem.putPixel(i, j, pixel);
	        }
	    }

	    // Exibir a imagem de luminância analógica
	    imagemCopia.show();
	}
	
	public void luminanciaAnalogica() {
	    // Definir os pesos
	    Double wR = 0.299;
	    Double wG = 0.587;
	    Double wB = 0.114;

	    // Obter dimensões da imagem
	    int largura = processadorImagemOriginal.getWidth();
	    int altura = processadorImagemOriginal.getHeight();

	    //ImageProcessor luminAnalogProcessor = imagemCopia.getProcessor();

	    // Iterar através de cada pixel e aplicar a fórmula da luminância analógica
	    for (int i = 0; i < largura; i++) {
	        for (int j = 0; j < altura; j++) {
	            // Obter os valores dos canais de cor do pixel atual
	            int[] rgb = processadorCopiaImagem.getPixel(i, j, new int[3]);

	            // Calcular a luminância analógica usando os pesos
	            int pixel = (int) ((wR * rgb[0]) + (wG * rgb[1]) + (wB * rgb[2]));

	            // Normalizar o valor de intensidade para o intervalo de 0 a 255
	            pixel = Math.max(0, Math.min(255, pixel));

	            // Armazenar o resultado na imagem de luminância analógica
	            processadorCopiaImagem.putPixel(i, j, pixel);
	        }
	    }

	    // Exibir a imagem de luminância analógica
	    imagemCopia.show();
	}
	
	public void media() {
        // Obter dimensões da imagem
        int largura = processadorImagemOriginal.getWidth();
        int altura = processadorImagemOriginal.getHeight();

        // Criar uma cópia da imagem original
        imagemCopia = imagem.duplicate();
        processadorCopiaImagem = imagemCopia.getProcessor();

        // Iterar através de cada pixel e aplicar a fórmula da média
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                int[] rgb = processadorImagemOriginal.getPixel(x, y, new int[3]);
                
                // formula da media
                int cinza = (rgb[0] + rgb[1] + rgb[2]) / 3;
                
                // atribui os valores de intensidade de cinza calculados de volta à imagem
                processadorCopiaImagem.putPixel(x, y, new int[]{cinza, cinza, cinza});
            }
        }

        // Exibir a imagem em tons de cinza
        imagemCopia.show();
    }
	
	
}
