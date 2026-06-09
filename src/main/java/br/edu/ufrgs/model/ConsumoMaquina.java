package br.edu.ufrgs.model;

public class ConsumoMaquina {
    
    //atributos privados
    private String maquinaID;
    private String nomeSetor;
    private double consumoKwh;
    private int horas;

    //construtor
    public ConsumoMaquina(String maquinaID, String nomeSetor, double consumoKwh, int horas) {
        this.maquinaID = maquinaID;
        this.nomeSetor = nomeSetor;
        this.consumoKwh = consumoKwh;
        this.horas = horas;
    }

    //getters
    public String getMaquinaID() {
        return maquinaID;
    }

    public String getNomeSetor() {
        return nomeSetor;
    }

    public double getConsumoKwh() {
        return consumoKwh;
    }

    public int getHoras() {
        return horas;
    }
}
