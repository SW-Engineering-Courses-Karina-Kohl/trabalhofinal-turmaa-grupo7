package br.edu.ufrgs.model;

public class SetorConfiguracao {
    
    //atributos privados (encapsulamento)
    private String nomeSetor;
    private double fatorEmissao;
    private double limiteMensal;

    //construtor: cria o objeto com os dados lidos do CSV
    public SetorConfiguracao(String nomeSetor, double fatorEmissao, double limiteMensal) {
        this.nomeSetor = nomeSetor;
        this.fatorEmissao = fatorEmissao;
        this.limiteMensal = limiteMensal;
    }

    //getters: métodos públicos para leitura segura dos dados
    public String getNomeSetor() {
        return nomeSetor;
    }

    public double getFatorEmissao() {
        return fatorEmissao;
    }

    public double getLimiteMensal() {
        return limiteMensal;
    }
}
