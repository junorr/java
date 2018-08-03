package br.com.bb.disec.aplic.html;

public interface HtmlElement {

	public static final String
			TAG_END = ">",
			TAG_START = "<",
			TAG_CLOSE = "</",
			TAG_SELF_CLOSE = "/>",
			STYLE = " style=",
      ID = " id=",
			QT = "\"";
	
	
	public String toHtml();
	
}
