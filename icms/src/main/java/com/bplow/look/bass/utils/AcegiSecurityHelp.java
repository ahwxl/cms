package com.bplow.look.bass.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.bplow.look.bass.DateUtil;
import com.google.common.collect.ObjectArrays;


public class AcegiSecurityHelp {

	private String syssalt = "/a/";
	/*
	 * 生成6位的验证码
	 */
	public static String getAccreditCode(){
		byte tmp [] = {1,2,3};
		Random random = new Random();
		random.nextBytes(tmp);
		return new String(Hex.encodeHex(tmp));
	}
	
	public String decodeStr(String str) throws DecoderException{
		//MD5.toMD5("");
		String encodeStr = "";
		/*Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		encodeStr = md5.encodePassword(str, "/a/");
		System.out.println(encodeStr);
		
		byte []data1 = {3,123};
		char []data = "69a3c825188cb38b9fdf23da32421ed4".toCharArray();
		System.out.println(Hex.encodeHex(data1));
		System.out.println(Hex.decodeHex(data));*/
		
		return null;
	}
	
	/**
	 * 加密字符窜
	 */
	public String encodeStr(String str){
		
		/*Md5PasswordEncoder md5 = new Md5PasswordEncoder();*/
		return /*md5.encodePassword(str, syssalt)*/null;
	}
	
	/**
	 * @param args
	 * @throws DecoderException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws DecoderException, IOException {
		AcegiSecurityHelp vo = new AcegiSecurityHelp();
		vo.decodeStr("123456");

		//System.out.println(getAccreditCode());
		
		String obj[] = new String[]{"aa","bb"};
		
		
		
		File f = new File("C:\\sms.sql");

		   if (f.exists()) {

		/** ///1 可以   
		* 
		* */
			FileInputStream fis = null;

		    fis = new FileInputStream(f);

		    System.out.println("File has " + fis.available()
		      + " bytes");
		    System.out.println("File has " + (double)((double)fis.available()/1000/1000)
		      + "M");
		    System.out.println("File has " + (double)(fis.available()/1024/1024)
		      + "M");

		    /** ///2 可以
		    * System.out.println("文件存在");
		    * System.out.println("文件大小为:"+(double)(f.length()/1024/1024)+"M");
		    */
		   } else {
		    f.createNewFile();
		    System.out.println("文件不存在");
		   }
		
		
		System.out.println((double)(15741/1024));
		
		String bb = "aa";
		String aa ="/x20aa";
		System.out.println(aa);
		
		
		try {
			System.out.println(DateUtil.strToDate("2010-08-18 16:40:60","yyyy-MM-dd HH:mm:ss").getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println((new Date()).getTime());
		System.out.println(System.currentTimeMillis());
		
		String a[] = new String[]{"1","2","3","4","5"};
		//a[5] = "wang";
		
		String [] b=ObjectArrays.concat("wang", a);
		for(Object tmp : b){
			System.out.println(tmp.toString());
		}
		
		
		
		
		
	}
}
