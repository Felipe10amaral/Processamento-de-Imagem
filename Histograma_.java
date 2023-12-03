
import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class Histograma_ implements PlugIn {	
	private GenericDialog interfaceGrafica;
	private ImagePlus imagem;
	private ImageProcessor processador;
	private int alow, ahigh;
	private final String[] metodos = {"expansao","equalizacao"};

	
	public void run(String arg) {
		imagem = IJ.getImage();
		processador = imagem.getProcessor();		
		ajusteHistograma();			
		
	}
	
	public String interfaceGrafica(){		
		this.interfaceGrafica = new GenericDialog("Histograma");		
		interfaceGrafica.addRadioButtonGroup("Estrategias", metodos, 2, 1, metodos[0]);		
		interfaceGrafica.showDialog();
		if (interfaceGrafica.wasOKed()) {			
			return interfaceGrafica.getNextRadioButton();
		}
		else {
			return null;
		}
	}

	public void ajusteHistograma() {
		String metodo = interfaceGrafica();		
		switch (metodo){
			case "expansao":				
				expansao();
				break;
			case "equalizacao":				
				equalizacao();
				break;
			default:
				IJ.log("Plugin Cancelado");
				break;
		}
	}
	
	private void equalizacao() {		
		int largura = imagem.getWidth(), altura = imagem.getHeight(), valorPixel, novoPixel;		
		
		// calcular valores minimos e maximos
		setAHighALow();
		CalculoEqualizacao calcImgEqualizacao = new CalculoEqualizacao(largura, altura);		
		
		for (int x = 0; x < largura; x++) {
			for (int y = 0; y < altura; y++) {
				
				// valor do pixel na posição (x, y)
				valorPixel = processador.getPixel(x, y);
				
				// Chama o método addPixel na instância de CalculoEqualizacao para adicionar o valor do pixel ao histograma interno dessa classe.
				calcImgEqualizacao.addPixel(valorPixel);				
			}
		}		
		//equalização do histograma, utilizando o valor máximo (ahigh).
		calcImgEqualizacao.calculo(ahigh);
		
		for (int x = 0; x < largura; x++) {
			for (int y = 0; y < altura; y++) {
				valorPixel = processador.getPixel(x, y);
				if (calcImgEqualizacao.getSkRound(valorPixel) > 0) {
					novoPixel = calcImgEqualizacao.getSkRound(valorPixel);					
				}
				else novoPixel = valorPixel;	
				processador.putPixel(x, y, novoPixel);
			}
		}
		imagem.updateAndDraw();
		IJ.run(imagem, "Histogram", "");			
	} 
	
	private void expansao() {
		int largura = imagem.getWidth(), altura = imagem.getHeight(), valorPixel;
		int amin = 0; 
		int amax = 255;
		
		setAHighALow();
		
		for (int x = 0; x < largura; x++) {
			for (int y = 0; y < altura; y++) {
				
				valorPixel = processador.getPixel(x, y);
				// formula do calculo de expansao 
				valorPixel = validarPixel((int) (amin + (valorPixel - alow)*(amax-amin)/(ahigh-alow)));		
				
				// Atualiza o valor do pixel na imagem original com o novo valor calculado.
				processador.putPixel(x, y, valorPixel);
			}
		}
		imagem.updateAndDraw();
		IJ.run(imagem, "Histogram", "");	
	}	
	
	// calcular valores minimos e maximos
	private void setAHighALow() {
		// dimensoes da imagem
		int largura = imagem.getWidth(), altura = imagem.getHeight(),  valorPixel;
		
		// valores maximos e minimos
		alow = 255;
		ahigh = 0;
		
		// loop para atualizar valores minimos e maximos
		for (int x = 0; x < largura; x++) {
			for (int y = 0; y < altura; y++) {
				valorPixel = processador.getPixel(x, y);
				
				// valorPixel for maior que ahigh e estiver no intervalo 0, 255
				if (ahigh < valorPixel && valorPixel <= 255) {
					ahigh = valorPixel;
				}
				
				//valorPixel for menor que alow e estiver no intervalo 0, 255, atualiza alow com valorPixel.
				if (alow > valorPixel && valorPixel >= 0 ) {
					alow = valorPixel;
				}
			}								
		}		
	}
	
	
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