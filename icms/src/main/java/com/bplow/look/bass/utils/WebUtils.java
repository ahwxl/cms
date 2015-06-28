package com.bplow.look.bass.utils;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;

public abstract class WebUtils extends org.springframework.web.util.WebUtils {

	
	public static String getParameter(ServletRequest request, String name, String defaultValue)
    {
        String v = request.getParameter(name);
        if(StringUtils.isEmpty(v))
            v = defaultValue;
        return v;
    }
}
