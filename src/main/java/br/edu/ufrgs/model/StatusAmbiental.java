package br.edu.ufrgs.model;

/*
classificação do impacto ambiental de um setor, baseada no
percentual de consumo em relação ao limite mensal
*/

public enum StatusAmbiental {
    ECO_FRIENDLY,   // até 70%
    ALERTA,         // 71% a 100%
    ALTO_IMPACTO    // acima de 100%
    
}
