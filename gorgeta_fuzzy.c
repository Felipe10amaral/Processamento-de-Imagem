#include <stdio.h>

// Função de pertinência triangular
float triangular(float x, float a, float b, float c) {
    if (x <= a || x >= c) return 0;
    if (x >= a && x <= b) return (x - a) / (b - a);
    if (x >= b && x <= c) return (c - x) / (c - b);
    return 0;
}

// Função de pertinência trapezoidal
float trapezoidal(float x, float a, float b, float c, float d) {
    if (x <= a || x >= d) return 0;
    if (x >= b && x <= c) return 1;
    if (x > a && x < b) return (x - a) / (b - a);
    if (x > c && x < d) return (d - x) / (d - c);
    return 0;
}

int main() {
    // Entradas
    float servico, localizacao, qualidade_comida, preco, estrutura;

    // Saída
    float gorjeta;

    // Variável de controle do loop
    char continuar;

    do {
        // Entrada de dados
        printf("\nInforme a avaliacao do servico (0 a 10): ");
        scanf("%f", &servico);

        printf("Informe a localizacao (0 a 20 km): ");
        scanf("%f", &localizacao);

        printf("Informe a qualidade da comida (0 a 10): ");
        scanf("%f", &qualidade_comida);

        printf("Informe o preco (0 a 1000 reais): ");
        scanf("%f", &preco);

        printf("Informe a avaliacao da estrutura (0 a 10): ");
        scanf("%f", &estrutura);

        // Lógica Fuzzy
        float servico_med = triangular(servico, 3, 5, 7);
        float servico_bom = trapezoidal(servico, 6, 8, 10, 10);
        float servico_excelente = triangular(servico, 8, 9, 10);

        // Regras Fuzzy
        float regra1 = servico_med * 0.05; // Gorjeta pequena para serviço medíocre
        float regra2 = servico_bom * 0.10; // Gorjeta média para serviço bom
        float regra3 = servico_excelente * 0.20; // Gorjeta generosa para serviço excelente
        float regra4 = (preco <= 1000 && localizacao <= 20) ? 0.20 : 0; // Gorjeta generosa se preço for baixo e localização for perto
        float regra5 = (preco <= 1000 && estrutura >= 8) ? 0.20 : 0; // Gorjeta generosa se preço for baixo e estrutura for excelente

        // Defuzzificação - Soma dos Centros de Área (Média Ponderada)
        gorjeta = (regra1 + regra2 + regra3 + regra4 + regra5) / (servico_med + servico_bom + servico_excelente + (regra4 > 0 ? 1 : 0) + (regra5 > 0 ? 1 : 0));

        // Exibição do valor da gorjeta
        printf("\nValor da gorjeta: ");
        if (gorjeta <= 0.05)
            printf("Pequena (5%%)\n");
        else if (gorjeta <= 0.15)
            printf("Media (10%%)\n");
        else
            printf("Generosa (20%% ou mais)\n");

        // Verificar se o usuário deseja continuar
        printf("\nDeseja calcular outra gorjeta? (S/N): ");
        scanf(" %c", &continuar);
    } while (continuar == 'S' || continuar == 's');

    printf("\nPrograma encerrado.\n");

    return 0;
}

