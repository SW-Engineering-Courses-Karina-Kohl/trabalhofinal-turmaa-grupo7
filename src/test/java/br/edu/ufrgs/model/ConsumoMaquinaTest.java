package br.edu.ufrgs.model;

public class ConsumoMaquinaTest {package br.edu.ufrgs.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ConsumoMaquinaTest {

    @Test
    public void testCriacaoEGettersConsumoMaquina() {
        // cria o objeto passando os dados no construtor
        ConsumoMaquina maquina = new ConsumoMaquina("MAQ-001", "Usinagem", 1500.50, 120);

        // verifica se o objeto guardou os dados corretamente (Getters)
        assertEquals("MAQ-001", maquina.getMaqId(), "O ID da máquina deve ser MAQ-001");
        assertEquals("Usinagem", maquina.getSetor(), "O setor deve ser Usinagem");
        assertEquals(1500.50, maquina.getConsumoKwh(), "O consumo deve ser 1500.50");
        assertEquals(120, maquina.getHoras(), "As horas de uso devem ser 120");
    }
}
    
}


