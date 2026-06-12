package br.edu.ufrgs.util;

import br.edu.ufrgs.model.SetorConfiguracao;
import br.edu.ufrgs.model.SetorTotalizador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExportadorCSVTest {
    
    @TempDir
    Path tempDir;
    
    @Test
    void testGerarRelatorioCarbonoTemporario() throws Exception {
        SetorConfiguracao configUsinagem = new SetorConfiguracao("Usinagem", 0.85, 10000.00);
        SetorTotalizador usinagem = new SetorTotalizador(configUsinagem);
        usinagem.adicionarConsumo(1200.50);
        usinagem.adicionarConsumo(1150.75);
        usinagem.calcularEmissao();
        
        List<SetorTotalizador> resultados = new ArrayList<>();
        resultados.add(usinagem);
        
        // Testa geração do CSV em arquivo
        ExportadorCSV exportador = new ExportadorCSV();
        String caminhoArquivo = tempDir.resolve("relatorio.csv").toString();
        exportador.gerarRelatorioCarbono(resultados, caminhoArquivo);
        
        // Verifica se o arquivo foi criado
        Path arquivo = Path.of(caminhoArquivo);
        assertTrue(Files.exists(arquivo));
        
        // Lê o conteúdo do arquivo
        String conteudo = Files.readString(arquivo);
        
        assertNotNull(conteudo);
        assertTrue(conteudo.contains("setor,consumo_total_kwh,emissao_total_co2_kg,status_ambiental"));
        assertTrue(conteudo.contains("Usinagem"));
        assertTrue(conteudo.contains("2351.25")); // 1200.50 + 1150.75
    }
    
    @Test
    void testGerarRelatorioCarbonoComMultiplosSetores() throws Exception {
        SetorConfiguracao configUsinagem = new SetorConfiguracao("Usinagem", 0.85, 10000.00);
        SetorConfiguracao configMontagem = new SetorConfiguracao("Montagem", 0.65, 8000.00);
        
        SetorTotalizador usinagem = new SetorTotalizador(configUsinagem);
        usinagem.adicionarConsumo(1200.50);
        usinagem.adicionarConsumo(1150.75);
        usinagem.calcularEmissao();
        
        SetorTotalizador montagem = new SetorTotalizador(configMontagem);
        montagem.adicionarConsumo(800.00);
        montagem.adicionarConsumo(750.50);
        montagem.calcularEmissao();
        
        List<SetorTotalizador> resultados = new ArrayList<>();
        resultados.add(usinagem);
        resultados.add(montagem);
        
        ExportadorCSV exportador = new ExportadorCSV();
        String caminhoArquivo = tempDir.resolve("relatorio_multiplos.csv").toString();
        exportador.gerarRelatorioCarbono(resultados, caminhoArquivo);
        
        Path arquivo = Path.of(caminhoArquivo);
        assertTrue(Files.exists(arquivo));
        
        String conteudo = Files.readString(arquivo);
        assertTrue(conteudo.contains("Usinagem"));
        assertTrue(conteudo.contains("Montagem"));
        assertTrue(conteudo.contains("2351.25"));
        assertTrue(conteudo.contains("1550.50")); // 800.00 + 750.50
    }
    
    @Test
    void testGerarRelatorioCarbonoComListaVazia() throws Exception {
        List<SetorTotalizador> resultados = new ArrayList<>();
        
        ExportadorCSV exportador = new ExportadorCSV();
        String caminhoArquivo = tempDir.resolve("relatorio_vazio.csv").toString();
        exportador.gerarRelatorioCarbono(resultados, caminhoArquivo);
        
        Path arquivo = Path.of(caminhoArquivo);
        assertTrue(Files.exists(arquivo));
        
        String conteudo = Files.readString(arquivo);
        assertTrue(conteudo.contains("setor,consumo_total_kwh,emissao_total_co2_kg,status_ambiental"));
        // Deve ter apenas o cabeçalho
        assertEquals(1, conteudo.split("\n").length);
    }
}