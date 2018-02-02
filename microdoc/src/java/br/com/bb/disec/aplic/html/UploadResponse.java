package br.com.bb.disec.aplic.html;

import br.com.bb.disec.aplic.upload.FileSizeFormatter;

public class UploadResponse extends AbstractHtmlElement {
  
  public static final String DEFAULT_SUCCESS_MESSAGE = "Arquivo carregado com sucesso!";
  
  public static final String DEFAULT_FAIL_MESSAGE = "Falha ao carregar o arquivo!";
  

	private String message;
	
	private boolean success;
	
	private String filename;
  
  private long size;
	
	
	public UploadResponse() {
		message = null;
		filename = null;
    size = 0;
		success = false;
	}
	
	
	public UploadResponse(String msg, boolean sucs) {
		this();
		message = msg;
		sucs = success;
	}
	
	
	public String getMessage() {
		return message;
	}
	
	
	public String getFilename() {
		return filename;
	}
  
  
  public long getSize() {
    return size;
  }
	
	
	public boolean isSuccessful() {
		return success;
	}
	
	
	public UploadResponse setMessage(String msg) {
		message = msg;
		return this;
	}
	
	
	public UploadResponse setFilename(String fname) {
		filename = fname;
		return this;
	}
  
  
  public UploadResponse setSize(long size) {
    this.size = size;
    return this;
  }
	
	
	public UploadResponse setSuccessful(boolean sucs) {
		success = sucs;
		return this;
	}
	
	
  @Override
  public String toHtml() {
    Span outter = new Span();
    if(filename != null) {
      outter.setID(filename);
    }
    outter.addAttribute(
        "data-success", String.valueOf(success)
    );
    if(!content.isEmpty()) {
      outter.getContent().addAll(content);
    }
    else {
      if(message == null) {
        message = (success ? DEFAULT_SUCCESS_MESSAGE : DEFAULT_FAIL_MESSAGE);
      }
      outter.add(new H(4).add(message));
      UL ul = new UL();
      if(filename != null) {
        Span it = new Span();
        it.add(new Span().setStyle(new Style().setFontBold()).add("Arquivo: "));
        it.add(filename);
        ul.add(it);
      }
      if(size > 0) {
        Span it = new Span();
        it.add(new Span().setStyle(new Style().setFontBold()).add("Tamanho: "));
        it.add(new FileSizeFormatter().format(size));
        ul.add(it);
      }
      if(!ul.getContent().isEmpty()) {
        outter.add(ul);
      }
    }
    return outter.toHtml();
  }
  
	
	
	public static void main(String[] args) {
		UploadResponse resp = new UploadResponse();
		resp.setFilename("picture.jpg")
        .setSize(1000000)
        .setSuccessful(true);
    System.out.println("* html: \n"+ resp.toHtml());
	}
	
}
