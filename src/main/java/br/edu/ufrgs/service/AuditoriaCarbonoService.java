package br.edu.ufrgs.service;

import br.edu.ufrgs.model.ConsumoMaquina;
import br.edu.ufrgs.model.SetorConfiguracao;
import br.edu.ufrgs.model.SetorTotalizador;
import br.edu.ufrgs.util.ValidadorDados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditoriaCarbonoService {

    // Mapas para acesso rápido O(1)
    private Map<String, SetorConfiguracao> setoresConfig;
    private Map<String, SetorTotalizador> totalizadores;
    private ValidadorDados validador;

    public AuditoriaCarbonoService() {
        this.setoresConfig = new HashMap<>();
        this.totalizadores = new HashMap<>();
        this.validador = new ValidadorDados();
    }

    /**
     * RF01: Carrega as tabelas de referência e prepara os totalizadores.
     */
    public void carregarConfiguracoes(Map<String, SetorConfiguracao> configuracoes) {
        this.setoresConfig = configuracoes;
        this.totalizadores.clear();

        // Para cada setor configurado, já deixamos um Totalizador pronto
        for (Map.Entry<String, SetorConfiguracao> entry : configuracoes.entrySet()) {
            this.totalizadores.put(entry.getKey(), new SetorTotalizador(entry.getValue()));
        }
    }

    /**
     * RF02 e RF03: Processa os consumos individuais, validando se o setor existe.
     */
    public void processarConsumo(List<ConsumoMaquina> consumos) {
        for (ConsumoMaquina consumo : consumos) {
            String nomeSetor = consumo.getNomeSetor(); // Usando o getter da sua classe ConsumoMaquina

            // O validador checa se o setor está no mapa e já gera o log de erro se não estiver (RF02)
            if (validador.setorValido(nomeSetor, setoresConfig)) {
                SetorTotalizador totalizador = totalizadores.get(nomeSetor);
                // Acumula o kWh da máquina no total do setor correspondente (RF03)
                totalizador.adicionarConsumo(consumo.getConsumoKwh());
            }
        }
    }

    /**
     * RF03 e RF04: Executa a matemática final e gera a lista para exportação/exibição.
     */
    public List<SetorTotalizador> consolidarResultados() {
        List<SetorTotalizador> resultados = new ArrayList<>();

        for (SetorTotalizador totalizador : totalizadores.values()) {
            // Chama o metodo que aplica a fórmula e define se é ECO_FRIENDLY, ALERTA, etc.
            totalizador.calcularEmissao();
            resultados.add(totalizador);
        }

        return resultados;
    }

    public List<String> getLogsValidacao() {
        return validador.getLogsErro();
    }
}