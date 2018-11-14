package com.stream.spider.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class HttpClientUtil {
    static CloseableHttpClient hc = HttpClients.createDefault();

    static {
        BasicHttpClientConnectionManager hccm = new BasicHttpClientConnectionManager();
        HttpClientBuilder hc = HttpClientBuilder.create().setConnectionManager(hccm);
        /*
        hc.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        hc.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");
        hc.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        hc.getHttpConnectionManager().getParams().setSoTimeout(15000);
    */
    }

    public String get(String url) throws IOException {
//        clearCookies();
        HttpGet g = new HttpGet(url);
        CloseableHttpResponse response = hc.execute(g);
        HttpEntity entity = response.getEntity();
        return IOUtils.toString(entity.getContent());
    }

    public byte[] getAsByte(String url) throws IOException {
//        clearCookies();
        HttpGet g = new HttpGet(url);
        CloseableHttpResponse response = hc.execute(g);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        return IOUtils.toByteArray(is);
    }

    public String getWithRealHeader(String url) throws IOException {
//        clearCookies();
        HttpGet g = new HttpGet(url);
        ////////////////////////
        g.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        g.setHeader("Accept-Language", "zh-cn");
        g.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        g.setHeader("Keep-Alive", "300");
        g.setHeader("Connection", "Keep-Alive");
        g.setHeader("Cache-Control", "no-cache");
        ///////////////////////
        CloseableHttpResponse response = hc.execute(g);
        HttpEntity entity = response.getEntity();
        return IOUtils.toString(entity.getContent());
    }


    public String get(String url, String cookies) throws
            IOException {
//        clearCookies();
        HttpGet g = new HttpGet(url);
        closeRedirect(g);
        if (StringUtils.isNotEmpty(cookies)) {
            g.setHeader("cookie", cookies);
        }
        CloseableHttpResponse response = hc.execute(g);
        HttpEntity entity = response.getEntity();
        return IOUtils.toString(entity.getContent());
    }

    private void closeRedirect(HttpGet g) {
        HttpParams params = new BasicHttpParams();
        params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
        g.setParams(params);
    }

    public String get(String url, String cookies, boolean followRedirects) throws
            IOException {
//        clearCookies();
        HttpGet g = new HttpGet(url);
        this.closeRedirect(g);
        if (StringUtils.isNotEmpty(cookies)) {
            g.addHeader("cookie", cookies);
        }
        CloseableHttpResponse response = hc.execute(g);
        return getResponseBodyAsString(response);
    }
    String getResponseBodyAsString(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        return IOUtils.toString(entity.getContent());
    }
    public String get(String url, boolean followRedirects) throws
            IOException {
//        clearCookies();
        HttpGet g = new HttpGet(url);
        this.closeRedirect(g);
        CloseableHttpResponse response = hc.execute(g);
        HttpEntity entity = response.getEntity();
        return IOUtils.toString(entity.getContent());
    }

    public String getHeader(String url, String cookies, String headername) throws IOException {
//        clearCookies();
        HttpGet g = new HttpGet(url);
        this.closeRedirect(g);
        if (StringUtils.isNotEmpty(cookies)) {
            g.addHeader("cookie", cookies);
        }
        CloseableHttpResponse response = hc.execute(g);
        return response.getFirstHeader(headername) == null ? null : response.getFirstHeader(headername).getValue();
    }

    public String post(String postURL, Map<String, String> partam, String cookies)
            throws IOException {
//        clearCookies();
        HttpPost p = new HttpPost(postURL);
        HttpParams params = new BasicHttpParams();
        for (String key : partam.keySet()) {
            if (partam.get(key) != null) {
                params.setParameter(key,partam.get(key));
            }
        }
        p.setParams(params);
        if (StringUtils.isNotEmpty(cookies)) {
            p.setHeader("cookie", cookies);
        }
        CloseableHttpResponse response = hc.execute(p);
        return this.getResponseBodyAsString(response);
    }

    public String post(String url, String data) throws IOException {
        HttpPost post = new HttpPost(url);
        if (data != null && !data.isEmpty()) {
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(data, "application/json", "utf8"));
        }
        CloseableHttpResponse response = hc.execute(post);
        return this.getResponseBodyAsString(response);
    }

    public String post(String postURL, Map<String, String> partam, String cookies, Map<String, String> header)
            throws IOException {
//        clearCookies();
        HttpPost p = new HttpPost(postURL);
        String reqEntity = "";
        for (Map.Entry<String, String> entry : partam.entrySet()) {
            reqEntity += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf8") + "&";
        }
//        p.setRequestBody(nameValuePair);
        p.setEntity(new StringEntity(reqEntity));
        if (StringUtils.isNotEmpty(cookies)) {
            p.addHeader("cookie", cookies);
        }
        for (Map.Entry<String, String> entry : header.entrySet()) {
            p.addHeader(entry.getKey(), entry.getValue());
        }
        CloseableHttpResponse response = hc.execute(p);
        return this.getResponseBodyAsString(response);
    }

    public String getCookie() {
        HttpClientContext context = HttpClientContext.create();
        CookieStore cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        String tmpcookies = "";
        for (Cookie c : cookies) {
            tmpcookies += c.toString() + ";";
        }
        return tmpcookies;
    }
/*
    public void clearCookies() {
        hc.getState().clearCookies();
    }

    public void addCookie(String cookie, String domain) {
        String[] data = cookie.split(";");
        for (String s : data) {
            String[] kvPair = s.split("=");
            if (kvPair.length == 2) {
                String name = kvPair[0];
                String value = kvPair[1];
                if (!name.equals("path") && !name.equals("domain")) {
                    hc.getState().addCookie(new Cookie(domain, name, value));
                }
            }
        }

    }
*/
}
