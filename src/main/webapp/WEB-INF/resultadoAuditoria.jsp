<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="pt-BR">

        <head>
            <meta charset="UTF-8">
            <title>Auditoria de Emissões</title>
            <style>
                body {
                    font-family: 'Segoe UI', Arial, sans-serif;
                    margin: 0;
                    padding: 20px;
                    background-color: #f4f6f9;
                    color: #333;
                }

                .container {
                    max-width: 900px;
                    margin: 0 auto;
                    background: #fff;
                    padding: 35px;
                    border-radius: 8px;
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                }

                h1 {
                    color: #2c3e50;
                    margin-top: 0;
                    font-size: 1.8rem;
                }

                p.subtitle {
                    color: #64748b;
                    margin-bottom: 30px;
                    font-size: 1rem;
                }

                .upload-group {
                    margin-bottom: 25px;
                    padding: 15px;
                    border: 2px dashed #cbd5e1;
                    border-radius: 8px;
                    background-color: #f8fafc;
                    transition: border-color 0.3s;
                }

                .upload-group:hover {
                    border-color: #3b82f6;
                }

                .upload-group label {
                    display: block;
                    font-weight: bold;
                    margin-bottom: 10px;
                    color: #475569;
                }

                .file-preview {
                    margin-top: 15px;
                }

                .file-name {
                    font-weight: bold;
                    color: #2563eb;
                    font-size: 0.95rem;
                }

                details {
                    background: #fff;
                    border: 1px solid #e2e8f0;
                    border-radius: 4px;
                    padding: 8px 12px;
                    margin-top: 8px;
                }

                summary {
                    cursor: pointer;
                    color: #475569;
                    font-weight: 600;
                    outline: none;
                    font-size: 0.9rem;
                }

                .preview-table-wrap {
                    margin-top: 10px;
                    max-height: 180px;
                    overflow: auto;
                    border: 1px solid #e2e8f0;
                    border-radius: 4px;
                }

                .preview-table {
                    border-collapse: collapse;
                    width: 100%;
                    font-family: monospace;
                    font-size: 0.85em;
                }

                .preview-table th,
                .preview-table td {
                    padding: 6px 10px;
                    border-bottom: 1px solid #e2e8f0;
                    text-align: left;
                    white-space: nowrap;
                }

                .preview-table th {
                    background: #f1f5f9;
                    color: #475569;
                    position: sticky;
                    top: 0;
                }

                .preview-table tr:last-child td {
                    border-bottom: none;
                }

                #btn-processar {
                    display: none;
                    background-color: #2563eb;
                    color: white;
                    border: none;
                    padding: 14px 20px;
                    font-size: 1.05rem;
                    border-radius: 6px;
                    cursor: pointer;
                    width: 100%;
                    font-weight: bold;
                    margin-top: 10px;
                    box-shadow: 0 2px 4px rgba(37, 99, 235, 0.3);
                }

                #btn-processar:hover {
                    background-color: #1d4ed8;
                }

                .btn-download {
                    display: inline-flex;
                    align-items: center;
                    gap: 8px;
                    background-color: #10b981;
                    color: white;
                    text-decoration: none;
                    padding: 12px 20px;
                    border-radius: 6px;
                    font-weight: bold;
                    margin-top: 20px;
                    box-shadow: 0 2px 4px rgba(16, 185, 129, 0.3);
                }

                .btn-download:hover {
                    background-color: #059669;
                }

                .btn-download svg {
                    display: block;
                }

                .resultados-section {
                    margin-top: 40px;
                    border-top: 2px solid #e2e8f0;
                    padding-top: 25px;
                }

                table {
                    border-collapse: collapse;
                    width: 100%;
                    margin-top: 15px;
                }

                th,
                td {
                    padding: 12px;
                    text-align: left;
                    border-bottom: 1px solid #e2e8f0;
                }

                th {
                    background-color: #f8fafc;
                    color: #64748b;
                    font-size: 0.85rem;
                    text-transform: uppercase;
                }

                .badge {
                    padding: 6px 12px;
                    border-radius: 20px;
                    font-size: 0.8rem;
                    font-weight: bold;
                }

                .badge-eco {
                    background-color: #d1fae5;
                    color: #065f46;
                }

                .badge-alerta {
                    background-color: #fef3c7;
                    color: #92400e;
                }

                .badge-alto {
                    background-color: #fee2e2;
                    color: #991b1b;
                }
            </style>
        </head>

        <body>

            <div class="container">
                <h1>Nova Auditoria de Emissões</h1>
                <p class="subtitle">Insira os dados base da fábrica para gerar o relatório de impacto de carbono. Anexe
                    os arquivos nos campos abaixo.</p>

                <form action="${pageContext.request.contextPath}/auditoria" method="post" enctype="multipart/form-data">

                    <div class="upload-group">
                        <label for="file-config">Selecione Arquivo de Configurações</label>
                        <input type="file" id="file-config" name="fileConfig" accept=".csv" required
                            onchange="handleFileSelect(event, 'preview-config')">
                        <div id="preview-config" class="file-preview"></div>
                    </div>

                    <div class="upload-group">
                        <label for="file-maquinas">Consumo das Máquinas</label>
                        <input type="file" id="file-maquinas" name="fileMaquinas" accept=".csv" required
                            onchange="handleFileSelect(event, 'preview-maquinas')">
                        <div id="preview-maquinas" class="file-preview"></div>
                    </div>

                    <button type="submit" id="btn-processar">Processar Auditoria e Gerar Relatório</button>
                </form>

                <c:if test="${not empty resultados}">
                    <div class="resultados-section" id="relatorio-resultados">
                        <h2 style="color: #2c3e50;">Resultado da Auditoria</h2>

                        <table>
                            <thead>
                                <tr>
                                    <th>Setor</th>
                                    <th>Consumo (kWh)</th>
                                    <th>Emissão (kg CO2)</th>
                                    <th>Status Ambiental</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${resultados}">
                                    <tr>
                                        <td>${item.config.nomeSetor}</td>
                                        <td>${item.consumoTotalKwh}</td>
                                        <td>${item.emissaoTotalCo2}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.status == 'ECO_FRIENDLY'}">
                                                    <span class="badge badge-eco">Eco Friendly</span>
                                                </c:when>
                                                <c:when test="${item.status == 'ALERTA'}">
                                                    <span class="badge badge-alerta">Alerta</span>
                                                </c:when>
                                                <c:when test="${item.status == 'ALTO_IMPACTO'}">
                                                    <span class="badge badge-alto">Alto Impacto</span>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <a href="${pageContext.request.contextPath}/auditoria?acao=download" class="btn-download">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
                                fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                stroke-linejoin="round" aria-hidden="true">
                                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                                <polyline points="7 10 12 15 17 10"></polyline>
                                <line x1="12" y1="15" x2="12" y2="3"></line>
                            </svg>
                            <span>Baixar relatório (CSV)</span>
                        </a>

                        <c:if test="${not empty erros}">
                            <div
                                style="margin-top: 25px; background: #fff5f5; border-left: 4px solid #f56565; padding: 15px;">
                                <h4 style="color: #c53030; margin-top: 0;">Inconsistências Encontradas:</h4>
                                <ul style="color: #742a2a; font-family: monospace;">
                                    <c:forEach var="erro" items="${erros}">
                                        <li>${erro}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:if>
                    </div>

                    <script>
                        document.getElementById('relatorio-resultados').scrollIntoView({ behavior: 'smooth' });
                    </script>
                </c:if>
            </div>

            <script>
                function handleFileSelect(event, previewId) {
                    const file = event.target.files[0];
                    const previewContainer = document.getElementById(previewId);

                    if (!file) {
                        previewContainer.innerHTML = '';
                        checkBothFiles();
                        return;
                    }

                    const reader = new FileReader();
                    reader.onload = function (e) {
                        const content = e.target.result;
                        const lines = content.split('\n').filter(function (line) { return line.trim() !== ''; });

                        let html = '<div class="file-name">' + escapeHtml(file.name) + '</div>';
                        html += '<details><summary>Ver dados do arquivo (' + lines.length + ' linhas)</summary>';
                        html += '<div class="preview-table-wrap"><table class="preview-table">';

                        for (let i = 0; i < lines.length; i++) {
                            const celulas = lines[i].split(',');
                            const tag = (i === 0) ? 'th' : 'td';
                            html += '<tr>';
                            for (let j = 0; j < celulas.length; j++) {
                                html += '<' + tag + '>' + escapeHtml(celulas[j].trim()) + '</' + tag + '>';
                            }
                            html += '</tr>';
                        }

                        html += '</table></div></details>';
                        previewContainer.innerHTML = html;

                        checkBothFiles();
                    };
                    reader.readAsText(file);
                }

                function escapeHtml(texto) {
                    const div = document.createElement('div');
                    div.textContent = texto;
                    return div.innerHTML;
                }

                function checkBothFiles() {
                    const file1 = document.getElementById('file-config').files.length > 0;
                    const file2 = document.getElementById('file-maquinas').files.length > 0;
                    const btn = document.getElementById('btn-processar');

                    if (file1 && file2) {
                        btn.style.display = 'block';
                    } else {
                        btn.style.display = 'none';
                    }
                }
            </script>

        </body>

        </html>