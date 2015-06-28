package com.bplow.base.page;;

/**
 * 分页--生成HTML
 * 
 * 
 */
public class PageHtml implements PageHtmlInter {
	String url;// 针对不同模块所使用得参数

	String from;//页面所对应的FORM名称
	int currentPage; // 当前页


	int totalPages; // 总页数


	int totalRows; // 总行数


	int everySize;// 每页显示的条数


	int sdream;//跳转到的页数

	/**
	 * 生成HTML
	 * 
	 * @param data
	 *            将此替换为你查询到得数据集合
	 */
	public PageHtml(HibernatePage pages) {
		totalPages = pages.getTotalPage();
		totalRows = pages.getTotalRows();
		everySize = pages.getPageSize();
		currentPage = pages.getCurPage();
		url = pages.getUrl();
        from = pages.getFrom();
		sdream = pages.getYouto();
		init();
	}

	public int getCurrentPage() {
		return currentPage;
	}
    
    /**
     * 生成HTML
     * 
     * @param data
     *            将此替换为你查询到得数据集合
     */
	public String getHTML() {
		StringBuffer html = new StringBuffer();
        html.append("<script language='javascript'>"
                +"function sub(url) {document."+from+".action=url;document."+from+".submit();}"
            +"</script>"  
        );
		if (totalRows == 0) {
			html
					.append("<div class='boss'><br />没有记录！<br /></div>");
		} else {
			/*html.append("\r<a href=javascript:sub('" + url + "everySize=" + everySize
					+ "&page=1')>首页</a>\r<a href=javascript:sub('" + url + "everySize="
					+ everySize + "&page=" + (currentPage - 1)
					+ "')>上页</a>");
			
			html.append("<a href=javascript:sub('" + url + "everySize=" + everySize
                    + "&page=" + (currentPage + 1)
                    + "')  >下页</a>\r<a href=javascript:sub('" + url + "everySize="
					+ everySize + "&page=" + totalPages + "')>末页</a>\r");*/
			
			if (currentPage == 1){
				html.append("");
			} else {
				html.append("\r<a href=javascript:sub('" + url + "everySize=" + everySize
						+ "&page=1')>首页</a>\r<a href=javascript:sub('" + url + "everySize="
						+ everySize + "&page=" + (currentPage - 1)
						+ "')>上页</a>");
			}
			if (currentPage >= totalPages) {
				html.append("");
			} else {
				html.append("<a href=javascript:sub('" + url + "everySize=" + everySize
                        + "&page=" + (currentPage + 1)
                        + "')  >下页</a>\r<a href=javascript:sub('" + url + "everySize="
						+ everySize + "&page=" + totalPages + "')>末页</a>\r");
			}
			
			/*if (currentPage == 1){
				html.append("");
			} else {
				html.append("\r<a href=javascript:sub('" + url + "everySize=" + everySize
						+ "&page=1')>首页</a>\r<a href=javascript:sub('" + url + "everySize="
						+ everySize + "&page=" + (currentPage - 1)
						+ "')>上页</a>");
			}

			if (totalPages <= 10) {
				for (int i = 1; i < totalPages + 1; i++) {
					if (currentPage == i) {
						html.append("<span class='thisPage' title='当前页'>&nbsp;"
								+ i + "&nbsp;</span>");
					} else {
						html.append("<a href=javascript:sub('" + url + "everySize="
								+ everySize + "&page=" + i + "')>&nbsp;" + i
								+ "&nbsp;</a>");
					}
				}
			} else {
				if (currentPage <= 5) {
					for (int i = 1; i < 11; i++) {
						if (currentPage == i) {
							html
									.append("<span class='thisPage' title='当前页'>&nbsp;"
											+ i + "&nbsp;</span>");
						} else {
							html.append("<a href=javascript:sub('" + url + "everySize="
									+ everySize + "&page=" + i + "')>&nbsp;" + i
									+ "&nbsp;</a>");
						}
					}
					html.append("<a href=javascript:sub('" + url + "everySize=" + everySize
							+ "&page=15' title='第11-20页')>>></a>");
				} else if (currentPage >= totalPages - 5) {
					html.append("<a href=javascript:sub('" + url + "everySize=" + everySize
							+ "&page=" + (totalPages - 15) + "' title='第"
							+ (totalPages - 19) + "-" + (totalPages - 10)
							+ "页')><<</a>");
					for (int i = totalPages - 9; i < totalPages + 1; i++) {
						if (currentPage == i) {
							html
									.append("<span class='thisPage' title='当前页'>&nbsp;"
											+ i + "&nbsp;</span>");
						} else {
							html.append("<a href=javascript:sub('" + url + "everySize="
									+ everySize + "&page=" + i + "')>&nbsp;" + i
									+ "&nbsp;</a>");
						}
					}
				} else {
					if (currentPage <= 10) {
					} else if (currentPage > 10 & currentPage < 15) {
						html.append("<a href=javascript:sub('" + url + "everySize="
								+ everySize
								+ "&page=5' title='第1-10页')><<</a>");
					} else {
						html.append("<a href=javascript:sub('" + url + "everySize="
								+ everySize + "&page=" + (currentPage - 10)
								+ "' title='第" + (currentPage - 14) + "-"
								+ (currentPage - 5) + "页')><<</a>");
					}
					for (int i = currentPage - 4; i <= currentPage + 5; i++) {
						if (currentPage == i) {
							html
									.append("<span class='thisPage' title='当前页'>&nbsp;"
											+ i + "&nbsp;</span>");
						} else {
							html.append("<a href=javascript:sub('" + url + "everySize="
									+ everySize + "&page=" + i + "')>&nbsp;" + i
									+ "&nbsp;</a>");
						}
					}
					if (currentPage > totalPages - 15
							& currentPage <= totalPages - 10) {
						html.append("<a href=javascript:sub('" + url + "everySize="
								+ everySize + "&page=" + (totalPages - 5)
								+ "' title='第" + (totalPages - 9) + "-"
								+ (totalPages) + "页')>>></a>");
					} else if (currentPage > totalPages - 10) {
					} else {
						html.append("<a href=javascript:sub('" + url + "everySize="
								+ everySize + "&page=" + (currentPage + 10)
								+ "' title='第" + (currentPage + 6) + "-"
								+ (currentPage + 15) + "页')>>></a>");
					}
				}
			}

			if (currentPage >= totalPages) {
				html.append("");
			} else {
				html.append("<a href=javascript:sub('" + url + "everySize=" + everySize
                        + "&page=" + (currentPage + 1)
                        + "')  >下页</a>\r<a href=javascript:sub('" + url + "everySize="
						+ everySize + "&page=" + totalPages + "')>末页</a>\r");
			}
			*/
			html
					.append("每页<input value='"
							+ everySize
							+ "' name='everySize' type='text' id='everySize' class='showNum' size='1' title='您可以自定义每页显示多少条数据'>条" 
							+"共"+totalRows+"条&nbsp;第<input value='"
							+ currentPage
							+ "' name='pageNo' type='text' id='pageNo' class='toPage' size='1' onKeyPress='return handleEnterOnPageNo();' title='输入您要查看的页码'>/"
							+ totalPages
							+ "页&nbsp;"
							+ "<input name='goto' type='button' id='goto' value='提交' onClick='forward();' style='font-family: Arial, Helvetica, sans-serif;font-size: 12px;color: #FFFFFF;background-color: #6e6e6e;height: 21px;border-right-width: 1px;border-bottom-width: 1px;border-top-style: none;border-right-style: solid;border-bottom-style: solid;border-left-style: none;border-right-color: #434343;border-bottom-color: #434343;line-height: 21px;'></div>");
			html.append("<script type='text/javaScript'>");
			html.append("function   forward(){");
			html
					.append("      if(!(/^([-]){0,1}([0-9]){1,}$/.test(document.all.pageNo.value)))");
			html.append("    {");
			html.append("alert('不合法的页号！');");
			html.append("document.all.pageNo.focus();");
			html.append("document.all.pageNo.select();");
			html
					.append("}else if(document.all.pageNo.value<1){ alert('无法显示至比1小的页！');}");
			html
			.append("else if(document.all.pageNo.value>"+totalPages+"){ alert('对不起，你填的页数，超过总页数！');document.all.pageNo.focus();document.all.pageNo.select(); }");
			html
			.append("else if(document.all.everySize.value>"+100+"){ alert('对不起，每页最多显示100条信息！');document.all.everySize.focus();document.all.everySize.select(); }");
			
			html
					.append("else{if(!(/^([-]){0,1}([0-9]){1,}$/.test(document.all.everySize.value))){ alert(document.all.everySize.value+'不是合法的数字!');return   false;}");
			html
					.append("if(document.all.everySize.value<1){ alert('无法在页面显示'+document.all.everySize.value+'条记录!');return   false}document."+from+".action='"
							+ url
							+ "page='+document.all.pageNo.value+'&everySize='+document.all.everySize.value;" 
                            + "document."+from+".submit();"+"}");
			html
					.append("}function   handleEnterOnPageNo(){if(event.keyCode   ==   13){");
			html
					.append("forward();return false;}return true;}</script>\n<!--分页HTML-part-2结束-->");
		}
		return html.toString();
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public String getUrl() {
		return url;
	}

	public void init() {
		// 如果url 中没有？ 就在末尾加上？

		if (url.indexOf("?") == -1)
			url += "?";
		// 如果url中的？ 在末尾就不加& 不在末尾就加&---//从页面获得totalRows得值对于分页毫无意义，保留其为备用参数
		if (url.indexOf("?") == url.length() - 1){			
			url = url 
					+ new StringBuffer("totalRows=").append(totalRows).append(
							"&").toString();
		}
		else {
			url = url + "&"
					+ new StringBuffer("&totalRows=").append(totalRows).append(
							"&").toString();
		}
	}

	public void setCurrentPage(int i) {
		currentPage = i;
	}

	public void setTotalPages(int i) {
		totalPages = i;
	}

	public void setTotalRows(int i) {
		totalRows = i;
	}

	public void setUrl(String s) {
		url = s;
	}
	/**
     * 开始创建HTML代码
	 */
	public String getStartHTML() {
		StringBuffer html = new StringBuffer();
		html.append("<!--分页HTML-part-1开始--author:SDream-->\n<div class='startBoss'>");
/*		if (sdream > totalPages) {
			if (sdream == 364230257) {
				html
				.append("<div class='pageToo' id='pageToo'onclick=pageToo.className='pageTooFor' align='center'><span>SDream</span></div>");
			}else{				
//			html
//					.append("<div class='pageToo' id='pageToo'onclick=pageToo.className='pageTooFor' align='center'><span>!</span><div>您要求每页显示"
//							+ everySize
//							+ "条，并且要求是第"
//							+ sdream
//							+ "页，而本次的查询结果一共"
//							+ totalPages + "页，小于你要查看的页次，所以自动跳转到最后一页!</div></div>");
			}
		}*/
		
		/**
		 * 共<span class='starText'>" + totalRows
				+ "</span>条
				\n每页<span class='starText'>" + everySize
				+ "</span>条,\n共<span class='starText'>" + totalPages
				+ "</span>页.\n
		 */
		html.append("<!--分页HTML-part-1结束-->");
		return html.toString();
	}

    
    public String getFrom() {
    
        return from;
    }

    
    public void setFrom(String from) {
    
        this.from = from;
    }

}
