package br.edu.ufrgs.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import br.edu.ufrgs.model.ConsumoMaquina;
import br.edu.ufrgs.model.SetorConfiguracao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LeitorCSVTest {
    
    private Path tempConfig;
    private Path tempMaquinas;
    private LeitorCSV leitor;
    
    @BeforeEach
    void setUp() throws IOException {
        leitor = new LeitorCSV();

        tempConfig = Files.createTempFile("config", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempConfig.toFile()))) {
            writer.write("nome_setor,fator_emissao,limite_mensal\n");
            writer.write("Usinagem,0.85,10000.00\n");
            writer.write("Montagem,0.90,8000.00\n");
            writer.write("Pintura,0.75,6000.00\n");
        }

        tempMaquinas = Files.createTempFile("consumo_maquinas", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempMaquinas.toFile()))) {
            writer.write("maquina_id,setor,consumo_kwh,horas_ativas\n");
            writer.write("M_01,Usinagem,1200.50,160\n");
            writer.write("M_02,Usinagem,1150.75,158\n");
            writer.write("M_03,Montagem,850.40,160\n");
            writer.write("M_04,Fundicao,650.25,140\n");
            writer.write("M_05,Expedicao,450.30,120\n");
        }
    }
    
    @Test
    void testLerConsumoMaquinas() {
        List<ConsumoMaquina> maquinas = leitor.lerConsumoMaquinas(tempMaquinas.toString());

        assertNotNull(maquinas);
        assertEquals(5, maquinas.size());

        ConsumoMaquina primeira = maquinas.get(0);
        assertEquals("M_01", primeira.getMaquinaID());
        assertEquals("Usinagem", primeira.getNomeSetor());
        assertEquals(1200.50, primeira.getConsumoKwh());
        assertEquals(160, primeira.getHoras());
    }

    @Test
    void testLerConfiguracoesSetores() {
        Map<String, SetorConfiguracao> configs = leitor.lerConfiguracoesSetores(tempConfig.toString());

        assertEquals(3, configs.size());
        assertEquals(0.85, configs.get("Usinagem").getFatorEmissao());
        assertEquals(8000.00, configs.get("Montagem").getLimiteMensal());
        assertNull(configs.get("Inexistente"));
    }

    @Test
    void testArquivoInexistente() {
        Map<String, SetorConfiguracao> configs = leitor.lerConfiguracoesSetores("arquivo_que_nao_existe.csv");

        assertNotNull(configs);
        assertTrue(configs.isEmpty());
    }

    @Test
    void testConfiguracoesIgnoraLinhasMalformadas() throws IOException {
        Path arquivo = Files.createTempFile("config_invalido", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile()))) {
            writer.write("nome_setor,fator_emissao,limite_mensal\n");
            writer.write("Usinagem,0.85,10000.00\n");   // válida
            writer.write("\n");                          // linha em branco -> ignorada sem log
            writer.write("Montagem,texto,8000.00\n");    // fator não numérico -> erro
            writer.write("Pintura,0.75\n");              // colunas faltando -> erro
        }

        Map<String, SetorConfiguracao> configs = leitor.lerConfiguracoesSetores(arquivo.toString());

        assertEquals(1, configs.size());
        assertNotNull(configs.get("Usinagem"));
        List<String> logs = leitor.getLogsLeitura();
        assertEquals(2, logs.size());
        assertTrue(logs.get(0).startsWith("[ERRO]"));
    }

    @Test
    void testConsumoIgnoraLinhasMalformadas() throws IOException {
        Path arquivo = Files.createTempFile("consumo_invalido", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile()))) {
            writer.write("maquina_id,setor,consumo_kwh,horas_ativas\n");
            writer.write("M_01,Usinagem,1200.50,160\n");
            writer.write("\n");
            writer.write("M_02,Usinagem,abc,160\n");
            writer.write("M_03,Usinagem\n");
        }

        List<ConsumoMaquina> maquinas = leitor.lerConsumoMaquinas(arquivo.toString());

        assertEquals(1, maquinas.size());
        assertEquals("M_01", maquinas.get(0).getMaquinaID());
        assertEquals(2, leitor.getLogsLeitura().size());
    }

    @Test
    void testConsumoArquivoInexistente() {
        List<ConsumoMaquina> maquinas = leitor.lerConsumoMaquinas("nao_existe_consumo_xyz.csv");

        assertNotNull(maquinas);
        assertTrue(maquinas.isEmpty());
        assertTrue(leitor.getLogsLeitura().isEmpty());
    }

    @Test
    void testArquivoTotalmenteInvalidoRetornaListaVaziaComLogs() throws IOException {
        Path arquivo = Files.createTempFile("consumo_lixo", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile()))) {
            writer.write("cabecalho\n");
            writer.write("DATA;___/___/___;;;;\n");
            writer.write("1;CORONA;;;\n");
        }

        List<ConsumoMaquina> maquinas = leitor.lerConsumoMaquinas(arquivo.toString());

        assertTrue(maquinas.isEmpty());
        assertEquals(2, leitor.getLogsLeitura().size());
    }
}