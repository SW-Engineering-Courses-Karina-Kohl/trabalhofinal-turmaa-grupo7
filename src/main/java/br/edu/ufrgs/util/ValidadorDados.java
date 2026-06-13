package br.edu.ufrgs.util;

import br.edu.ufrgs.model.SetorConfiguracao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ValidadorDados {
    private final List<String> logsErro = new ArrayList<>();

    public boolean setorValido(String setor, Map<String, SetorConfiguracao> configMap){
        if(setor == null || setor.trim().isEmpty()){
            adicionarLogErro("Setor vazio ou nulo", "Setor não informado");
            return false;
        }
        if(configMap == null || !configMap.containsKey(setor)){
            adicionarLogErro("Setor '" + setor + "' não cadastrado", "linha ignorada");
            return false;
        }
        return true;
    }

    public void adicionarLogErro(String identificador, String motivo){
        logsErro.add(String.format("[ERRO] %s -> %s", identificador, motivo));
    }

    public List<String> getLogsErro(){
        return new ArrayList<>(logsErro);
    }

    public boolean possuiErros(){
        return !logsErro.isEmpty();
    }

    public void limparLogs(){
        logsErro.clear();
    }
}
