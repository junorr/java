package br.com.bb.disec.aplic.html;

public class UL extends AbstractHtmlElement {

	public static final String 
			NAME = "ul",
			ITEM = "li";
	
	
	public UL() {
		super(NAME, false);
	}
  
  
  @Override
  public UL add(HtmlElement he) {
    if(he != null) {
      content.add(new GenericHtmlElement(ITEM, false).add(he));
    }
    return this;
  }
	
	
  @Override
  public UL add(String text) {
    if(text != null) {
      content.add(new GenericHtmlElement(ITEM, false).add(text));
    }
    return this;
  }
	
}
