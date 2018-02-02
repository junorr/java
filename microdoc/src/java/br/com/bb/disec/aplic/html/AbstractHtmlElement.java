package br.com.bb.disec.aplic.html;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractHtmlElement implements HtmlElement {

	protected String tagname;
  
  protected String id;
	
	protected boolean selfClosed;
	
	protected Style style;
	
	protected List<HtmlElement> content;
  
  protected Map<String,String> attrs;
	
	
	protected AbstractHtmlElement() {
		content = new LinkedList<HtmlElement>();
    attrs = new HashMap<>();
	}
	
	
	protected AbstractHtmlElement(String name, boolean selfClosed) {
		this();
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("Invalid Tag Name: "+ name);
		this.tagname = name;
		this.selfClosed = selfClosed;
	}
	
	
	public AbstractHtmlElement add(String text) {
		if(text != null && !text.trim().isEmpty()) {
			content.add(new SimpleText(text));
		}
		return this;
	}
	
	
	public AbstractHtmlElement add(HtmlElement elt) {
		if(elt != null) {
			content.add(elt);
		}
		return this;
	}
  
  
  public AbstractHtmlElement addAttribute(String name, String value) {
    if(name != null && value != null) {
      attrs.put(name, value);
    }
    return this;
  }
	
	
	public String getTagName() {
		return tagname;
	}
  
  
  public String getID() {
    return id;
  }
  
  
  public AbstractHtmlElement setID(String id) {
    if(id != null) {
      this.id = id;
    }
    return this;
  }
	
	
	public boolean isSelfClosed() {
		return selfClosed;
	}
	
	
	public AbstractHtmlElement setStyle(Style stl) {
		style = stl;
		return this;
	}
	
	
	public Style getStyle() {
		return style;
	}
	
	
	protected List<HtmlElement> getContent() {
		return content;
	}
  
  
  protected Map<String,String> getAttributes() {
    return attrs;
  }
	
	
	protected AbstractHtmlElement setContent(List<HtmlElement> cont) {
		content = cont;
		return this;
	}
	
	
	public String toHtml() {
		StringBuilder sb = new StringBuilder()
				.append(TAG_START).append(tagname);
    if(id != null) {
      sb.append(ID)
          .append(QT)
          .append(id)
          .append(QT);
    }
		if(style != null) {
			sb.append(STYLE).append(QT)
					.append(style.toHtml())
					.append(QT);
		}
    if(!attrs.isEmpty()) {
      for(Entry<String,String> e : attrs.entrySet()) {
        sb.append(" ")
            .append(e.getKey())
            .append("=").append(QT)
            .append(e.getValue())
            .append(QT);
      }
    }
		if(!selfClosed) {
			sb.append(TAG_END);
			for(HtmlElement el : content) {
				sb.append(el.toHtml());
			}
			sb.append(TAG_CLOSE).append(tagname).append(TAG_END);
		}
		else {
			sb.append(TAG_SELF_CLOSE);
		}
		return sb.toString();
	}
	
}
