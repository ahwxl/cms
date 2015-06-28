// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SimplePostTool.java

package com.bplow.look.bass.utils;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * post http 提交文件到端口
 * 提交搜索内容xm文件
 * @author new
 *
 */

public class SimplePostTool
{
	public static final String DEFAULT_POST_URL = "http://localhost:8983/solr/update";
    public static final String POST_ENCODING = "UTF-8";
    public static final String VERSION_OF_THIS_TOOL = "1.2";
    private static final String SOLR_OK_RESPONSE_EXCERPT = "<int name=\"status\">0</int>";
    private static final String DEFAULT_COMMIT = "yes";
    private static final String DATA_MODE_FILES = "files";
    private static final String DATA_MODE_ARGS = "args";
    private static final String DATA_MODE_STDIN = "stdin";
    private static final String DEFAULT_DATA_MODE = "files";
    private static final Set DATA_MODES;
    protected URL solrUrl;
	
	
    private class PostException extends RuntimeException
    {

        final SimplePostTool this$0;

        PostException(String reason, Throwable cause)
        {
            this$0 = SimplePostTool.this;
            //super((new StringBuilder()).append(reason).append(" (POST URL=").append(solrUrl).append(")").toString(), cause);
        }
    }


    public void doPost(String[] args) throws Exception
    {
        info("version 1.2");
        if(0 < args.length && "-help".equals(args[0]))
        {
            System.out.println("This is a simple command line tool for POSTing raw XML to a Solr\nport.  XML data can be read from files specified as commandline\nargs; as raw commandline arg strings; or via STDIN.\nExamples:\n  java -Ddata=files -jar post.jar *.xml\n  java -Ddata=args  -jar post.jar '<delete><id>42</id></delete>'\n  java -Ddata=stdin -jar post.jar < hd.xml\nOther options controlled by System Properties include the Solr\nURL to POST to, and whether a commit should be executed.  These\nare the defaults for all System Properties...\n  -Ddata=files\n  -Durl=http://localhost:8983/solr/update\n  -Dcommit=yes\n");
            return;
        }
        URL u = null;
        u = new URL("http://localhost:8383/solr/update");
        
        
        String mode = System.getProperty("data", "files");
        if(!DATA_MODES.contains(mode))
            fatal((new StringBuilder()).append("System Property 'data' is not valid for this tool: ").append(mode).toString());
        try
        {
            if("files".equals(mode))
            {
                if(0 < args.length)
                {
                    info((new StringBuilder()).append("POSTing files to ").append(u).append("..").toString());
                    int posted = postFiles(args, 0);
                }
            } else
            if("args".equals(mode))
            {
                if(0 < args.length)
                {
                    info((new StringBuilder()).append("POSTing args to ").append(u).append("..").toString());
                    String arr$[] = args;
                    int len$ = arr$.length;
                    for(int i$ = 0; i$ < len$; i$++)
                    {
                        String a = arr$[i$];
                        StringWriter sw = new StringWriter();
                        postData(new StringReader(a), sw);
                        warnIfNotExpectedResponse(sw.toString(), "<int name=\"status\">0</int>");
                    }

                }
            } else
            if("stdin".equals(mode))
            {
                info((new StringBuilder()).append("POSTing stdin to ").append(u).append("..").toString());
                StringWriter sw = new StringWriter();
                postData(new InputStreamReader(System.in, "UTF-8"), sw);
                warnIfNotExpectedResponse(sw.toString(), "<int name=\"status\">0</int>");
            }
            if("yes".equals(System.getProperty("commit", "yes")))
            {
                info("COMMITting Solr index changes..");
                StringWriter sw = new StringWriter();
                commit(sw);
                warnIfNotExpectedResponse(sw.toString(), "<int name=\"status\">0</int>");
            }
        }
        catch(IOException ioe)
        {
            fatal((new StringBuilder()).append("Unexpected IOException ").append(ioe).toString());
        }
    }

    int postFiles(String args[], int startIndexInArgs)
        throws IOException
    {
        int filesPosted = 0;
        for(int j = startIndexInArgs; j < args.length; j++)
        {
            File srcFile = new File(args[j]);
            StringWriter sw = new StringWriter();
            if(srcFile.canRead())
            {
                info((new StringBuilder()).append("POSTing file ").append(srcFile.getName()).toString());
                postFile(srcFile, sw);
                filesPosted++;
                warnIfNotExpectedResponse(sw.toString(), "<int name=\"status\">0</int>");
            } else
            {
                warn((new StringBuilder()).append("Cannot read input file: ").append(srcFile).toString());
            }
        }

        return filesPosted;
    }

    static void warnIfNotExpectedResponse(String actual, String expected)
    {
        if(actual.indexOf(expected) < 0)
            warn((new StringBuilder()).append("Unexpected response from Solr: '").append(actual).append("' does not contain '").append(expected).append("'").toString());
    }

    static void warn(String msg)
    {
        System.err.println((new StringBuilder()).append("SimplePostTool: WARNING: ").append(msg).toString());
    }

    static void info(String msg)
    {
        System.out.println((new StringBuilder()).append("SimplePostTool: ").append(msg).toString());
    }

    static void fatal(String msg)
    {
        System.err.println((new StringBuilder()).append("SimplePostTool: FATAL: ").append(msg).toString());
        System.exit(1);
    }

    public SimplePostTool(URL solrUrl)
    {
        this.solrUrl = solrUrl;
        warn("Make sure your XML documents are encoded in UTF-8, other encodings are not currently supported");
    }

    public void commit(Writer output)
        throws IOException
    {
        postData(new StringReader("<commit/>"), output);
    }

    public void postFile(File file, Writer output)
        throws FileNotFoundException, UnsupportedEncodingException
    {
        Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
        try
        {
            postData(reader, output);
        }
        finally
        {
            try
            {
                if(reader != null)
                    reader.close();
            }
            catch(IOException e)
            {
                throw new PostException("IOException while closing file", e);
            }
        }
    }

    public void postData(Reader data, Writer output)
    {
        HttpURLConnection urlc = null;
        try
        {
            urlc = (HttpURLConnection)solrUrl.openConnection();
            try
            {
                urlc.setRequestMethod("POST");
            }
            catch(ProtocolException e)
            {
                throw new PostException("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
            }
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
            OutputStream out = urlc.getOutputStream();
            try
            {
                Writer writer = new OutputStreamWriter(out, "UTF-8");
                pipe(data, writer);
                writer.close();
            }
            catch(IOException e)
            {
                throw new PostException("IOException while posting data", e);
            }
            finally
            {
                if(out != null)
                    out.close();
            }
            InputStream in = urlc.getInputStream();
            try
            {
                Reader reader = new InputStreamReader(in);
                pipe(reader, output);
                reader.close();
            }
            catch(IOException e)
            {
                throw new PostException("IOException while reading response", e);
            }
            finally
            {
                if(in != null)
                    in.close();
            }
        }
        catch(IOException e)
        {
            try
            {
                fatal((new StringBuilder()).append("Solr returned an error: ").append(urlc.getResponseMessage()).toString());
            }
            catch(IOException f) { }
            fatal((new StringBuilder()).append("Connection error (is Solr running at ").append(solrUrl).append(" ?): ").append(e).toString());
        }
        finally
        {
            if(urlc != null)
                urlc.disconnect();
        }
    }

    private static void pipe(Reader reader, Writer writer)
        throws IOException
    {
        char buf[] = new char[1024];
        for(int read = 0; (read = reader.read(buf)) >= 0;)
            writer.write(buf, 0, read);

        writer.flush();
    }

    static 
    {
        DATA_MODES = new HashSet();
        DATA_MODES.add("files");
        DATA_MODES.add("args");
        DATA_MODES.add("stdin");
    }
    
    
    
    public static void main(String[] args) {
    	try {
			//SimplePostTool simplePostTool = new SimplePostTool(new URL("http://localhost:8383/solr/update"));			
			//simplePostTool.doPost(new String[]{"D:/out_to_local/open_project/apache-solr-1.4.1/example/exampledocs/solr.xml"});
			
    		HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://localhost:8080" +
                    "/servlets-examples/servlet/RequestInfoExample");
			
            //HttpResponse response = httpclient.execute(httppost);
            //HttpEntity resEntity = response.getEntity();
			
			
			
			
			
			
			
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
    
    
    
    
    
}
