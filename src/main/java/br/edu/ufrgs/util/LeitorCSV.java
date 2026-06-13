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

    private final List<String> logsLeitura = new ArrayList<>();

    public List<String> getLogsLeitura() {
        return new ArrayList<>(logsLeitura);
    }

    public Map<String, SetorConfiguracao> lerConfiguracoesSetores(String caminhoArquivo) {
        Map<String, SetorConfiguracao> configs = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {

            br.readLine(); // pula cabeçalho

            String linha;
            int numeroLinha = 1;

            while ((linha = br.readLine()) != null) {
                numeroLinha++;

                if (linha.trim().isEmpty()) {
                    continue; // ignora linhas em branco
                }

                String[] dados = linha.split(",");

                if (dados.length < 3) {
                    registrarErro(numeroLinha, linha,
                            "esperadas 3 colunas (setor,fator,limite)");
                    continue;
                }

                try {
                    String setor = dados[0].trim();
                    double fator = Double.parseDouble(dados[1].trim());
                    double limite = Double.parseDouble(dados[2].trim());

                    configs.put(setor,
                            new SetorConfiguracao(setor, fator, limite));
                } catch (NumberFormatException e) {
                    registrarErro(numeroLinha, linha,
                            "fator/limite não numérico");
                }
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
            int numeroLinha = 1;

            while ((linha = br.readLine()) != null) {
                numeroLinha++;

                if (linha.trim().isEmpty()) {
                    continue; // ignora linhas em branco
                }

                String[] dados = linha.split(",");

                if (dados.length < 4) {
                    registrarErro(numeroLinha, linha,
                            "esperadas 4 colunas (maquina_id,setor,consumo_kwh,horas_ativas)");
                    continue;
                }

                try {
                    lista.add(new ConsumoMaquina(
                            dados[0].trim(),
                            dados[1].trim(),
                            Double.parseDouble(dados[2].trim()),
                            Integer.parseInt(dados[3].trim())
                    ));
                } catch (NumberFormatException e) {
                    registrarErro(numeroLinha, linha,
                            "consumo_kwh/horas_ativas não numérico");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    private void registrarErro(int numeroLinha, String conteudo, String motivo) {
        logsLeitura.add(String.format(
                "[ERRO] Linha %d ignorada (%s): %s", numeroLinha, motivo, conteudo));
    }
}