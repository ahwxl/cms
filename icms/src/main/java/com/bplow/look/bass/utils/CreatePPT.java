package com.bplow.look.bass.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

//import org.apache.poi.hs
//import org.apache.poi.hslf.usermodel.SlideShow;

public class CreatePPT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*SlideShow ppt = new SlideShow();

	    //add first slide
	    Slide s1 = ppt.createSlide();

	    //add second slide
	    Slide s2 = ppt.createSlide();*/
	    
	    //save changes in a file
	    FileOutputStream out;
		try {
			out = new FileOutputStream("slideshow.ppt");
//			ppt.write(out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    

	}

}
