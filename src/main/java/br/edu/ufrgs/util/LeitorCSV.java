package br.edu.ufrgs.util;

import br.edu.ufrgs.model.ConsumoMaquina;
import br.edu.ufrgs.model.SetorConfiguracao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeitorCSV {

    public Map<String, SetorConfiguracao> lerConfiguracoesSetores(String caminhoArquivo) {
        Map<String, SetorConfiguracao> configs = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {

            br.readLine(); // pula cabeçalho

            String linha;

            while ((linha = br.readLine()) != null) {

                String[] dados = linha.split(",");

                String setor = dados[0];
                double fator = Double.parseDouble(dados[1]);
                double limite = Double.parseDouble(dados[2]);

                configs.put(setor,
                        new SetorConfiguracao(setor, fator, limite));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return configs;
    }

    public List<ConsumoMaquina> lerConsumoMaquinas(String caminhoArquivo) {

        List<ConsumoMaquina> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {

            br.readLine();

            String linha;

            while ((linha = br.readLine()) != null) {

                String[] dados = linha.split(",");

                lista.add(new ConsumoMaquina(
                        dados[0],
                        dados[1],
                        Double.parseDouble(dados[2]),
                        Integer.parseInt(dados[3])
                ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }
}