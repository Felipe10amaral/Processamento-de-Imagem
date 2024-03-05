
import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.gui.GenericDialog;

import ij.IJ;
import ij.process.ImageProcessor;

public class MorfologiaMatematica_ implements PlugIn{		
	private ImagePlus imagem;
	private ImageProcessor processadorCopia, processadorOriginal;
	private int[][] estrutura = {{1,1,1},{1,1,1},{1,1,1}};	
	
	public void run(String arg) {		
		imagem = IJ.getImage();		
		processadorOriginal = imagem.getProcessor();	
		convertBinario();
		processadorCopia = imagem.duplicate().getProcessor();				
		for (int x = 0; x < imagem.getWidth(); x++) {
			for (int y = 0; y < imagem.getHeight(); y++) {
				processadorCopia.putPixel(x, y, 255);
			}
		}
		imageChange();	
		}				
		

	public String interfaceGrafica(){
		GenericDialog interfaceGrafica = new GenericDialog("Técnicas de Morfologia");	
		String[] metodo = {"dilatacao","erosao","abertura","fechamento","borda"};		
		interfaceGrafica.addRadioButtonGroup("Métodos disponíveis", metodo, metodo.length, 1, metodo[0]);		
		interfaceGrafica.showDialog();		
		if (interfaceGrafica.wasOKed()) {			
			return interfaceGrafica.getNextRadioButton();
		}
		else return "exit";		
	}
	
	public void imageChange() {		
		String metodo = this.interfaceGrafica();		
		switch (metodo) {
		case "dilatacao":
			dilatacao();
			break;
		case "erosao":
			erosao();
			break;
		case "abertura":
			abertura();
			break;
		case "fechamento":
			fechamento();
			break;		
		case "borda":
			borda();
			break;
		default:
			IJ.log("Plugin cancelado");
			processadorCopia = processadorOriginal.duplicate();
			break;			
		}
		imagem.setProcessor(processadorCopia);
		imagem.updateAndDraw();
	}
	
	public void borda() {
		int pixelOriginal, pixelCopia;
		int y;
		erosao();
		for (int x = 0; x < imagem.getWidth(); x++) 
			for ( y = 0; y < imagem.getHeight(); y++) {
				pixelOriginal = processadorOriginal.getPixel(x,y);
				pixelCopia = processadorCopia.getPixel(x, y);
				if (pixelOriginal == 0 && pixelCopia == 255)
					processadorCopia.putPixel(x, y, 0);						
				else processadorCopia.putPixel(x, y, 255);	
			}				
	}
	
	public void abertura() {
		erosao();
		processadorOriginal = processadorCopia.duplicate();
		dilatacao();
	}
	
	public void fechamento() {
		dilatacao();		
		processadorOriginal = processadorCopia.duplicate();
		erosao();
	}
	
	public void dilatacao() {
		int largura = imagem.getWidth(), altura = imagem.getHeight();
		int i,j;
		for (int x = 1; x < largura-1; x++)
			for (int y = 1; y < altura-1; y++) 
				if (processadorOriginal.getPixel(x,y) == 0) 
					for ( i=0;i<3;i++)
						for ( j=0;j<3;j++) 
							if (estrutura[i][j] == 1)
								processadorCopia.putPixel(x-1+i, y-1+j, 0);				
	}
	
	public void erosao() {
		int countPreto, countUm = 0, largura = imagem.getWidth(), altura = imagem.getHeight();
		int i,j;
		for (int[] linha : estrutura)			
		    for (int value : linha)			    
		         if (value == 1)
		        	 countUm++;
		for (int x = 1; x < largura-1; x++)
			for (int y = 1; y < altura-1; y++) {
				if (processadorOriginal.getPixel(x,y) == 0) {
					countPreto = 0;
					for ( i=0;i<3;i++)
						for ( j=0;j<3;j++) {
							if (processadorOriginal.getPixel(x-1+i, y-1+j) == 0 && estrutura[i][j] == 1) {
								countPreto++;
							}
						}
					if (countPreto == countUm)
						processadorCopia.putPixel(x, y, 0);
				}
			}				
	}
	
	public void convertBinario() {		
		int pixel, countNaoBin=0, largura = imagem.getWidth(), altura = imagem.getHeight();		
		for (int x = 1; x < largura-1; x++)
			for (int y = 1; y < altura-1; y++) {
				pixel = processadorOriginal.getPixel(x, y);
				if (pixel != 0 &&  pixel != 255)
					countNaoBin++;
			}
		if (countNaoBin > 0)
			IJ.run(imagem, "Convert to Mask", "");
	}
	
	
}