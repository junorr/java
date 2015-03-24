package br.com.bb.dinop.arqRedeDinop.util;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Mensagem {
	
	public static String getMensagem(boolean test){
		String msg = null;
		if(test){
			msg = "Operação concluída com sucesso";
		}else{
			msg = "Falha na operação. Favor, tente novamente";
		}
		return msg;
	}
	
	public static String getMensagem(int test){
		String msg = null;
		if(test == 1){
			msg = "Operação concluída com sucesso";
		}else{
			msg = "Falha na operação. Favor, tente novamente";
		}
		return msg;
	}
	
	public static String decode64(final String input) {
        String retorno = "";
        if(input == null){
        	return null;
        }
        try {
            final BASE64Decoder base64Decoder = new BASE64Decoder();
            final byte[] byteArray = base64Decoder.decodeBuffer(input);
            retorno = new String(byteArray, "UTF-8");
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }
	
	public static String encode64(final String input) {
        String retorno = "";
        if(input == null){
        	return null;
        }
        try {
            final BASE64Encoder base64Encoder = new BASE64Encoder();
            final byte[] byteArray = base64Encoder.encode(input.getBytes()).getBytes();
            retorno = new String(byteArray, "UTF-8");
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}

}
