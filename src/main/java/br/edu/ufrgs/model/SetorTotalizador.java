package br.edu.ufrgs.model;

public class SetorTotalizador {
    
    private String nomeSetor;
    private double consumoTotalKwh;
    private SetorConfiguracao configuracao; // Traz o fator de emissão e o limite mensal

    public SetorTotalizador(String nomeSetor, SetorConfiguracao configuracao) {
        this.nomeSetor = nomeSetor;
        this.configuracao = configuracao;
        this.consumoTotalKwh = 0.0; // Inicia zerado
    }

    // Método para ir somando o consumo de cada máquina lida no CSV
    public void adicionarConsumo(double kwh) {
        this.consumoTotalKwh += kwh;
    }

    public double getConsumoTotalKwh() {
        return this.consumoTotalKwh;
    }

    // RF03: Calcula a emissão total multiplicando o consumo acumulado pelo fator
    public double calcularEmissaoTotal() {
        return this.consumoTotalKwh * this.configuracao.getFatorEmissao();
    }

    // RF04: Define o status com base na porcentagem de uso do limite
    public ClassificacaoAmbiental getClassificacaoAmbiental() {
        // Regra de três básica para achar a porcentagem
        double percentualUso = (this.consumoTotalKwh / this.configuracao.getLimiteMensal()) * 100;

        if (percentualUso <= 70.0) {
            return ClassificacaoAmbiental.ECO_FRIENDLY;
        } else if (percentualUso <= 100.0) {
            return ClassificacaoAmbiental.ALERTA;
        } else {
            return ClassificacaoAmbiental.ALTO_IMPACTO;
        }
    }
}