package com.bplow.look.bass.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;

public class WebContext {

	private WebContext()
    {
    }

    public static WebContext getWebContext()
    {
        WebContext context = (WebContext)webContext.get();
        if(context == null)
        {
            context = new WebContext();
            webContext.set(context);
        }
        return context;
    }

    public static void clear()
    {
        webContext.set(null);
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public void setRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }

    public void setResponse(HttpServletResponse response)
    {
        this.response = response;
    }

    public LocaleResolver getLocaleResolver()
    {
        return localeResolver;
    }

    public void setLocaleResolver(LocaleResolver localeResolver)
    {
        this.localeResolver = localeResolver;
    }

    public Locale getLocale()
    {
        if(localeResolver != null && request != null)
            return localeResolver.resolveLocale(request);
        else
            return null;
    }

    private static final ThreadLocal webContext = new ThreadLocal();
    private HttpServletRequest request;
    private HttpServletResponse response;
    private LocaleResolver localeResolver;
}
