public class CalculoEqualizacao {
	private int[] nk, skRound;
	private int  linha, coluna;
	private double[] pr_rk, sk;
	
	
	public CalculoEqualizacao(int linha, int coluna) {		
		this.nk = new int[256];
		this.skRound = new int[256];
		this.pr_rk = new double[256];
		this.sk = new double[256];
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public void calculo(double amax) {
		double acumSk = 0d;
		
		// itera sobre todos os possíveis valores de intensidade de pixel
		for (int i=0; i<256; i++) {	
			
			// Se for zero, significa que esse valor de intensidade não está presente na imagem
			if (this.nk[i] > 0) {
				
				// calculo de pr_rk
				this.pr_rk[i] = (double) this.nk[i] / ((double) this.linha * (double) this.coluna);
				
				// atualiza os valores
				acumSk += this.pr_rk[i] * amax;
				
				// atualiza sk na posicao i
				this.sk[i] = acumSk;
				
				// atualiza sk arredondado
				this.skRound[i] = (int) this.sk[i];	
			}
		}		
	}
	
	// sk arredondado
	public int getSkRound(int index) {
		return this.skRound[index];
	}
	
	// nk
	public int getNk(int index) {
		return this.nk[index];
	}
	
	public void addPixel(int index) {
		this.nk[index] += 1;
	}
}