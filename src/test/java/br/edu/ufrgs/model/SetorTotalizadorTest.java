package br.edu.ufrgs.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SetorTotalizadorTest {
    
    @Test
    void deveCalcularEmissaoEStatusEcoFriendly(){
        SetorConfiguracao cfg = new SetorConfiguracao("Usinagem", 0.092, 2000.0);
        SetorTotalizador total = new SetorTotalizador(cfg);
        total.adicionarConsumo(1200);
        total.adicionarConsumo(200.0);
        // total = 1400 -> exatamente 70%
        total.calcularEmissao();

        assertEquals(1400.0, total.getConsumoTotalKwh());
        assertEquals(1400 * 0.092, total.getEmissaoTotalCo2(), 0.001);
        assertEquals(StatusAmbiental.ECO_FRIENDLY, total.getStatus());
    }

    @Test
    void deveLancarExcecaoParaConsumoNegativo(){
        SetorConfiguracao cfg = new SetorConfiguracao("Log", 0.2, 500.0);
        SetorTotalizador total = new SetorTotalizador(cfg);
        assertThrows(IllegalArgumentException.class, () -> total.adicionarConsumo(-10.0));
    }

    @Test
    void statusAlertaQuandoAcima70Ate100(){
        SetorConfiguracao cfg = new SetorConfiguracao("Teste", 0.1, 100.0);
        SetorTotalizador total = new SetorTotalizador(cfg);
        total.adicionarConsumo(85.0);
        total.calcularEmissao();
        assertEquals(StatusAmbiental.ALERTA, total.getStatus());
    }

    @Test
    void statusAltoImpactQuandoAcima100(){
        SetorConfiguracao cfg = new SetorConfiguracao("Teste", 0.1, 100.0);
        SetorTotalizador total = new SetorTotalizador(cfg);
        total.adicionarConsumo(150.0);
        total.calcularEmissao();
        assertEquals(StatusAmbiental.ALTO_IMPACTO, total.getStatus());
    }

    @Test
    void statusAlertaQuandoLimiteMensaEhZero(){
        SetorConfiguracao cfg = new SetorConfiguracao("TesteZero", 0.1, 0.0);
        SetorTotalizador total = new SetorTotalizador(cfg);
        total.adicionarConsumo(500.0);
        total.calcularEmissao();
        assertEquals(StatusAmbiental.ALERTA, total.getStatus());
    }
}
