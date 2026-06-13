package br.edu.ufrgs.service;

import br.edu.ufrgs.model.ConsumoMaquina;
import br.edu.ufrgs.model.SetorConfiguracao;
import br.edu.ufrgs.model.SetorTotalizador;
import br.edu.ufrgs.model.StatusAmbiental;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuditoriaCarbonoServiceTest {

    private AuditoriaCarbonoService service;
    private Map<String, SetorConfiguracao> configuracoesMock;

    @BeforeEach
    void setUp() {
        // Inicializa o service novo para cada teste, garantindo isolamento
        service = new AuditoriaCarbonoService();
        configuracoesMock = new HashMap<>();

        // Criando configurações base para os testes
        configuracoesMock.put("Usinagem", new SetorConfiguracao("Usinagem", 0.5, 1000.0));
        configuracoesMock.put("Logistica", new SetorConfiguracao("Logistica", 0.8, 500.0));
    }

    @Test
    void testCarregarConfiguracoes() {
        // Execução
        service.carregarConfiguracoes(configuracoesMock);
        List<SetorTotalizador> resultados = service.consolidarResultados();

        // Verificação
        assertEquals(2, resultados.size(), "Deve ter criado um totalizador para cada setor configurado");
    }

    @Test
    void testProcessarConsumoComSetorValido() {
        // Preparação
        service.carregarConfiguracoes(configuracoesMock);

        List<ConsumoMaquina> consumos = new ArrayList<>();
        consumos.add(new ConsumoMaquina("M1", "Usinagem", 400.0, 10));
        consumos.add(new ConsumoMaquina("M2", "Usinagem", 200.0, 5));

        // Execução
        service.processarConsumo(consumos);
        List<SetorTotalizador> resultados = service.consolidarResultados();

        // Verificação
        SetorTotalizador usinagem = resultados.stream()
                .filter(r -> r.getConfig().getNomeSetor().equals("Usinagem"))
                .findFirst()
                .orElse(null);

        assertNotNull(usinagem, "O totalizador de usinagem deve existir");
        assertEquals(600.0, usinagem.getConsumoTotalKwh(), "O consumo total deve ser a soma das duas máquinas (400 + 200)");

        // Emissão = 600 * 0.5 (fator) = 300
        assertEquals(300.0, usinagem.getEmissaoTotalCo2(), "A emissão deve ser calculada corretamente");

        // 600 de consumo num limite de 1000 = 60% (Deve ser ECO_FRIENDLY, pois é <= 70%)
        assertEquals(StatusAmbiental.ECO_FRIENDLY, usinagem.getStatus(), "O status ambiental deve ser ECO_FRIENDLY");

        // Não deve haver logs de erro
        assertTrue(service.getLogsValidacao().isEmpty(), "A lista de erros deve estar vazia para setores válidos");
    }

    @Test
    void testProcessarConsumoComSetorInvalido() {
        // Preparação
        service.carregarConfiguracoes(configuracoesMock);

        List<ConsumoMaquina> consumos = new ArrayList<>();
        // Setor "RH" não existe nas configurações mockadas
        consumos.add(new ConsumoMaquina("M3", "RH", 150.0, 8));

        // Execução
        service.processarConsumo(consumos);

        // Verificação
        List<String> erros = service.getLogsValidacao();

        assertFalse(erros.isEmpty(), "Deve gerar um log de erro para setor não encontrado");
        assertTrue(erros.get(0).contains("RH"), "A mensagem de erro deve mencionar o setor RH");
    }

    @Test
    void testConsolidarResultadosGeraStatusAltoImpacto() {
        // Preparação
        service.carregarConfiguracoes(configuracoesMock);

        List<ConsumoMaquina> consumos = new ArrayList<>();
        // Consumo de 600 num limite de 500 para Logística (120% do limite)
        consumos.add(new ConsumoMaquina("M4", "Logistica", 600.0, 20));

        // Execução
        service.processarConsumo(consumos);
        List<SetorTotalizador> resultados = service.consolidarResultados();

        // Verificação
        SetorTotalizador logistica = resultados.stream()
                .filter(r -> r.getConfig().getNomeSetor().equals("Logistica"))
                .findFirst()
                .orElse(null);

        assertNotNull(logistica);
        assertEquals(StatusAmbiental.ALTO_IMPACTO, logistica.getStatus(), "Consumo acima do limite deve gerar status ALTO_IMPACTO");
    }
}