package br.edu.ufrgs.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HomeServletTest {

    private HomeServlet servlet;
    private String urlRedirecionada;

    @BeforeEach
    void setUp() {
        servlet = new HomeServlet();
        urlRedirecionada = null;
    }

    @Test
    void doGet_deveRedirecionarParaAuditoria() throws Exception {
        // 1. Prepara o Request falso (Mock dinâmico)
        HttpServletRequest requestMock = (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class[]{HttpServletRequest.class},
                (proxy, method, args) -> {
                    // Quando o servlet chamar getContextPath(), retornamos um contexto simulado
                    if (method.getName().equals("getContextPath")) {
                        return "/ecofactory";
                    }
                    return null;
                }
        );

        // 2. Prepara o Response falso (Mock dinâmico)
        HttpServletResponse responseMock = (HttpServletResponse) Proxy.newProxyInstance(
                HttpServletResponse.class.getClassLoader(),
                new Class[]{HttpServletResponse.class},
                (proxy, method, args) -> {
                    // Quando o servlet chamar sendRedirect(), capturamos a URL passada por parâmetro
                    if (method.getName().equals("sendRedirect")) {
                        urlRedirecionada = (String) args[0];
                    }
                    return null;
                }
        );

        // 3. Executa a ação
        servlet.doGet(requestMock, responseMock);

        // 4. Verifica os resultados
        assertNotNull(urlRedirecionada, "O método sendRedirect deveria ter sido chamado.");
        assertEquals("/ecofactory/auditoria", urlRedirecionada,
                "O redirecionamento deve concatenar o context path com /auditoria");
    }
}