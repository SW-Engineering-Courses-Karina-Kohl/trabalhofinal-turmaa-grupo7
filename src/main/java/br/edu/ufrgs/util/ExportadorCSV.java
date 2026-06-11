package br.edu.ufrgs.util;

import br.edu.ufrgs.model.SetorTotalizador;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ExportadorCSV {
    
    /**
     * Gera relatório de carbono em formato CSV
     * @param resultados Lista de totalizadores por setor
     * @param caminhoExportacao Caminho onde o arquivo será salvo
     */
    public void gerarRelatorioCarbono(List<SetorTotalizador> resultados, String caminhoExportacao) {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminhoExportacao))) {
            
            writer.println("setor,consumo_total_kwh,emissao_total_co2_kg,status_ambiental");
            
            for (SetorTotalizador setor : resultados) {
                writer.printf("%s,%.2f,%.2f,%s%n",
                    setor.getConfig().getNomeSetor(),
                    setor.getConsumoTotalKwh(),
                    setor.getEmissaoTotalCo2(),
                    setor.getStatus()
                );
            }
            
            System.out.println("Relatório exportado com sucesso para: " + caminhoExportacao);
            
        } catch (IOException e) {
            System.err.println("Erro ao exportar arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}