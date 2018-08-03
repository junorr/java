package br.com.bb.disec.aplic.upload;

import br.com.bb.disec.aplic.html.H;
import br.com.bb.disec.aplic.html.HtmlAppender;
import br.com.bb.disec.aplic.html.HtmlElement;
import br.com.bb.disec.aplic.html.Style;
import br.com.bb.disec.aplic.html.UL;
import br.com.bb.disec.aplic.html.UploadResponse;
import br.com.bb.sso.bean.User;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class ServletUpload extends HttpServlet {
	
	private static final long serialVersionUID = 1024L;

	public static final String UPLOAD_PATH = "upload_path";
	
	public static final String MAX_FILE_SIZE = "max_file_size";
	
	
	private ServletConfig config;
	
	private ServletContext context;
	
	private long maxFileSize;
	
	private String filepath;

	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		config = servletConfig;
		context = config.getServletContext();
		String strMaxFileSize = config.getInitParameter(MAX_FILE_SIZE);
		maxFileSize = Long.parseLong(strMaxFileSize);
		filepath = config.getInitParameter(UPLOAD_PATH);
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
		if(ServletFileUpload.isMultipartContent(req)) {
			DiskFileItemFactory diskfac = new DiskFileItemFactory();
			diskfac.setSizeThreshold((int) maxFileSize);
			diskfac.setRepository(File.createTempFile("servletUpload", "tmp"));
			ServletFileUpload upload = new ServletFileUpload(diskfac);
			upload.setFileSizeMax(maxFileSize);
			try {
				List<FileItem> ls = upload.parseRequest(req);
				FormFieldHandler ff = new FormFieldHandler(ls).parse();
				User usu = (User) req.getSession().getAttribute("user");
				HtmlAppender ap = new HtmlAppender();
				for(FileItem fi : ff.getFileUpload()) {
					HtmlElement el = doUpload(fi, usu);
					if(el != null) {
            ap.append(el);
					}
				}
				writeResponse(ap, rsp.getWriter());
			} 
			catch(FileSizeLimitExceededException e) {
				HtmlAppender ap = new HtmlAppender();
				ap.append(new H(3).setStyle(new Style().setColor(Color.RED)).add("Falha no Carregamento!"))
						.append(new H(4).add("O tamanho do arquivo ultrapassa o limite m&aacute;ximo."))
						.append(new UL().add("Tamanho M&aacute;ximo Permitido: "
							+ new FileSizeFormatter().format(maxFileSize)))
						.appendHidden("success", "false");
				writeResponse(ap, rsp.getWriter());
			}
			catch(FileUploadException | IOException e) {
				writeResponse(new ExceptionResponseHandler(e).handle(), rsp.getWriter());
			}//try-catch
		}//if isMultipart
	}
	
	
	public UploadResponse doUpload(FileItem item, User usu) {
		if(item == null 
				|| item.isFormField() 
				|| usu == null)
			return null;
		
		File file = null;
		String filename = null;
		UploadResponse resp;

		try {
			//Verifica permissoes para upload de arquivos, localizados no web.xml da aplicacao.
			//boolean access = new UploadAccessHandler(usu, context).handle();
			//System.out.println("*** access allowed? "+ access);
			if( ! new UploadAccessHandler(usu, context).handle()) {
				throw new IllegalStateException("Desculpe "+ usu.getNomeGuerra()
						+ ", voc&ecirc; n&atilde;o possui acesso para carregar arquivos.");
			}
		
			NameHandler fname = new NameHandler(filepath, item.getName());
			filename = fname.handle();
			file = new File( filename );
	        
			//Escreve o arquivo no sistema de arquivos do servidor.
			item.write(file);
      UploadResponse ur = new FileHandler(file).handle();
      if(ur != null) {
        resp = ur;
        if(!file.delete()) {
          file.deleteOnExit();
        }
      } else {
        resp = new UploadResponse()
            .setFilename(fname.getBaseName())
            .setSize(file.length())
            .setSuccessful(true);
      }
		} 
		catch (Exception e) {
			resp = new UploadResponse()
          .setFilename(filename)
          .setMessage(e.toString())
          .setSuccessful(false);
		}
		return resp;
	}
	
	
	protected void writeResponse(HtmlElement rsp, PrintWriter out) {
		if(rsp == null || out == null) return;
		out.println(rsp.toHtml());
		out.flush();
	}
	
}
