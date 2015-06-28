package com.bplow.look.bass.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;



public class CustomDateSerializer extends JsonSerializer<Date>{

	@Override
	public void serialize(Date mydate, JsonGenerator jg, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
        String formattedDate = formatter.format(mydate); 
        jg.writeString(formattedDate); 
	}

}
