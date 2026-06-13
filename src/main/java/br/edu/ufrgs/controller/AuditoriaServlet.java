package br.edu.ufrgs.controller;

import br.edu.ufrgs.model.ConsumoMaquina;
import br.edu.ufrgs.model.SetorConfiguracao;
import br.edu.ufrgs.model.SetorTotalizador;
import br.edu.ufrgs.service.AuditoriaCarbonoService;
import br.edu.ufrgs.util.ExportadorCSV;
import br.edu.ufrgs.util.LeitorCSV;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

/**
 * Servlet da Auditoria de Carbono
 * Controlador entre o navegador e a lógica de negócio; aceita upload de CSV
 *
 * doPost: recebe os dois arquivos enviados pelo formulário ("fileConfig" = setores e
 * "fileMaquinas" = consumo), grava em arquivos temporários, lê com o LeitorCSV, executa a
 * auditoria no AuditoriaCarbonoService e gera o relatório com o ExportadorCSV. Guarda os
 * resultados na request e o caminho do relatório na sessão (para download).
 *
 * doGet: exibe a tela (resultadoAuditoria.jsp); baixa o último relatório.
 */

@WebServlet("/auditoria")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 100
)
public class AuditoriaServlet extends HttpServlet {
    private AuditoriaCarbonoService auditoriaService;

    @Override
    public void init() throws ServletException {
        this.auditoriaService = new AuditoriaCarbonoService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("download".equals(acao)) {
            String caminhoExportacao = (String) request.getSession().getAttribute("caminhoRelatorio");
            if (caminhoExportacao != null && new File(caminhoExportacao).exists()) {
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment; filename=\"relatorio_auditoria.csv\"");
                Files.copy(new File(caminhoExportacao).toPath(), response.getOutputStream());
                return;
            }
        }
        
        request.getRequestDispatcher("/WEB-INF/resultadoAuditoria.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recebe os ficheiros enviados
        Part partConfig = request.getPart("fileConfig");
        Part partMaquinas = request.getPart("fileMaquinas");

        if (partConfig != null && partMaquinas != null && partConfig.getSize() > 0 && partMaquinas.getSize() > 0) {
            
            // Cria arquivos temporários para não quebrar a lógica do LeitorCSV
            File tempConfig = File.createTempFile("config_upload_", ".csv");
            File tempMaquinas = File.createTempFile("maquinas_upload_", ".csv");
            File tempRelatorio = File.createTempFile("relatorio_final_", ".csv");
            
            try (InputStream isConfig = partConfig.getInputStream()) {
                Files.copy(isConfig, tempConfig.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            try (InputStream isMaquinas = partMaquinas.getInputStream()) {
                Files.copy(isMaquinas, tempMaquinas.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            LeitorCSV leitor = new LeitorCSV();
            
            Map<String, SetorConfiguracao> configs = leitor.lerConfiguracoesSetores(tempConfig.getAbsolutePath());
            List<ConsumoMaquina> maquinas = leitor.lerConsumoMaquinas(tempMaquinas.getAbsolutePath());

            auditoriaService = new AuditoriaCarbonoService(); 
            auditoriaService.carregarConfiguracoes(configs);
            auditoriaService.processarConsumo(maquinas);
            List<SetorTotalizador> resultados = auditoriaService.consolidarResultados();

            ExportadorCSV exportador = new ExportadorCSV();
            exportador.gerarRelatorioCarbono(resultados, tempRelatorio.getAbsolutePath());

            request.getSession().setAttribute("caminhoRelatorio", tempRelatorio.getAbsolutePath());

            request.setAttribute("resultados", resultados);
            request.setAttribute("erros", auditoriaService.getLogsValidacao());
            
            tempConfig.delete();
            tempMaquinas.delete();
        }

        request.getRequestDispatcher("/WEB-INF/resultadoAuditoria.jsp").forward(request, response);
    }
}