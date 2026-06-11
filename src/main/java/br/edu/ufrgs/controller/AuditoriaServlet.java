package br.edu.ufrgs.controller;

import br.edu.ufrgs.model.ConsumoMaquina;
import br.edu.ufrgs.model.SetorConfiguracao;
import br.edu.ufrgs.model.SetorTotalizador;
import br.edu.ufrgs.service.AuditoriaCarbonoService;
import br.edu.ufrgs.util.ExportadorCSV;
import br.edu.ufrgs.util.LeitorCSV;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/auditoria")
public class AuditoriaServlet extends HttpServlet {
    private AuditoriaCarbonoService auditoriaService;

    @Override
    public void init() throws ServletException {
        this.auditoriaService = new AuditoriaCarbonoService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Encontra o caminho real dos arquivos dentro do servidor web empacotado (WAR)
        String caminhoConfig = getServletContext().getRealPath("/WEB-INF/configuracoes.csv");
        String caminhoMaquinas = getServletContext().getRealPath("/WEB-INF/maquinas.csv");
        String caminhoExportacao = getServletContext().getRealPath("/WEB-INF/relatorio_final.csv");

        LeitorCSV leitor = new LeitorCSV();
        
        // lê os arquivos
        Map<String, SetorConfiguracao> configs = leitor.lerConfiguracoesSetores(caminhoConfig);
        List<ConsumoMaquina> maquinas = leitor.lerConsumoMaquinas(caminhoMaquinas);

        // executa a lógica de negócio
        auditoriaService.carregarConfiguracoes(configs);
        auditoriaService.processarConsumo(maquinas);
        List<SetorTotalizador> resultados = auditoriaService.consolidarResultados();

        // exporta o CSV
        ExportadorCSV exportador = new ExportadorCSV();
        exportador.gerarRelatorioCarbono(resultados, caminhoExportacao);

        // prepara para a tela
        exibirResultados(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // Redireciona para o fluxo principal
    }

    private void exibirResultados(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("resultados", auditoriaService.consolidarResultados());
        request.setAttribute("erros", auditoriaService.getLogsValidacao());
        request.getRequestDispatcher("/WEB-INF/resultadoAuditoria.jsp").forward(request, response);
    }
}