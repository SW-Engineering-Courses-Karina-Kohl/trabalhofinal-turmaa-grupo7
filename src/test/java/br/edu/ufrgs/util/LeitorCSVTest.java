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
    
    private Path tempFile;
    private LeitorCSV leitor;  
    
    @BeforeEach
    void setUp() throws IOException {
        leitor = new LeitorCSV();
        
        tempFile = Files.createTempFile("config", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.toFile()))) {
            writer.write("nome_setor,fator_emissao,limite_mensal\n");
            writer.write("Usinagem,0.85,10000.00\n");
            writer.write("Montagem,0.90,8000.00\n");
            writer.write("Pintura,0.75,6000.00\n");
        }
    }
    
    @Test
    void testLerConsumoMaquinas() {
        // este teste precisa do arquivo "consumo_maquinas_teste.csv" no diretório correto
        String caminho = "src/test/resources/consumo_maquinas_teste.csv";
        
        List<ConsumoMaquina> maquinas = leitor.lerConsumoMaquinas(caminho);
        
        assertNotNull(maquinas);
        assertEquals(5, maquinas.size());
        
        // Testa primeira máquina
        ConsumoMaquina primeira = maquinas.get(0);
        assertEquals("M_01", primeira.getMaquinaID());
        assertEquals("Usinagem", primeira.getNomeSetor());
        assertEquals(1200.50, primeira.getConsumoKwh());
        assertEquals(160, primeira.getHoras());
    }
    
    @Test
    void testLerConfiguracoesSetores() {
        Map<String, SetorConfiguracao> configs = leitor.lerConfiguracoesSetores(tempFile.toString());
        
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
}