package br.com.bb.disec.aplic.html;

public class SimpleText implements HtmlElement {

	private String text;
	
	
	public SimpleText() {
		text = null;
	}
	
	
	public SimpleText(String txt) {
		text = txt;
	}
	
	
	public SimpleText setText(String txt) {
		text = txt;
		return this;
	}
	
	
	public String getText() {
		return text;
	}
	
	
	public String toHtml() {
		return text;
	}
	
}
