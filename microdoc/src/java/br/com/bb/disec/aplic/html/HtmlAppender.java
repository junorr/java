package br.com.bb.disec.aplic.html;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class HtmlAppender implements HtmlElement {

	private final List<HtmlElement> content;
	
	
	public HtmlAppender() {
		content = new LinkedList<>();
	}
	
	
	public HtmlAppender append(HtmlElement elt) {
		if(elt != null)
			content.add(elt);
		return this;
	}
	
	
	public HtmlAppender appendHidden(String name, Object value) {
		if(name != null && value != null) {
			Span span = new Span();
      span.setID(name);
			span.add(Objects.toString(value))
				.setStyle(new Style().addRaw("visibility", "hidden"));
			content.add(span);
		}
		return this;
	}
	
	
	public List<HtmlElement> content() {
		return content;
	}
	
	
	public String toHtml() {
		StringBuilder sb = new StringBuilder();
		if(content == null || content.isEmpty())
			return sb.toString();
		
		for(HtmlElement el : content) {
			sb.append(el.toHtml());
		}
		return sb.toString();
	}
	
}
