package br.edu.ufrgs.model;

/*
processa o consumo de um setor, calcula a emissão de CO2 e define o status ambiental 
conforme os limites configurados
*/

public class SetorTotalizador {
    private final SetorConfiguracao config;
    private double consumoTotalKwh;
    private double emissaoTotalCo2;
    private StatusAmbiental status;

    public SetorTotalizador(SetorConfiguracao config){
        if(config == null){
            throw new IllegalArgumentException("Configuração do setor não pode ser nula");
        }
        this.config = config;
        this.consumoTotalKwh = 0.0;
        this.emissaoTotalCo2 = 0.0;
        this.status = null;
    }

    public void adicionarConsumo(double consumoKwh){
        if(consumoKwh < 0){
            throw new IllegalArgumentException("Consumo não pode ser negativo: " + consumoKwh);
        }
        this.consumoTotalKwh += consumoKwh;
    }

    public void calcularEmissao(){
        this.emissaoTotalCo2 = this.consumoTotalKwh * config.getFatorEmissao();
        this.status = determinarStatus();
    }

    private StatusAmbiental determinarStatus(){
        double limite = config.getLimiteMensal();
        if(limite <= 0){
            return StatusAmbiental.ALERTA;
        }
        double percentual = (consumoTotalKwh / limite) * 100.0;
        if(percentual <= 70.0){
            return StatusAmbiental.ECO_FRIENDLY;
        } else if(percentual <= 100){
            return StatusAmbiental.ALERTA;
        } else{
            return StatusAmbiental.ALTO_IMPACTO;
        }
    }

    public SetorConfiguracao getConfig(){
        return config;
    }

    public double getConsumoTotalKwh(){
        return consumoTotalKwh;
    }

    public double getEmissaoTotalCo2(){
        return emissaoTotalCo2;
    }

    public StatusAmbiental getStatus(){
        return status;
    }
}
