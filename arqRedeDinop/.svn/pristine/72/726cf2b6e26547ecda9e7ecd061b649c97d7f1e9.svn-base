package br.com.bb.dinop.arqRedeDinop;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.bb.dinop.action.ActionRouter;
import br.com.bb.dinop.action.RequestParameters;
import br.com.bb.dinop.arqRedeDinop.beans.ArquivoBean;
import br.com.bb.dinop.arqRedeDinop.persistencia.ArquivoPersistencia;
import br.com.bb.dinop.arqRedeDinop.util.StreamUtils;
import br.com.bb.dinop.base.BaseCmdo;
import br.com.bb.sso.api.bean.Usuario;

public class ArquivoDownload extends BaseCmdo {

	public static final String
		DEST_FILETABLES = "arqRedeDinop.filetables.jsp",
		DEST_FILEERROR = "arqRedeDinop.fileerror.jsp",
		CONTENT_APP_OSTREAM = "application/octet-stream",
		HEADER_CONTENT_DISP = "Content-Disposition",
		HEADER_CONTENT_ATTACH = "attachment; filename=\"";
	
	
	public ArquivoDownload() {
		super();
		this.transacaoAcesso = "arqRedeDinop.conteudo";
	}
	
	
	@Override
	public ActionRouter perform(final HttpServlet servlet, final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
        HttpSession session = req.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
		try {
			final RequestParameters rp = getParameterMap(req);
			int arqId = rp.getIntParm("arqId");
			String label = rp.getStringParm("label");
			
			String uorstr = usuario.getCodigoUorDependencia();
			long uor = -1;
			try {
				uor = Long.parseLong(uorstr);
			} catch(NumberFormatException e) {
				e.printStackTrace();
				throw new ServletException("Cod UOR inválido (uor="+ uorstr+ ")");
			}
			
			int cdCmss = toInt(usuario.getCodigoComissao());
			int cdPrf = toInt(usuario.getPrefixoDependencia());

			ArquivoPersistencia ap = new ArquivoPersistencia(label);
			//Recupera os dados do arquivo do banco
			ArquivoBean arq = ap.getArquivoById(arqId);
			
			//Se o arquivo não existir, redireciona para a página de erro
			if(arq == null) {
				return new ActionRouter("arqRedeDinop.semacesso.jsp", true, true, this.idPagina, usuario);
			}
			else {
				//Efetua o download via ServletResponse
				File file = new File(arq.getLink());
				res.setContentType(CONTENT_APP_OSTREAM);
				res.setContentLength((int)file.length());
				res.setHeader(HEADER_CONTENT_DISP, HEADER_CONTENT_ATTACH+ arq.getNome()+ "\"");
				InputStream is = StreamUtils.getInputStream(file);
				OutputStream os = res.getOutputStream();
				StreamUtils.transferAndClose(is, os);
				res.flushBuffer();
				
				//Efetua log de tentativa de download para o arquivo
				ap.insertArquivoLog(arqId, usuario.getChave(), cdCmss, cdPrf, uor);
			}
		} 
		catch (final Exception exception) {
			exception.printStackTrace();
			throw new ServletException(exception.getMessage());
		}
		return null;
	}
	
	
	private int toInt(String str) {
		if(str == null || str.isEmpty())
			return 0;
		try {
			return Integer.parseInt(str);
		} catch(NumberFormatException e) {
			return 0;
		}
	}

}
