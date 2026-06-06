package br.edu.ufrgs.model;

public enum ClassificacaoAmbiental {
    
    ECO_FRIENDLY("Manter operação"),
    ALERTA("Revisar manutenção"),
    ALTO_IMPACTO("Plano de mitigação obrigatório");

    private final String recomendacao;

    // Construtor do Enum
    ClassificacaoAmbiental(String recomendacao) {
        this.recomendacao = recomendacao;
    }

    // Getter para a mensagem de recomendação
    public String getRecomendacao() {
        return recomendacao;
    }
}