package com.bplow.look.bass.autoCreate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.ArrayUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.bplow.look.bass.annotation.fieldDesc;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class StartHere {
	
	private Configuration cfg;
	Properties prop = new Properties();
	String propertyName = "temlFile.properties";//属性文件的文件名
	String WebRootPath = null;//WebRoot路径
	String classPath = null;//当前类所在的路径
	String moduleName = null;
	String voname = null;//实体类名称
	String voFullName = null;//实体类全路径
	List classProField = new ArrayList<FieldAttribute>();
	
	String jspQueryListPageTempl = null;
	String jspAddPageTempl = null;
	String jspEditorPageTempl = null;
	
	String classDaoHibernateTempl = null;
	String classDaoJdbcTempl = null;
	String classServiceTempl = null;
	String classActionTempl = null;
	String classToolKitsTempl = null;
	
	/**
	 * 获取类的全路径
	 * 初始化模板类的定义
	 */
	public StartHere() {
		super();
		classPath = this.getClass().getClassLoader().getResource("").getPath();		
		
		cfg = new Configuration();
		//cfg.setServletContextForTemplateLoading(getServletContext(), "WEB-INF/templates");
		cfg.setClassForTemplateLoading(this.getClass(), "/");
		cfg.setTemplateUpdateDelay(0);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		cfg.setDefaultEncoding("UTF-8");//ISO-8859-1
		cfg.setOutputEncoding("UTF-8");//UTF-8
		cfg.setLocale(Locale.ENGLISH);
		System.out.println(classPath);
	}





	/**
	 * 读取属性配置文件
	 * @throws IOException 
	 */
	public void loadPropertyFile(String filePath) throws IOException{
		
		InputStream inStream = new BufferedInputStream (new FileInputStream(classPath+"/"+propertyName));		
		prop.load(inStream);
		
		WebRootPath = prop.getProperty("WebRootPath");
		moduleName = prop.getProperty("moduleName");
		voname =  prop.getProperty("vo_name");
		voFullName = prop.getProperty("vo_full_name");
		
		jspQueryListPageTempl = prop.getProperty("jspQueryListPageTempl");
		jspAddPageTempl = prop.getProperty("jspAddPageTempl");
		jspEditorPageTempl = prop.getProperty("jspEditorPageTempl");
		
		classDaoHibernateTempl = prop.getProperty("ClassDaoHibernateTempl");
		classDaoJdbcTempl = prop.getProperty("ClassDaoJdbcTempl");
		classServiceTempl = prop.getProperty("ClassServiceTempl");
		classActionTempl = prop.getProperty("ClassActionTempl");
		classToolKitsTempl = prop.getProperty("ClassToolKitsTempl");
		//System.out.println(prop.get("web_jsp_path"));
	}
	
	/***
	 * 读取实体类的属性字段 封装到指定的对象中
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 */
	public void readClassProperty() throws SecurityException, ClassNotFoundException{
		
		Class tmpclass = Class.forName(voFullName);	
		Field[] f = tmpclass.getDeclaredFields();		
		for(Field fld : f){
			FieldAttribute fa = new FieldAttribute();
			fa.setFieldNo(fld.getName());
			fa.setFieldType(fld.getType().toString());
			
			classProField.add(fa);
			Annotation[] annotation = fld.getDeclaredAnnotations();
			for(Annotation atn : annotation){
//				TypeVariable[] type =atn.annotationType().getTypeParameters();
				fieldDesc vo = (fieldDesc)atn;
				fa.setFieldName(vo.fldDesc());
				fa.setIsNeedVerify(vo.isNeedVerify());
//				System.out.println(vo.fldDesc());
			}
			//System.out.println(fld.getName());
		}
		
	}
	//--------------------------------生成JSP页面---------------------------------------------
	/**
	 * 生成页面文件
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public void createTemplFile() throws IOException, TemplateException{
		Writer out = null;
		String preJsppath = "WEB-INF/content/"+moduleName+"/";
		File newF = new File(WebRootPath+preJsppath);
		if(!newF.exists()){
			newF.mkdir();
		}
		
		//配置列表页面的数据
		Map rootMap=new HashMap();		
		rootMap.put("classProField", classProField);
		rootMap.put("voname", voname);
		rootMap.put("vonameF", changeFirstCharToupper(voname));
		rootMap.put("moduleName", moduleName);
		rootMap.put("moduleNameLower", moduleName.toLowerCase());
		Template tpl = null;
		//生成list Page
		if(prop.getProperty("isCreateListJsp").equals("0")){
			out = createWriterObj(WebRootPath +preJsppath+voname+"QueryList.jsp");
			tpl = cfg.getTemplate(jspQueryListPageTempl);
			tpl.setEncoding("UTF-8");
			tpl.process(rootMap, out);
			System.out.println("-----------List JSP文件生成成功---------------");
		}
		
		//生成添加页面
		if(prop.getProperty("isCreateAddJsp").equals("0")){
		out = createWriterObj(WebRootPath +preJsppath+"Add"+changeFirstCharToupper(voname)+".jsp");
		tpl = cfg.getTemplate(jspAddPageTempl);
		tpl.process(rootMap, out);
		System.out.println("-----------ADD JSP文件生成成功---------------");
		}
		
		//生成修改页面
		if(prop.getProperty("isCreateEditorJsp").equals("0")){
		out = createWriterObj(WebRootPath +preJsppath+"Editor"+changeFirstCharToupper(voname)+".jsp");
		tpl = cfg.getTemplate(jspEditorPageTempl);
		tpl.process(rootMap, out);
		System.out.println("-----------EDITOR JSP文件生成成功---------------");
		}
	}
	
	
	/**
	 * 创建jsp文件
	 */
	public Writer createWriterObj(String fileName)throws IOException{
		String outputfileName = fileName;// classPath
		File file = new File(outputfileName);
//		if(!file.isFile()){
//			file.createNewFile();
//		}
		OutputStream outstream = new FileOutputStream(file);
		Writer out = new OutputStreamWriter(outstream,"UTF-8");
		
		return out;
	}
	//------------------------------以下是生成类 begin---------------------------------------
	/**
	 * 生成类文件的目录结构
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public void createClassPathDir() throws IOException, TemplateException{
		File webRootFile = new File(WebRootPath);
		File bfjFile = webRootFile.getParentFile();
		String templateDir = "/src_"+moduleName+"/com/bplow/look/"+moduleName.toLowerCase()+"/";
		
		String bfjDir = bfjFile.getAbsolutePath();
		File daoDirFile = new File(bfjDir+templateDir+"dao");
		if(!daoDirFile.exists()){
			daoDirFile.mkdir();
		}
		daoDirFile = new File(bfjDir+templateDir+"service");
		if(!daoDirFile.exists()){
			daoDirFile.mkdir();
		}
		daoDirFile = new File(bfjDir+templateDir+"utils");
		if(!daoDirFile.exists()){
			daoDirFile.mkdir();
		}
		daoDirFile = new File(bfjDir+templateDir+"web");
		if(!daoDirFile.exists()){
			daoDirFile.mkdir();
		}
		
		//dao class file
		Writer out = null;
		Map rootMap=new HashMap();
		Template tpl = null;
		rootMap.put("moduleName", moduleName);
		rootMap.put("moduleNameLower", moduleName.toLowerCase());
		rootMap.put("moduleNameAction", changeFirstCharToupper(moduleName.toLowerCase()));
		rootMap.put("moduleNameF", changeFirstCharToupper(moduleName));
		rootMap.put("voname", changeFirstCharToupper(voname));
		rootMap.put("vonameF", voname);
		rootMap.put("voFullName", voFullName);
		
		if(prop.getProperty("isCreateDaoHibernateClass").equals("0")){
		out = createWriterObj(bfjDir+templateDir+"dao/"+changeFirstCharToupper(moduleName)+"Hibernate.java");
		tpl = cfg.getTemplate(classDaoHibernateTempl);
		tpl.setEncoding("UTF-8");
		tpl.process(rootMap, out);
		System.out.println("-----------dao 文件生成成功---------------");
		}
		
		if(prop.getProperty("isCreateDaoJdbcClass").equals("0")){
		out = createWriterObj(bfjDir+templateDir+"dao/"+changeFirstCharToupper(moduleName)+"JdbcDao.java");
		tpl = cfg.getTemplate(classDaoJdbcTempl);
		tpl.setEncoding("UTF-8");
		tpl.process(rootMap, out);
		System.out.println("-----------jdbc 文件生成成功---------------");
		}
		
		if(prop.getProperty("isCreateServiceClass").equals("0")){
		out = createWriterObj(bfjDir+templateDir+"service/"+changeFirstCharToupper(moduleName)+"Service.java");
		tpl = cfg.getTemplate(classServiceTempl);
		tpl.setEncoding("UTF-8");
		tpl.process(rootMap, out);
		System.out.println("-----------service 文件生成成功---------------");
		}
		if(prop.getProperty("isCreateActionClass").equals("0")){
		out = createWriterObj(bfjDir+templateDir+"web/"+changeFirstCharToupper(moduleName)+"Action.java");
		tpl = cfg.getTemplate(classActionTempl);
		tpl.setEncoding("UTF-8");
		tpl.process(rootMap, out);
		System.out.println("-----------action 文件生成成功---------------");
		}
		if(prop.getProperty("isCreateToolkitsClass").equals("0")){
		out = createWriterObj(bfjDir+templateDir+"utils/"+"Tooltiks.java");
		tpl = cfg.getTemplate(classToolKitsTempl);
		tpl.setEncoding("UTF-8");
		tpl.process(rootMap, out);
		System.out.println("-----------toolkits 文件生成成功---------------");
		}
		
		System.out.println("查询列表页面URL:"+moduleName+"/"+moduleName.toLowerCase()+"!"+moduleName+"ListWeb.action");
		//System.out.println(bfjDir);
		
	}
	
	
	
	
	
	//--------------------------------------以下是生成配置-----------------------------------------------------\\
	//-------------------------------------------------------------------------------------------------------\\
	/**
	 * 生成hibernate 配置文件
	 * @throws IOException 
	 */
	public void createHibernateConfigFile() throws IOException{
		Document document = structureHibernateConfDocument();
		Element sessionE = document.getRootElement().element("session-factory");
		File configfile = new File(WebRootPath+"WEB-INF/framework/hibernate-config/hibernate."+moduleName+".cfg.xml");//实体类的存放路径	
		String preVoPath = StringUtils.substringBeforeLast(voFullName, ".").replace(".", "/");//实体类在Class文件下的路径
		//给document添加实体类映射元素
		String voEntityDir =classPath+preVoPath;
		File tmpfile = new File(voEntityDir);
		if(tmpfile.isDirectory()){
			File[] voFile = tmpfile.listFiles();
			for(File voEntityfile : voFile){
				
				if(voEntityfile.getName().indexOf("hbm")>0){
					Element element = sessionE.addElement("mapping");
					element.setAttributeValue("resource",preVoPath+"/"+ voEntityfile.getName());
				}
			}
		}
		
		
		saveDocumentToFile(document,configfile);
		System.out.println("-----------hibernate文件生成成功---------------");
	}
	
	/**
	 * 生成hibernate 配置文件
	 * @throws IOException 
	 */
	public void createSpringConfigFile() throws IOException{
		Document document = structureSpringConfDocument();
		
		File configfile = new File(WebRootPath+"WEB-INF/framework/spring-config/applicationContext-"+moduleName+".xml");//实体类的存放路径	
				
		saveDocumentToFile(document,configfile);
		System.out.println("-----------hibernate文件生成成功---------------");
	}
	
	
	
	/**
	 * 定义hibernate配置文件 document
	 */
	public Document structureHibernateConfDocument(){
		
		Document document = DocumentHelper.createDocument();
		document.addDocType("hibernate-configuration", "-//Hibernate/Hibernate Configuration DTD 3.0//EN", 
				"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd");
		Element rootElement =document.addElement("hibernate-configuration");
		Element sessionfactoryElement =rootElement.addElement("session-factory");
		
		return document;

	}
	
	/**
	 * 定义spring配置文件 document
	 * @throws UnsupportedEncodingException 
	 */
	public Document structureSpringConfDocument() throws UnsupportedEncodingException{
		
		Document document = DocumentHelper.createDocument();
		Element rootElement =document.addElement("beans");
		rootElement.addAttribute("xmlns", "http://www.springframework.org/schema/beans").
		            addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance").
		            addAttribute("xsi:schemaLocation","http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd")
		            .addAttribute("default-lazy-init","true");
		Element descElement =rootElement.addElement("description");
		descElement.setText("");
		Element beanE = rootElement.addElement("bean");
		beanE.addAttribute("id", changeFirstCharLower(moduleName+"JdbcDao")).addAttribute("class", "com.bplow.look."+moduleName.toLowerCase()+".dao."+changeFirstCharToupper(moduleName)+"JdbcDao").addAttribute("parent", "baseJdbcDao");
		Element beanHibernateE = rootElement.addElement("bean");
		beanHibernateE.addAttribute("id", changeFirstCharLower(moduleName+"Hibernate")).
		   addAttribute("class", "com.bplow.look."+moduleName.toLowerCase()+".dao."+changeFirstCharToupper(moduleName+"Hibernate"));
		Element beanServiceE = rootElement.addElement("bean");
		beanServiceE.addAttribute("id", changeFirstCharLower(moduleName+"Service")).
		   addAttribute("class", "com.bplow.look."+moduleName.toLowerCase()+".service."+changeFirstCharToupper(moduleName+"Service"));
		return document;

	}
	
	
	
	/**
	 * 生成xml文件
	 * @throws IOException 
	 */
	public void saveDocumentToFile(Document document ,File file) throws IOException{
		// 美化格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		//document.setXMLEncoding("UTF-8");
		XMLWriter output = new XMLWriter(new FileWriter(file), format);
        output.write(document);
        output.close();

	}
	
	/*
	 * 将字符串首字母改为大写
	 */
	public String changeFirstCharToupper(String str){
//		byte tmp[]= str.getBytes();
		char charArray[] = new char[str.length()];
		char dst[] =new char[str.length()];
		str.getChars(0, str.length() , charArray, 0);
		String.valueOf(charArray[0]).toUpperCase().getChars(0, 1, dst, 0);
		charArray[0] = dst[0];
		
		
		return String.valueOf(charArray);
	}
	/*
	 * 将字符串首字母改为小写
	 */
	public String changeFirstCharLower(String str){
//		byte tmp[]= str.getBytes();
		char charArray[] = new char[str.length()];
		char dst[] =new char[str.length()];
		str.getChars(0, str.length() , charArray, 0);
		String.valueOf(charArray[0]).toLowerCase().getChars(0, 1, dst, 0);
		charArray[0] = dst[0];
				
		return String.valueOf(charArray);
	}
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		String classpath = null;
		StartHere sh = new StartHere();
		try {
			sh.loadPropertyFile("");
			
			sh.readClassProperty();
			
			if(sh.prop.getProperty("isCreateJsp").equals("0") )sh.createTemplFile();
			
			//生成hibernate配置文件
			if(sh.prop.getProperty("isCreateHibernate").equals("0") )sh.createHibernateConfigFile();
			//生成spring配置文件
			if(sh.prop.getProperty("isCreateSpring").equals("0") )sh.createSpringConfigFile();
			
			sh.createClassPathDir();
			
			//System.out.println(sh.changeFirstCharToupper("wangXiaoLei"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
