package br.com.bb.disec.aplic.upload;

import javax.servlet.ServletContext;

import br.com.bb.sso.bean.User;
import br.com.bb.sso.bean.Usuario;

public class UploadAccessHandler implements UploadHandler {

	public static final String 
			UPLOAD_ALLOWED_UORS = "upload_allowed_uors",
			UPLOAD_ALLOWED_CHAVES = "upload_allowed_chaves",
			UPLOAD_ALLOWED_CMSS = "upload_allowed_cmss";


	private User usr;
	
	private ServletContext cxt;
	
	
	public UploadAccessHandler(User usr, ServletContext cxt) {
		if(usr == null) {
			throw new IllegalArgumentException("Invalid user: "+ usr);
		}
		if(cxt == null) {
			throw new IllegalArgumentException("Invalid context: "+ cxt);
		}
		this.usr = usr;
		this.cxt = cxt;
	}
	
	
	private boolean checkForUor() {
		String uors = cxt.getInitParameter(UPLOAD_ALLOWED_UORS);
		return uors == null 
        || uors.contains(String.valueOf(usr.getUorDepe()))
        || uors.contains(String.valueOf(usr.getUorEquipe()));
	}
	
	
	private boolean checkForComissao() {
		String cmss = cxt.getInitParameter(UPLOAD_ALLOWED_CMSS);
		return cmss == null
        || cmss.contains(String.valueOf(usr.getCodigoComissao()));
	}
	
	
	private boolean checkForChave() {
		String chaves = cxt.getInitParameter(UPLOAD_ALLOWED_CHAVES);
		return chaves == null
        || chaves.contains(usr.getChave());
	}
	
	
	@Override
	public Boolean handle() {
		return checkForUor()
			|| checkForComissao()
			|| checkForChave();
	}

}
