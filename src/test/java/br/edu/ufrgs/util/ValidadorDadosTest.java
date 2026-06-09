package br.edu.ufrgs.util;

import br.edu.ufrgs.model.SetorConfiguracao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;


class ValidadorDadosTest {
    
    private ValidadorDados validador;
    private Map<String, SetorConfiguracao> configMap;

    @BeforeEach
    void setUp(){
        validador = new ValidadorDados();
        configMap = new HashMap<>();
        configMap.put("Usinagem", new SetorConfiguracao("Usinagem", 0.092, 2000.0));
    }

    @Test
    void setorExistenteEhValido(){
        assertTrue(validador.setorValido("Usinagem", configMap));
        assertFalse(validador.possuiErros());
    } 

    @Test
    void setorInexistenteGeraLog(){
        assertFalse(validador.setorValido("Fundicao", configMap));
        assertTrue(validador.possuiErros());
        assertTrue(validador.getLogsErro().get(0).contains("não cadastrado"));
    }

    @Test
    void setorNuloGeraErro(){
        assertFalse(validador.setorValido(null, configMap));
        assertTrue(validador.possuiErros());
    }

    @Test
    void deveLimparLogs(){
        validador.adicionarLogErro("teste", "motivo");
        assertTrue(validador.possuiErros());
        validador.limparLogs();
        assertFalse(validador.possuiErros());
    }
}
