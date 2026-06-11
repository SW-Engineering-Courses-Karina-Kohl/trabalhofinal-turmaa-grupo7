package br.edu.ufrgs.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufrgs.model.ConsumoMaquina;
import br.edu.ufrgs.model.SetorConfiguracao;

public class LeitorCSV {
    public static List<ConsumoMaquina> lerConsumoMaquinas(String arquivoCsv) {
        
        List<ConsumoMaquina> listaConsumoMaquinas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCsv))) {
            String linha;
            boolean isCabecalho = true;

            while ((linha = br.readLine()) != null) {
                if (isCabecalho) {
                    isCabecalho = false;
                    continue; // Pula o cabeçalho
                }

                String[] campos = linha.split(",");

                if (campos.length >= 4) {
                    ConsumoMaquina consumoMaq = new ConsumoMaquina(
                        campos[0].trim(),
                        campos[1].trim(),
                        Double.parseDouble(campos[2].trim()),
                        Integer.parseInt(campos[3].trim())
                    );
                    listaConsumoMaquinas.add(consumoMaq);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }

        return listaConsumoMaquinas;
    }   

    public static Map<String, SetorConfiguracao> lerConfiguracoesSetores(String arquivoCsv) {
    
        Map<String, SetorConfiguracao> mapaSetorConfiguracao = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCsv))) {
            String linha;
            boolean isCabecalho = true;

            while ((linha = br.readLine()) != null) {
                if (isCabecalho) {
                    isCabecalho = false;
                    continue; // Pula o cabeçalho
                }

                String[] campos = linha.split(",");

                if (campos.length >= 3) { 
                    SetorConfiguracao setorConfiguracao = new SetorConfiguracao(
                        campos[0].trim(),                    // nomeSetor
                        Double.parseDouble(campos[1].trim()), // fatorEmissao
                        Double.parseDouble(campos[2].trim())  // limiteMensal
                    );
                    mapaSetorConfiguracao.put(setorConfiguracao.getNomeSetor(), setorConfiguracao);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }

        return mapaSetorConfiguracao;
    }     
}
