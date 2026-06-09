package br.edu.ufrgs.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SetorConfiguracaoTest {

    @Test
    public void testCriacaoEGettersSetorConfiguracao() {
        // cria a configuração do setor
        SetorConfiguracao config = new SetorConfiguracao("Montagem", 0.045, 300.0);

        // verifica se os valores foram salvos corretamente
        assertEquals("Montagem", config.getNome(), "O nome do setor deve ser Montagem");
        assertEquals(0.045, config.getFatorEmissao(), "O fator de emissão deve ser 0.045");
        assertEquals(300.0, config.getLimiteMensal(), "O limite mensal deve ser 300.0");
    }
}
