<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Relatório de Auditoria de Carbono</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { border-collapse: collapse; width: 100%; margin-bottom: 30px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .erro { color: red; }
    </style>
</head>
<body>
    <h1>Auditoria EcoFactory</h1>

    <h2>Resultados Consolidados</h2>
    <table>
        <tr>
            <th>Setor</th>
            <th>Consumo (kWh)</th>
            <th>Emissão (kg CO2)</th>
            <th>Status Ambiental</th>
        </tr>
        <c:forEach var="item" items="${resultados}">
            <tr>
                <td>${item.setorConfig.nome}</td>
                <td>${item.consumoTotalKwh}</td>
                <td>${item.emissaoTotalCo2}</td>
                <td><b>${item.statusAmbiental}</b></td>
            </tr>
        </c:forEach>
    </table>

    <h2>Logs de Validação (Erros)</h2>
    <ul>
        <c:forEach var="erro" items="${erros}">
            <li class="erro">${erro}</li>
        </c:forEach>
    </ul>
</body>
</html>