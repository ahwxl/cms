package com.bplow.base.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtil 
{
	
	/**
	 * 功能描述: 获取项目HTTP路径
	 * 时          间: 2011-12-26
	 * @author  韩冬 
	 * @param req
	 * @return
	 */
	public  static StringBuilder getWebAppPath(HttpServletRequest req)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(req.getScheme());

		sb.append("://");
		sb.append(req.getServerName());
		sb.append(":");
		sb.append(req.getServerPort());
		sb.append(req.getContextPath());
		return sb;
	}
	
	/**
	 * 功能描述: 获取服务器物理路径
	 * 时          间: 2011-12-27
	 * @author  韩冬 
	 * @param req
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static StringBuilder getWebRealPath(HttpServletRequest req)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(req.getRealPath("/"));
		return sb;

	}

}
