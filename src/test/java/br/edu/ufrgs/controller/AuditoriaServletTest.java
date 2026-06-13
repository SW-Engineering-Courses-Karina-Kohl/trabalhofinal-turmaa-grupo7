package br.edu.ufrgs.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AuditoriaServletTest {

    private final Map<String, Object> atributosRequest = new HashMap<>();
    private final Map<String, Object> atributosSession = new HashMap<>();
    private boolean forwardChamado = false;
    private String dispatcherPath = null;
    private File tempRelatorio;

    private final RequestDispatcher dispatcherStub = new RequestDispatcher() {
        @Override
        public void forward(jakarta.servlet.ServletRequest q, jakarta.servlet.ServletResponse r) {
            forwardChamado = true;
        }

        @Override
        public void include(jakarta.servlet.ServletRequest q, jakarta.servlet.ServletResponse r) {
        }
    };

    private final HttpSession sessionStub = new HttpSession() {
        @Override
        public long getCreationTime() {
            return 0;
        }

        @Override
        public String getId() {
            return "stub-session";
        }

        @Override
        public long getLastAccessedTime() {
            return 0;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public void setMaxInactiveInterval(int interval) {
        }

        @Override
        public int getMaxInactiveInterval() {
            return 0;
        }

        @Override
        public Object getAttribute(String name) {
            return atributosSession.get(name);
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return Collections.enumeration(atributosSession.keySet());
        }

        @Override
        public void setAttribute(String name, Object value) {
            atributosSession.put(name, value);
        }

        @Override
        public void removeAttribute(String name) {
            atributosSession.remove(name);
        }

        @Override
        public void invalidate() {
        }

        @Override
        public boolean isNew() {
            return false;
        }
    };

    private final HttpServletRequest requestStub = new HttpServletRequest() {
        @Override
        public void setAttribute(String n, Object v) {
            atributosRequest.put(n, v);
        }

        @Override
        public Object getAttribute(String n) {
            return atributosRequest.get(n);
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String p) {
            dispatcherPath = p;
            return dispatcherStub;
        }

        @Override
        public HttpSession getSession() {
            return sessionStub;
        }

        @Override
        public HttpSession getSession(boolean create) {
            return sessionStub;
        }

        @Override
        public String getParameter(String n) {
            if ("acao".equals(n))
                return parametroAcao;
            return null;
        }

        @Override
        public Part getPart(String n) {
            if ("fileConfig".equals(n))
                return partConfigStub;
            if ("fileMaquinas".equals(n))
                return partMaquinasStub;
            return null;
        }

        @Override
        public String getAuthType() {
            return null;
        }

        @Override
        public jakarta.servlet.http.Cookie[] getCookies() {
            return null;
        }

        @Override
        public long getDateHeader(String n) {
            return -1;
        }

        @Override
        public String getHeader(String n) {
            return null;
        }

        @Override
        public Enumeration<String> getHeaders(String n) {
            return Collections.emptyEnumeration();
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return Collections.emptyEnumeration();
        }

        @Override
        public int getIntHeader(String n) {
            return -1;
        }

        @Override
        public String getMethod() {
            return "GET";
        }

        @Override
        public String getPathInfo() {
            return null;
        }

        @Override
        public String getPathTranslated() {
            return null;
        }

        @Override
        public String getContextPath() {
            return "";
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public String getRemoteUser() {
            return null;
        }

        @Override
        public boolean isUserInRole(String r) {
            return false;
        }

        @Override
        public java.security.Principal getUserPrincipal() {
            return null;
        }

        @Override
        public String getRequestedSessionId() {
            return null;
        }

        @Override
        public String getRequestURI() {
            return "/auditoria";
        }

        @Override
        public StringBuffer getRequestURL() {
            return new StringBuffer("/auditoria");
        }

        @Override
        public String getServletPath() {
            return "";
        }

        @Override
        public String changeSessionId() {
            return null;
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        @Override
        public boolean authenticate(HttpServletResponse r) {
            return false;
        }

        @Override
        public void login(String u, String p) {
        }

        @Override
        public void logout() {
        }

        @Override
        public Collection<Part> getParts() {
            return null;
        }

        @Override
        public <T extends jakarta.servlet.http.HttpUpgradeHandler> T upgrade(Class<T> c) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return Collections.emptyEnumeration();
        }

        @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        @Override
        public void setCharacterEncoding(String e) {
        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public jakarta.servlet.ServletInputStream getInputStream() {
            return null;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.emptyEnumeration();
        }

        @Override
        public String[] getParameterValues(String n) {
            return null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.emptyMap();
        }

        @Override
        public String getProtocol() {
            return "HTTP/1.1";
        }

        @Override
        public String getScheme() {
            return "http";
        }

        @Override
        public String getServerName() {
            return "localhost";
        }

        @Override
        public int getServerPort() {
            return 8080;
        }

        @Override
        public java.io.BufferedReader getReader() {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return "127.0.0.1";
        }

        @Override
        public String getRemoteHost() {
            return "localhost";
        }

        @Override
        public void removeAttribute(String n) {
            atributosRequest.remove(n);
        }

        @Override
        public Locale getLocale() {
            return Locale.getDefault();
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return Collections.enumeration(List.of(Locale.getDefault()));
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public String getLocalName() {
            return "localhost";
        }

        @Override
        public String getLocalAddr() {
            return "127.0.0.1";
        }

        @Override
        public int getLocalPort() {
            return 8080;
        }

        @Override
        public jakarta.servlet.ServletContext getServletContext() {
            return null;
        }

        @Override
        public jakarta.servlet.AsyncContext startAsync() {
            return null;
        }

        @Override
        public jakarta.servlet.AsyncContext startAsync(jakarta.servlet.ServletRequest q,
                jakarta.servlet.ServletResponse r) {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public jakarta.servlet.AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public jakarta.servlet.DispatcherType getDispatcherType() {
            return jakarta.servlet.DispatcherType.REQUEST;
        }

        @Override
        public String getRequestId() {
            return "stub";
        }

        @Override
        public String getProtocolRequestId() {
            return "";
        }

        @Override
        public jakarta.servlet.ServletConnection getServletConnection() {
            return null;
        }
    };

    private String contentTypeDefinido = null;
    private String headerDefinido = null;

    private final HttpServletResponse responseStub = new HttpServletResponse() {
        @Override
        public void setContentType(String t) {
            contentTypeDefinido = t;
        }

        @Override
        public void setHeader(String n, String v) {
            headerDefinido = v;
        }

        @Override
        public void addCookie(jakarta.servlet.http.Cookie c) {
        }

        @Override
        public boolean containsHeader(String n) {
            return false;
        }

        @Override
        public String encodeURL(String u) {
            return u;
        }

        @Override
        public String encodeRedirectURL(String u) {
            return u;
        }

        @Override
        public void sendError(int sc, String m) {
        }

        @Override
        public void sendError(int sc) {
        }

        @Override
        public void sendRedirect(String l) {
        }

        @Override
        public void setDateHeader(String n, long d) {
        }

        @Override
        public void addDateHeader(String n, long d) {
        }

        @Override
        public void addHeader(String n, String v) {
        }

        @Override
        public void setIntHeader(String n, int v) {
        }

        @Override
        public void addIntHeader(String n, int v) {
        }

        @Override
        public void setStatus(int sc) {
        }

        @Override
        public int getStatus() {
            return 200;
        }

        @Override
        public String getHeader(String n) {
            return null;
        }

        @Override
        public Collection<String> getHeaders(String n) {
            return List.of();
        }

        @Override
        public Collection<String> getHeaderNames() {
            return List.of();
        }

        @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        @Override
        public String getContentType() {
            return "text/html";
        }

        @Override
        public jakarta.servlet.ServletOutputStream getOutputStream() {
            return new jakarta.servlet.ServletOutputStream() {
                @Override
                public void write(int b) {
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                }
            };
        }

        @Override
        public java.io.PrintWriter getWriter() {
            return null;
        }

        @Override
        public void setCharacterEncoding(String e) {
        }

        @Override
        public void setContentLength(int l) {
        }

        @Override
        public void setContentLengthLong(long l) {
        }

        @Override
        public void setBufferSize(int s) {
        }

        @Override
        public int getBufferSize() {
            return 0;
        }

        @Override
        public void flushBuffer() {
        }

        @Override
        public void resetBuffer() {
        }

        @Override
        public boolean isCommitted() {
            return false;
        }

        @Override
        public void reset() {
        }

        @Override
        public void setLocale(Locale l) {
        }

        @Override
        public Locale getLocale() {
            return Locale.getDefault();
        }
    };

    private final ServletConfig configStub = new ServletConfig() {
        @Override
        public String getServletName() {
            return "AuditoriaServlet";
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public String getInitParameter(String n) {
            return null;
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return Collections.emptyEnumeration();
        }
    };

    private AuditoriaServlet servlet;
    private String parametroAcao = null;
    private Part partConfigStub = null;
    private Part partMaquinasStub = null;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new AuditoriaServlet();
        servlet.init(configStub);

        atributosRequest.clear();
        atributosSession.clear();
        forwardChamado = false;
        dispatcherPath = null;
        parametroAcao = null;
        partConfigStub = null;
        partMaquinasStub = null;
        contentTypeDefinido = null;
        headerDefinido = null;
    }

    @Test
    void doGet_deveRedirecionarParaFormularioInicial() throws Exception {
        servlet.doGet(requestStub, responseStub);
        assertTrue(forwardChamado);
        assertEquals("/WEB-INF/resultadoAuditoria.jsp", dispatcherPath);
    }

    @Test
    void doGet_deveFazerDownloadSeAcaoForDownloadEArquivoExistir() throws Exception {
        tempRelatorio = File.createTempFile("teste_relatorio", ".csv");
        Files.writeString(tempRelatorio.toPath(), "teste,csv");

        parametroAcao = "download";
        atributosSession.put("caminhoRelatorio", tempRelatorio.getAbsolutePath());

        servlet.doGet(requestStub, responseStub);

        assertEquals("text/csv", contentTypeDefinido);
        assertEquals("attachment; filename=\"relatorio_carbono.csv\"", headerDefinido);

        tempRelatorio.delete();
    }

    @Test
    void doPost_deveProcessarArquivosEDefinirAtributos() throws Exception {
        String csvConfig = "nome_setor,fator_emissao,limite_mensal\nUsinagem,0.85,10000.00";
        partConfigStub = criarPartStub(csvConfig);

        String csvMaquinas = "id,setor,consumo,horas\nM1,Usinagem,100,10";
        partMaquinasStub = criarPartStub(csvMaquinas);

        servlet.doPost(requestStub, responseStub);

        assertTrue(forwardChamado);
        assertNotNull(atributosRequest.get("resultados"));
        assertNotNull(atributosSession.get("caminhoRelatorio"));
    }

    private Part criarPartStub(String conteudo) {
        return new Part() {
            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(conteudo.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public String getContentType() {
                return "text/csv";
            }

            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getSubmittedFileName() {
                return "teste.csv";
            }

            @Override
            public long getSize() {
                return conteudo.length();
            }

            @Override
            public void write(String s) {
            }

            @Override
            public void delete() {
            }

            @Override
            public String getHeader(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }
        };
    }

    @Test
    void doGet_acaoDownloadSemRelatorioNaSessao_deveEncaminharParaJsp() throws Exception {
        parametroAcao = "download";
        servlet.doGet(requestStub, responseStub);
        assertTrue(forwardChamado);
        assertEquals("/WEB-INF/resultadoAuditoria.jsp", dispatcherPath);
    }

    @Test
    void doGet_acaoDownloadComArquivoInexistente_deveEncaminharParaJsp() throws Exception {
        parametroAcao = "download";
        atributosSession.put("caminhoRelatorio", "caminho/que/nao/existe_xyz.csv");
        servlet.doGet(requestStub, responseStub);
        assertTrue(forwardChamado);
        assertEquals("/WEB-INF/resultadoAuditoria.jsp", dispatcherPath);
    }

    @Test
    void doPost_semArquivoConfig_naoProcessa() throws Exception {
        partConfigStub = null;
        partMaquinasStub = criarPartStub("id,setor,consumo,horas\nM1,Usinagem,100,10");
        servlet.doPost(requestStub, responseStub);
        assertTrue(forwardChamado);
        assertNull(atributosRequest.get("resultados"));
    }

    @Test
    void doPost_semArquivoMaquinas_naoProcessa() throws Exception {
        partConfigStub = criarPartStub("nome_setor,fator_emissao,limite_mensal\nUsinagem,0.85,10000.00");
        partMaquinasStub = null;
        servlet.doPost(requestStub, responseStub);
        assertTrue(forwardChamado);
        assertNull(atributosRequest.get("resultados"));
    }

    @Test
    void doPost_arquivoConfigVazio_naoProcessa() throws Exception {
        partConfigStub = criarPartStub("");
        partMaquinasStub = criarPartStub("id,setor,consumo,horas\nM1,Usinagem,100,10");
        servlet.doPost(requestStub, responseStub);
        assertTrue(forwardChamado);
        assertNull(atributosRequest.get("resultados"));
    }

    @Test
    void doPost_arquivoMaquinasVazio_naoProcessa() throws Exception {
        partConfigStub = criarPartStub("nome_setor,fator_emissao,limite_mensal\nUsinagem,0.85,10000.00");
        partMaquinasStub = criarPartStub("");
        servlet.doPost(requestStub, responseStub);
        assertTrue(forwardChamado);
        assertNull(atributosRequest.get("resultados"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void doPost_configSemLinhaValida_naoGeraResultadoEMostraMensagem() throws Exception {
        String configInvalido = "cabecalho\nDATA;___/___/___;;;;\nITEM;ENTRADA;SAIDA;SALDO";
        partConfigStub = criarPartStub(configInvalido);
        partMaquinasStub = criarPartStub("id,setor,consumo,horas\nM1,Usinagem,100,10");

        servlet.doPost(requestStub, responseStub);

        assertTrue(forwardChamado);
        assertNull(atributosRequest.get("resultados"));
        assertNull(atributosSession.get("caminhoRelatorio"));
        String mensagem = (String) atributosRequest.get("mensagemSemDados");
        assertNotNull(mensagem);
        assertTrue(mensagem.contains("configuração"));
        List<String> erros = (List<String>) atributosRequest.get("erros");
        assertNotNull(erros);
        assertFalse(erros.isEmpty());
    }

    @Test
    void doPost_maquinasSemLinhaValida_naoGeraResultadoEMostraMensagem() throws Exception {
        partConfigStub = criarPartStub("nome_setor,fator_emissao,limite_mensal\nUsinagem,0.85,10000.00");
        String maquinasInvalido = "cabecalho\nM_01;Usinagem;abc;xyz\nlinha_quebrada";
        partMaquinasStub = criarPartStub(maquinasInvalido);

        servlet.doPost(requestStub, responseStub);

        assertTrue(forwardChamado);
        assertNull(atributosRequest.get("resultados"));
        assertNull(atributosSession.get("caminhoRelatorio"));
        String mensagem = (String) atributosRequest.get("mensagemSemDados");
        assertNotNull(mensagem);
        assertTrue(mensagem.contains("consumo"));
    }

}