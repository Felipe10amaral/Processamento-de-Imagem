import ij.*;
import ij.plugin.PlugIn;
import ij.process.*;

public class GerarRGB_ implements PlugIn {

    public void run(String arg) {
        // Abrir imagens dos canais
        String vermelhoIMG = IJ.getFilePath("Entre com o canal vermelho");
        String verdeIMG = IJ.getFilePath("Entre com o canal verde");
        String azulIMG = IJ.getFilePath("Entre com o canal azul");

        ImagePlus canalVermelho = IJ.openImage(vermelhoIMG);
        ImagePlus canalVerde = IJ.openImage(verdeIMG);
        ImagePlus canalAzul = IJ.openImage(azulIMG);


        // Obter os ImageProcessors dos canais
        ImageProcessor processadorVermelho = canalVermelho.getProcessor();
        ImageProcessor processadorVerde = canalVerde.getProcessor();
        ImageProcessor processadorAzul = canalAzul.getProcessor();

       
        int largura = processadorVermelho.getWidth();
        int altura = processadorVermelho.getHeight();

        // Criar um novo ImageProcessor para a imagem final
        ColorProcessor processadorNovaImagemRGB = new ColorProcessor(largura, altura);

        // Preencher o ImageProcessor com os valores dos canais
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                int redValue = processadorVermelho.getPixel(x, y);
                int greenValue = processadorVerde.getPixel(x, y);
                int blueValue = processadorAzul.getPixel(x, y);

                int rgb = (redValue << 16) | (greenValue << 8) | blueValue;
                processadorNovaImagemRGB.putPixel(x, y, rgb);
            }
        }

        // Criar um novo ImagePlus para a imagem final
        ImagePlus imagemFinalRGB = new ImagePlus("Imagem Final", processadorNovaImagemRGB);

        // Exibir a imagem final
        imagemFinalRGB.show();
    }
}
