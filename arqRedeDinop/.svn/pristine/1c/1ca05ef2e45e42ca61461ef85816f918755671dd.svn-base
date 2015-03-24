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
public class Upload extends BaseCmdo {

	/**
	 * 
	 */

	public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) req;
		HttpSession httpSession = httpServletRequest.getSession(true);
		Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
		try{
			
			final boolean isMultipart = ServletFileUpload.isMultipartContent(req);
			if (isMultipart) {
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
					if(item.getName() != null){
						nomeArquivo = item.getName();
						RemoveCaracteresEstendidos removeCaracteresEstendidos = new RemoveCaracteresEstendidos();
						nomeArquivo = removeCaracteresEstendidos.replaceSpecial(nomeArquivo);
					}
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("tipo")) {
							tipo = item.getString();
						}
						if (item.getFieldName().equalsIgnoreCase("imgTypeSize")) {
							imgTypeSize = Integer.parseInt(item.getString());
						}
					}
					else {
						if (item.getFieldName().equalsIgnoreCase("fileFoto") || item.getFieldName().equalsIgnoreCase("fileAnx")) {
							data = item.get();
							arqSize = item.getSize()/1024;
							if(nomeArquivo.toUpperCase().contains(".JPG") || nomeArquivo.toUpperCase().contains(".JPEG") || nomeArquivo.toUpperCase().contains(".PNG") || nomeArquivo.toUpperCase().contains(".GIF")) {
								InputStream is = item.getInputStream();
								try {
								  Image image = ImageIO.read(is);  
								  imgWidth = image.getWidth(null);  
								  imgHeight = image.getHeight(null);  
								}
								catch (IOException ioe) {
								  System.out.println(ioe);
								}
							}
						}
					}
				}
				if (tipo.equals("foto")) {				
					if(nomeArquivo.toUpperCase().contains(".JPG")) {
						if (arqSize < 40) {	//Tamanho
							if ((imgWidth == 60 && imgHeight == 80)) {	//Dimensões
								String auxPath = servlet.getServletContext().getRealPath("");
								String fs = File.separator;
								String pathImagem = auxPath.substring(0, auxPath.lastIndexOf(fs)) + fs + "header2012"+fs+"images"+fs+"funcis"+fs+req.getSession(true).getAttribute("acesso.chave")+".jpg";
								
								FileOutputStream fos;
								try {
									fos = new FileOutputStream(pathImagem);
									fos.write(data);
									fos.flush();
									fos.close();
								}
								catch (FileNotFoundException fnfe) {
									System.out.println(fnfe);
								}
								catch (IOException ioe) {
									System.out.println(ioe);
								}
								req.setAttribute("statusUploadFile", "1");
								req.setAttribute("srcUploadFile", "/" + nomeArquivo);
								req.setAttribute("msgUploadFile", "Foto inserida com sucesso!");
								req.getSession().setAttribute("foto.img", nomeArquivo);
							}
							else {
								req.setAttribute("statusUploadFile", "0");
								req.setAttribute("srcUploadFile", "");
								req.setAttribute("msgUploadFile", "Dimensoes da imagem: "+imgWidth+"x"+imgHeight+" pixels.\nDimensoes permitidas: 60x80 pixels.\n\nFavor re-inserir a imagem com as dimensoes especificadas.");
							}
						}
						else {
							req.setAttribute("statusUploadFile", "0");
							req.setAttribute("srcUploadFile", "");
							req.setAttribute("msgUploadFile", "Tamanho da imagem: "+arqSize+"Kb.\nMaximo tamanho permitido: 40Kb.\n\nFavor re-inserir a imagem com o tamanho correto.");
						}
					}
					else {
						req.setAttribute("statusUploadFile", "0");
						req.setAttribute("srcUploadFile", "");
						req.setAttribute("msgUploadFile", "Extensao do arquivo incorreta.");
					}
				}else if (tipo.equals("anx")) {	//Anexos
					int arqSizeMax = 5 * 1024;
					if (arqSize < arqSizeMax) {	//Tamanho
						int ctTypeSize = 0;
						if (req.getSession().getAttribute("anx.ct") == null) {
							ctTypeSize = 1;
						}
						else {
							ctTypeSize = Integer.parseInt(req.getSession().getAttribute("anx.ct").toString()) + 1;
						}
						
						String auxPath = servlet.getServletContext().getRealPath("");
						String fs = File.separator;
						String pathImagem = auxPath.substring(0, auxPath.lastIndexOf(fs)) + fs + "header2012"+fs+"boletins";
						
						Calendar cal = Calendar.getInstance();
					    int month = cal.get(Calendar.MONTH) + 1;
					    int year = cal.get(Calendar.YEAR);
			
					    Date dtHH = cal.getTime();
					    
					    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
					    
						//System.out.println("Subindo arquivo: " + pathImagem + "\\" + year + "\\" + month + "\\" + sdf.format(dtHH) + "_" + nomeArquivo);
						
						if(!(new File(pathImagem + fs + year + fs)).exists()){
							new File(pathImagem + fs + year + fs).mkdir();	//cria o diretorio de ano, caso nao exista
						}
						if(!(new File(pathImagem + fs + year + fs + month + fs)).exists()){
							new File(pathImagem + fs + year + fs + month + fs).mkdir();	//cria o diretorio de mes, caso nao exista
						}
						FileOutputStream fos;
						try {
							fos = new FileOutputStream(pathImagem + fs + year + fs + month + fs + sdf.format(dtHH) + "_" + nomeArquivo);
						    BufferedOutputStream bos = new BufferedOutputStream(fos); //Criamos o arquivo  
						    bos.write(data); //Gravamos os bytes lá  
						    bos.close(); //Fechamos o stream.
						}
						catch (FileNotFoundException fnfe) {
							System.out.println(fnfe);
						}
						catch (IOException ioe) {
							System.out.println(ioe);
						}
						req.setAttribute("statusUploadFile", "1");
						req.setAttribute("srcUploadFile", year + "/" + month + "/" + sdf.format(dtHH) + "_" + nomeArquivo);
						req.setAttribute("pathUploadFile", year + "/" + month + "/" + sdf.format(dtHH) + "_" + nomeArquivo);

						req.setAttribute("msgUploadFile", "<span class='info'>Arquivo inserido com sucesso!</span>");
						req.getSession().setAttribute("anx.ct", ctTypeSize);
						req.getSession().setAttribute("anx.arq" + ctTypeSize, year + "/" + month + "/" + sdf.format(dtHH) + "_" + nomeArquivo);
					}
					else {
						req.setAttribute("statusUploadFile", "0");
						req.setAttribute("srcUploadFile", "");
						req.setAttribute("msgUploadFile", "<span class='alert'>Tamanho do arquivo: "+arqSize+"Kb.\nMaximo tamanho permitido: "+arqSizeMax+"Kb.\n\nFavor re-inserir o arquivo com o tamanho correto.</span>");
					}
					
				}
				else {
					//outra forma de upload
				}
			}
		} catch (final Exception exception) {
			throw new ServletException(exception);
		}
		return new ActionRouter(this.destPage, true, true, this.idPagina, usuario);
	}
}