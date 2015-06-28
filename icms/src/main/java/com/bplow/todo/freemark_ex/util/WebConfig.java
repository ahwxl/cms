package com.bplow.todo.freemark_ex.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;



public class WebConfig extends WebMvcConfigurerAdapter  {
	
	
	  @Override
	  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		  System.out.println("------------------------------");
	      //Configure the list of HttpMessageConverters to use
		  List supportedList = new ArrayList<MediaType>();
		  supportedList.add(new MediaType("text", "html", Charset.forName("UTF-8")));
		  supportedList.add(new MediaType("application", "x-www-form-urlencoded", Charset.forName("UTF-8")) );//application/x-www-form-urlencoded
		  //supportedList.add(new MediaType("application", "json", Charset.forName("UTF-8")) );//application/json
		  StringHttpMessageConverter stringHm = new StringHttpMessageConverter();
		  stringHm.setSupportedMediaTypes(supportedList);
		  
//		  MappingJacksonHttpMessageConverter jsonMc = new MappingJacksonHttpMessageConverter();
		  List JsonSupportedList = new ArrayList<MediaType>();
		  JsonSupportedList.add(new MediaType("application", "json", Charset.forName("UTF-8")));
		  ///jsonMc.setSupportedMediaTypes(JsonSupportedList);
		  
		  
		  converters.add(stringHm);
		  //converters.add(jsonMc);
		  //converters.add(new org.springframework.http.converter.json.MappingJacksonHttpMessageConverter());
		 // converters.get(0).getSupportedMediaTypes().add(new MediaType("text", "html", Charset.forName("UTF-8")) );
		  //converters.get(1).getSupportedMediaTypes().add(new MediaType("application", "x-www-form-urlencoded", Charset.forName("UTF-8")) );
	  }

}
