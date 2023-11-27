import java.awt.AWTEvent;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class OperacaoPontoaPonto_ implements PlugIn, DialogListener {
	private GenericDialog interfaceGrafica;
	private ImagePlus imagem, imagemCopia;
	private ImageProcessor processadorImagemOriginal, processadorCopiaImagem;
	private double brilho, contraste, solarizacao, saturacao;

	public void run(String arg) {
		imagemCopia = IJ.getImage().duplicate();
		imagemCopia.setTitle(IJ.getImage().getTitle());
		imagem = IJ.getImage();
		processadorImagemOriginal = imagem.getProcessor();
		processadorCopiaImagem = imagemCopia.getProcessor();
		if (imagemCopia.getType() != ImagePlus.COLOR_RGB) {
			IJ.error("Para executar este plugin, a imagem deve ser do tipo RGB");
		} else
			this.apresentarInterfaceGrafica();
	}

	// interface grafica com os elementos
	public void apresentarInterfaceGrafica() {
		brilho = 0;
		contraste = 0;
		solarizacao = 0;
		saturacao = 1;
		this.interfaceGrafica = new GenericDialog("Operações com a Imagem");
		this.interfaceGrafica.addDialogListener(this);
		interfaceGrafica.addSlider("Brilho", -255, 255, brilho, 1);
		interfaceGrafica.addSlider("Contraste", -255, 255, contraste, 1);
		interfaceGrafica.addSlider("Solarização", 0, 255, solarizacao, 1);
		interfaceGrafica.addSlider("Dessaturação", 0, 1, saturacao, 0.01);
		interfaceGrafica.showDialog();
		if (interfaceGrafica.wasOKed()) {
			IJ.log("Alterações salvas!!!");
		}
		if (interfaceGrafica.wasCanceled()) {
			IJ.getImage().close();
			this.imagem.show();
			IJ.showMessage("plugin cancelado");
		}
	}

	public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent e) {
		brilho = this.interfaceGrafica.getNextNumber();
		contraste = this.interfaceGrafica.getNextNumber();
		solarizacao = this.interfaceGrafica.getNextNumber();
		saturacao = this.interfaceGrafica.getNextNumber();
		OperacoesImagem();
		return true;
	}

	// utilizada para garantir que os valores dos pixels estejam dentro de um intervalo válido, 
	// que vai de 0 a 255
	private int validarPixel(int pixel) {
		if (pixel > 255) {
			return 255;
		} else if (pixel < 0) {
			return 0;
		} else
			return pixel;
	}

	private void OperacoesImagem() {
		int x, y, valorPixel[] = { 0, 0, 0 };
		double fator = (259 * (contraste + 255)) / (255 * (259 - contraste));
		for (x = 0; x < imagemCopia.getWidth(); x++) {
			for (y = 0; y < imagemCopia.getHeight(); y++) {
				valorPixel = processadorCopiaImagem.getPixel(x, y, valorPixel);
				for (int i = 0; i < 3; i++) {
					// ajuste de brilho
					if (brilho != 0) {
						valorPixel[i] = validarPixel(valorPixel[i] + (int) brilho);
					}
					// ajuste de contraste
					if (contraste != 0) {
						valorPixel[i] = validarPixel((int) (fator * ((valorPixel[i] - 128) + 128)));
					}
					// ajuste de solarização
					if (solarizacao != 0) {
						if (valorPixel[i] < solarizacao) {
							valorPixel[i] = 255 - valorPixel[i];
						}
					}
					//ajuste de  saturação
					double Y = 0.299 * valorPixel[0] + 0.587 * valorPixel[1] + 0.114 * valorPixel[2];
					valorPixel[i] = (int) (Y + saturacao * (valorPixel[i] - Y));
				}
				processadorImagemOriginal.putPixel(x, y, valorPixel);
			}
		}
		// atualizar a imagem na janela do ImageJ após realizar operações na imagem.
		imagem.updateAndDraw();
	}
}