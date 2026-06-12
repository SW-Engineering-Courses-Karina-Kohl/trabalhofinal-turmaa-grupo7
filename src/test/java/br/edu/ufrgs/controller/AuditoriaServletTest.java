package br.edu.ufrgs.controller;

import br.edu.ufrgs.model.ConsumoMaquina;
import br.edu.ufrgs.model.SetorConfiguracao;
import br.edu.ufrgs.model.SetorTotalizador;
import br.edu.ufrgs.service.AuditoriaCarbonoService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AuditoriaServletTest {

    private final Map<String, Object> atributos = new HashMap<>();
    private boolean forwardChamado = false;

    private final RequestDispatcher dispatcherStub = new RequestDispatcher() {
        @Override public void forward(jakarta.servlet.ServletRequest q, jakarta.servlet.ServletResponse r) { forwardChamado = true; }
        @Override public void include(jakarta.servlet.ServletRequest q, jakarta.servlet.ServletResponse r) {}
    };

    private final HttpServletRequest requestStub = new HttpServletRequest() {
        @Override public void setAttribute(String n, Object v) { atributos.put(n, v); }
        @Override public Object getAttribute(String n) { return atributos.get(n); }
        @Override public RequestDispatcher getRequestDispatcher(String p) { return dispatcherStub; }
        @Override public String getAuthType() { return null; }
        @Override public jakarta.servlet.http.Cookie[] getCookies() { return null; }
        @Override public long getDateHeader(String n) { return -1; }
        @Override public String getHeader(String n) { return null; }
        @Override public Enumeration<String> getHeaders(String n) { return Collections.emptyEnumeration(); }
        @Override public Enumeration<String> getHeaderNames() { return Collections.emptyEnumeration(); }
        @Override public int getIntHeader(String n) { return -1; }
        @Override public String getMethod() { return "GET"; }
        @Override public String getPathInfo() { return null; }
        @Override public String getPathTranslated() { return null; }
        @Override public String getContextPath() { return ""; }
        @Override public String getQueryString() { return null; }
        @Override public String getRemoteUser() { return null; }
        @Override public boolean isUserInRole(String r) { return false; }
        @Override public java.security.Principal getUserPrincipal() { return null; }
        @Override public String getRequestedSessionId() { return null; }
        @Override public String getRequestURI() { return "/auditoria"; }
        @Override public StringBuffer getRequestURL() { return new StringBuffer("/auditoria"); }
        @Override public String getServletPath() { return ""; }
        @Override public jakarta.servlet.http.HttpSession getSession(boolean c) { return null; }
        @Override public jakarta.servlet.http.HttpSession getSession() { return null; }
        @Override public String changeSessionId() { return null; }
        @Override public boolean isRequestedSessionIdValid() { return false; }
        @Override public boolean isRequestedSessionIdFromCookie() { return false; }
        @Override public boolean isRequestedSessionIdFromURL() { return false; }
        @Override public boolean authenticate(HttpServletResponse r) { return false; }
        @Override public void login(String u, String p) {}
        @Override public void logout() {}
        @Override public Collection<jakarta.servlet.http.Part> getParts() { return null; }
        @Override public jakarta.servlet.http.Part getPart(String n) { return null; }
        @Override public <T extends jakarta.servlet.http.HttpUpgradeHandler> T upgrade(Class<T> c) { return null; }
        @Override public Enumeration<String> getAttributeNames() { return Collections.emptyEnumeration(); }
        @Override public String getCharacterEncoding() { return "UTF-8"; }
        @Override public void setCharacterEncoding(String e) {}
        @Override public int getContentLength() { return 0; }
        @Override public long getContentLengthLong() { return 0; }
        @Override public String getContentType() { return null; }
        @Override public jakarta.servlet.ServletInputStream getInputStream() { return null; }
        @Override public String getParameter(String n) { return null; }
        @Override public Enumeration<String> getParameterNames() { return Collections.emptyEnumeration(); }
        @Override public String[] getParameterValues(String n) { return null; }
        @Override public Map<String, String[]> getParameterMap() { return Collections.emptyMap(); }
        @Override public String getProtocol() { return "HTTP/1.1"; }
        @Override public String getScheme() { return "http"; }
        @Override public String getServerName() { return "localhost"; }
        @Override public int getServerPort() { return 8080; }
        @Override public java.io.BufferedReader getReader() { return null; }
        @Override public String getRemoteAddr() { return "127.0.0.1"; }
        @Override public String getRemoteHost() { return "localhost"; }
        @Override public void removeAttribute(String n) { atributos.remove(n); }
        @Override public Locale getLocale() { return Locale.getDefault(); }
        @Override public Enumeration<Locale> getLocales() { return Collections.enumeration(List.of(Locale.getDefault())); }
        @Override public boolean isSecure() { return false; }
        @Override public int getRemotePort() { return 0; }
        @Override public String getLocalName() { return "localhost"; }
        @Override public String getLocalAddr() { return "127.0.0.1"; }
        @Override public int getLocalPort() { return 8080; }
        @Override public jakarta.servlet.ServletContext getServletContext() { return null; }
        @Override public jakarta.servlet.AsyncContext startAsync() { return null; }
        @Override public jakarta.servlet.AsyncContext startAsync(jakarta.servlet.ServletRequest q, jakarta.servlet.ServletResponse r) { return null; }
        @Override public boolean isAsyncStarted() { return false; }
        @Override public boolean isAsyncSupported() { return false; }
        @Override public jakarta.servlet.AsyncContext getAsyncContext() { return null; }
        @Override public jakarta.servlet.DispatcherType getDispatcherType() { return jakarta.servlet.DispatcherType.REQUEST; }
        @Override public String getRequestId() { return "stub"; }
        @Override public String getProtocolRequestId() { return ""; }
        @Override public jakarta.servlet.ServletConnection getServletConnection() { return null; }
    };

    private final HttpServletResponse responseStub = new HttpServletResponse() {
        @Override public void addCookie(jakarta.servlet.http.Cookie c) {}
        @Override public boolean containsHeader(String n) { return false; }
        @Override public String encodeURL(String u) { return u; }
        @Override public String encodeRedirectURL(String u) { return u; }
        @Override public void sendError(int sc, String m) {}
        @Override public void sendError(int sc) {}
        @Override public void sendRedirect(String l) {}
        @Override public void setDateHeader(String n, long d) {}
        @Override public void addDateHeader(String n, long d) {}
        @Override public void setHeader(String n, String v) {}
        @Override public void addHeader(String n, String v) {}
        @Override public void setIntHeader(String n, int v) {}
        @Override public void addIntHeader(String n, int v) {}
        @Override public void setStatus(int sc) {}
        @Override public int getStatus() { return 200; }
        @Override public String getHeader(String n) { return null; }
        @Override public Collection<String> getHeaders(String n) { return List.of(); }
        @Override public Collection<String> getHeaderNames() { return List.of(); }
        @Override public String getCharacterEncoding() { return "UTF-8"; }
        @Override public String getContentType() { return "text/html"; }
        @Override public jakarta.servlet.ServletOutputStream getOutputStream() { return null; }
        @Override public java.io.PrintWriter getWriter() { return null; }
        @Override public void setCharacterEncoding(String e) {}
        @Override public void setContentLength(int l) {}
        @Override public void setContentLengthLong(long l) {}
        @Override public void setContentType(String t) {}
        @Override public void setBufferSize(int s) {}
        @Override public int getBufferSize() { return 0; }
        @Override public void flushBuffer() {}
        @Override public void resetBuffer() {}
        @Override public boolean isCommitted() { return false; }
        @Override public void reset() {}
        @Override public void setLocale(Locale l) {}
        @Override public Locale getLocale() { return Locale.getDefault(); }
    };

    // ServletContext stub — só getRealPath é chamado pelo servlet
    private final ServletContext contextStub = new ServletContext() {
        @Override public String getRealPath(String p) { return "/tmp" + p; }
        @Override public String getContextPath() { return ""; }
        @Override public ServletContext getContext(String u) { return null; }
        @Override public int getMajorVersion() { return 6; }
        @Override public int getMinorVersion() { return 0; }
        @Override public int getEffectiveMajorVersion() { return 6; }
        @Override public int getEffectiveMinorVersion() { return 0; }
        @Override public String getMimeType(String f) { return null; }
        @Override public Set<String> getResourcePaths(String p) { return null; }
        @Override public java.net.URL getResource(String p) { return null; }
        @Override public java.io.InputStream getResourceAsStream(String p) { return null; }
        @Override public RequestDispatcher getRequestDispatcher(String p) { return null; }
        @Override public RequestDispatcher getNamedDispatcher(String n) { return null; }
        @Override public void log(String m) {}
        @Override public void log(String m, Throwable t) {}
        @Override public String getServerInfo() { return "stub"; }
        @Override public String getInitParameter(String n) { return null; }
        @Override public Enumeration<String> getInitParameterNames() { return Collections.emptyEnumeration(); }
        @Override public boolean setInitParameter(String n, String v) { return false; }
        @Override public Object getAttribute(String n) { return null; }
        @Override public Enumeration<String> getAttributeNames() { return Collections.emptyEnumeration(); }
        @Override public void setAttribute(String n, Object o) {}
        @Override public void removeAttribute(String n) {}
        @Override public String getServletContextName() { return "stub"; }
        @Override public jakarta.servlet.ServletRegistration.Dynamic addServlet(String n, String c) { return null; }
        @Override public jakarta.servlet.ServletRegistration.Dynamic addServlet(String n, jakarta.servlet.Servlet s) { return null; }
        @Override public jakarta.servlet.ServletRegistration.Dynamic addServlet(String n, Class<? extends jakarta.servlet.Servlet> c) { return null; }
        @Override public jakarta.servlet.ServletRegistration.Dynamic addJspFile(String n, String f) { return null; }
        @Override public <T extends jakarta.servlet.Servlet> T createServlet(Class<T> c) { return null; }
        @Override public jakarta.servlet.ServletRegistration getServletRegistration(String n) { return null; }
        @Override public Map<String, ? extends jakarta.servlet.ServletRegistration> getServletRegistrations() { return null; }
        @Override public jakarta.servlet.FilterRegistration.Dynamic addFilter(String n, String c) { return null; }
        @Override public jakarta.servlet.FilterRegistration.Dynamic addFilter(String n, jakarta.servlet.Filter f) { return null; }
        @Override public jakarta.servlet.FilterRegistration.Dynamic addFilter(String n, Class<? extends jakarta.servlet.Filter> c) { return null; }
        @Override public <T extends jakarta.servlet.Filter> T createFilter(Class<T> c) { return null; }
        @Override public jakarta.servlet.FilterRegistration getFilterRegistration(String n) { return null; }
        @Override public Map<String, ? extends jakarta.servlet.FilterRegistration> getFilterRegistrations() { return null; }
        @Override public jakarta.servlet.SessionCookieConfig getSessionCookieConfig() { return null; }
        @Override public void setSessionTrackingModes(Set<jakarta.servlet.SessionTrackingMode> m) {}
        @Override public Set<jakarta.servlet.SessionTrackingMode> getDefaultSessionTrackingModes() { return null; }
        @Override public Set<jakarta.servlet.SessionTrackingMode> getEffectiveSessionTrackingModes() { return null; }
        @Override public void addListener(String c) {}
        @Override public <T extends EventListener> void addListener(T t) {}
        @Override public void addListener(Class<? extends EventListener> c) {}
        @Override public <T extends EventListener> T createListener(Class<T> c) { return null; }
        @Override public jakarta.servlet.descriptor.JspConfigDescriptor getJspConfigDescriptor() { return null; }
        @Override public ClassLoader getClassLoader() { return null; }
        @Override public void declareRoles(String... r) {}
        @Override public String getVirtualServerName() { return "stub"; }
        @Override public int getSessionTimeout() { return 30; }
        @Override public void setSessionTimeout(int t) {}
        @Override public String getRequestCharacterEncoding() { return "UTF-8"; }
        @Override public void setRequestCharacterEncoding(String e) {}
        @Override public String getResponseCharacterEncoding() { return "UTF-8"; }
        @Override public void setResponseCharacterEncoding(String e) {}
    };

    private final ServletConfig configStub = new ServletConfig() {
        @Override public String getServletName() { return "AuditoriaServlet"; }
        @Override public ServletContext getServletContext() { return contextStub; }
        @Override public String getInitParameter(String n) { return null; }
        @Override public Enumeration<String> getInitParameterNames() { return Collections.emptyEnumeration(); }
    };

    private static class ServiceStub extends AuditoriaCarbonoService {
        @Override public void carregarConfiguracoes(Map<String, SetorConfiguracao> c) {}
        @Override public void processarConsumo(List<ConsumoMaquina> m) {}
        @Override public List<SetorTotalizador> consolidarResultados() { return List.of(); }
        @Override public List<String> getLogsValidacao() { return List.of("log simulado"); }
    }

    private AuditoriaServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new AuditoriaServlet();
        servlet.init(configStub);
        Field f = AuditoriaServlet.class.getDeclaredField("auditoriaService");
        f.setAccessible(true);
        f.set(servlet, new ServiceStub());
        atributos.clear();
        forwardChamado = false;
    }

    @Test
    void doGet_deveRedirecionarParaJSP() throws Exception {
        servlet.doGet(requestStub, responseStub);
        assertTrue(forwardChamado);
    }

    @Test
    void doGet_deveDefinirAtributosNaRequest() throws Exception {
        servlet.doGet(requestStub, responseStub);
        assertNotNull(atributos.get("resultados"));
        assertNotNull(atributos.get("erros"));
    }

    @Test
    void doPost_deveDelegarParaDoGet() throws Exception {
        servlet.doPost(requestStub, responseStub);
        assertTrue(forwardChamado);
        assertNotNull(atributos.get("resultados"));
    }
}