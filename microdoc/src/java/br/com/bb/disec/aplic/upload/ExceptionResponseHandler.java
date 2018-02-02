package br.com.bb.disec.aplic.upload;

import br.com.bb.disec.aplic.html.H;
import br.com.bb.disec.aplic.html.HtmlAppender;
import br.com.bb.disec.aplic.html.HtmlElement;
import br.com.bb.disec.aplic.html.Style;
import br.com.bb.disec.aplic.html.UL;
import java.awt.Color;


public class ExceptionResponseHandler implements UploadHandler {

	private final Exception exc;
	
	private final String msg;
	
	
	public ExceptionResponseHandler(Exception exc) {
		this(exc, null);
	}
	
	
	public ExceptionResponseHandler(Exception exc, String msg) {
		if(exc == null) {
			throw new IllegalArgumentException("Invalid Exception to generate response: "+ exc);
		}
		this.exc = exc;
		this.msg = (msg != null ? msg : exc.getMessage());
	}
	
	
	public Exception getException() {
		return exc;
	}
	
	
	public String getMessage() {
		return msg;
	}
	
	
	@Override
	public HtmlElement handle() {
		HtmlAppender ap = new HtmlAppender();
		ap.append(new H(3).setStyle(new Style().setColor(Color.RED)).add(msg))
			.append(new H(4).add(exc.toString()));
		StackTraceElement[] elts = exc.getStackTrace();
		UL ul = new UL();
		for(StackTraceElement elt : elts) {
			ul.add(elt.toString());
		}
		return ap.append(ul).appendHidden("success", String.valueOf(Boolean.FALSE));
	}

}
