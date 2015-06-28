package com.bplow.base.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SendSms {


	
    public static final Log logger = LogFactory.getLog(com.bplow.base.util.SendSms.class);

	/**
	 * 短信发送(及时带端口)
	 * @param userList
	 * @param smsContext
	 * @param smsContext
	 * @return 
	 */
	public  static int sendSms(String userPhone,String smsContext,String port)
	{
		int rec = sendRequest(userPhone+","+port+","+smsContext);
		return rec;
	}
	
	
	public static int sendRequest(String strXml)
    {
		logger.info("<发送开启>短信发送:"+strXml);
        HttpURLConnection c = null;
        try
        {
            URL url=new URL("http://10.97.177.119//WangService/SendSMS.aspx");
            c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setDoInput(true);
            c.connect();
            PrintWriter out = new PrintWriter(new OutputStreamWriter(c.getOutputStream(),"gb2312"));//发送数据
            out.print(strXml);
            out.flush();
            out.close();
            String header;
            for(int i=0;true;i++){
                header=c.getHeaderField(i);
                if(header==null)break;
            }
            //接收
            int rec = 0;
            rec = c.getResponseCode();
            logger.info("请求短信状态:"+rec);
            if(rec==200)
            {
            	logger.info("连接成功");
            	InputStream u = c.getInputStream();
            	BufferedReader in = new BufferedReader(new InputStreamReader(u));
            	String line = "";
            	while ((line = in.readLine())!=null)
            	{
            		logger.info("请求返回:"+line);
            	}
            }
            //接收  
            c.disconnect();
            return rec;
        }catch (Exception e) {
             e.printStackTrace();
             return 0;
        }
    }
}
