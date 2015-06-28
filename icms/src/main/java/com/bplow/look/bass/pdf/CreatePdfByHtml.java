package com.bplow.look.bass.pdf;


/**
 * 生成pdf文件 BY HTML
 * @author new
 *
 */

public class CreatePdfByHtml {


	    public static void main(String[] args) throws Exception {   
//	        String inputFile = "D:/users-guide-R8.html";
//	        String url = new File(inputFile).toURI().toURL().toString();   
//	        String outputFile = "d:/users-guide-R8.pdf";
//	        OutputStream os = new FileOutputStream(outputFile);
//	        
//	        ITextRenderer renderer = new ITextRenderer();
//	        renderer.setDocument(url);
//	        
//	        // 解决中文支持问题   
//	        ITextFontResolver fontResolver = renderer.getFontResolver();   
//	        fontResolver.addFont("C:\\windows\\Fonts\\ARIAL.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);   
//
//	        
//	        //解决图片的相对路径问题   
//	        renderer.getSharedContext().setBaseURL("file:/D:");
//	        
//	        renderer.layout();
//	        renderer.createPDF(os);
//
//	        os.close();
	    	String str ="100,101,102";
	    	
	    	String[] seedArray = str.split(",");
			int i = 0;
			Integer tmpaimValue = Math.abs(10*1/seedArray.length);
			double endVale = 0d;
			if(seedArray.length == 1){
				endVale = tmpaimValue*0.1;
			}else{
				endVale = 1-tmpaimValue*0.1*(seedArray.length - 1);
			}
	    	
	    	for(String a : seedArray){
	    		System.out.println(1-tmpaimValue*0.1);
	    		
	    	}
	    	
	    	//System.out.println(System.currentTimeMillis());
	    	
	    	//System.out.println(StringUtils.substringAfter("my__OVER aaa bbb","OVER"));
	    	
	        //System.out.println(StringUtils.substringBeforeLast("my asas1order", "order1"));
	    }

}
