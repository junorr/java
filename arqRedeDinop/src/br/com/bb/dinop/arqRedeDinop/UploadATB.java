package br.com.bb.dinop.arqRedeDinop;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import br.com.bb.dinop.action.ActionRouter;
import br.com.bb.dinop.base.BaseCmdo;
import br.com.bb.dinop.arqRedeDinop.util.RemoveCaracteresEstendidos;
import br.com.bb.sso.api.bean.Usuario;

/**
 * @author F9087547
 *
 */
public class UploadATB extends BaseCmdo {

	/**
	 * 
	 */

	public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		System.out.println("|==>> UploadATB: Enter... <<==|");
		HttpServletRequest httpServletRequest = (HttpServletRequest) req;
		HttpSession httpSession = httpServletRequest.getSession(true);
		Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
		try{
			
			final boolean isMultipart = ServletFileUpload.isMultipartContent(req);
			if (isMultipart) {
				System.out.println("|==>> UploadATB: is multipart... <<==|");
				this.destPage = "boletins.uploadSrc.jsp";
				final DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1024 * 5120);
				factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
				final ServletFileUpload upload = new ServletFileUpload(factory);
				final List<FileItem> items = upload.parseRequest(req);
				upload.setSizeMax(1024 * 5120);
				final Iterator<FileItem> iter = items.iterator();
				byte[] data = {};
				String nomeArquivo = "";
				String tipo = "";
				int imgWidth = 0;
				int imgTypeSize = 0;
				int imgHeight = 0;
				double arqSize = 0;
				while (iter.hasNext()) {
					final FileItem item = (FileItem) iter.next();
					System.out.println("|==>> UploadATB: FileItem ["+ item+"]... <<==|");
					if (!item.isFormField()) {
						System.out.println("|==>> UploadATB: is file... <<==|");
						if(item.getName() != null){
							nomeArquivo = item.getName();
							RemoveCaracteresEstendidos removeCaracteresEstendidos = new RemoveCaracteresEstendidos();
							nomeArquivo = removeCaracteresEstendidos.replaceSpecial(nomeArquivo);
						}
						System.out.println("|==>> UploadATB: fileName ["+ nomeArquivo+"]... <<==|");
					}
				}//while
			}
		} catch (final Exception exception) {
			throw new ServletException(exception);
		}
		return new ActionRouter(this.destPage, true, true, this.idPagina, usuario);
	}
}