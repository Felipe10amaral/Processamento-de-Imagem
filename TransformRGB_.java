import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class TransformRGB_ implements PlugIn {
	
	public void run(String arg) {
		
		ImagePlus imagem = IJ.getImage();
		
	
		ImageProcessor processador = imagem.getProcessor();
        int width = processador.getWidth();
        int height = processador.getHeight();
        
        ImageProcessor processadorVermelho = new ByteProcessor(width, height);
        ImageProcessor processadorVerde = new ByteProcessor(width, height);
        ImageProcessor processadorAzul = new ByteProcessor(width, height);
        
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] rgb = processador.getPixel(x, y, null);
                processadorVermelho.putPixel(x, y, rgb[0]);
                processadorVerde.putPixel(x, y, rgb[1]);
                processadorAzul.putPixel(x, y, rgb[2]);
            }
        }

        ImagePlus canalVermelho = new ImagePlus("Red", processadorVermelho);
        ImagePlus canalVerde = new ImagePlus("Green", processadorVerde);
        ImagePlus canalAzul = new ImagePlus("Blue", processadorAzul);

        canalVermelho.show();
        canalVerde.show();
        canalAzul.show();

	    
	}
	

}
