package br.com.bb.disec.aplic.html;

public class H extends AbstractHtmlElement {

	public static final String NAME = "h";
	
	private int size;
	
	
	public H(int size) {
		super(NAME + size, false);
		if(size < 1 || size > 9)
			throw new IllegalArgumentException("Invalid Size for H[1-9] tag: "+ size);
		this.size = size;
	}
	
}
