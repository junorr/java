package br.com.bb.dinop.arqRedeDinop.servlet;

import java.io.File;
import java.io.FileReader;
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
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import br.com.bb.dinop.arqRedeDinop.beans.UploadResponse;


public class UploadFile extends HttpServlet {
	
	private static final long serialVersionUID = 1024L;

	public static final String UPLOAD_PATH = "upload_path";
	
	public static final String MAX_FILE_SIZE = "max_file_size";
	
	public static final String 
			INPUT_INOME = "inome",
			INPUT_ICHAVE = "ichave",
			INPUT_ICMSS = "icmss",
			INPUT_IUOREQP = "iuoreqp",
			INPUT_ILABEL = "ilabel",
			INPUT_IUOR = "iuor";
	
	public static final String 
			UPLOAD_ALLOWED_UORS = "upload_allowed_uors",
			UPLOAD_ALLOWED_CHAVES = "upload_allowed_chaves",
			UPLOAD_ALLOWED_CMSS = "upload_allowed_cmss";

	public static final int MAX_MEM_SIZE = 1024 * 2048; //2MB
	
	
	private ServletConfig config;
	
	private ServletContext context;

	
	public void init(ServletConfig servletConfig) throws ServletException {
		config = servletConfig;
	}
	

	protected void doPost(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
		context = req.getServletContext();

		if(ServletFileUpload.isMultipartContent(req)) {
			DiskFileItemFactory diskfac = new DiskFileItemFactory();
			diskfac.setSizeThreshold(MAX_MEM_SIZE);
			diskfac.setRepository(File.createTempFile("servletUpload", "tmp"));
			ServletFileUpload upload = new ServletFileUpload(diskfac);
			String strMaxFileSize = config.getInitParameter(MAX_FILE_SIZE);
			long maxFileSize = Long.parseLong(strMaxFileSize);
			upload.setFileSizeMax(maxFileSize);
			
			try {
				List<FileItem> ls = upload.parseRequest(req);
				
				String inome = null;
				String ichave = null;
				String iuor = null;
				String icmss = null;
				String iuoreqp = null;
				String ilabel = null;
				FileItem fileup = null;
				
				for(FileItem item : ls) {
					if(item.isFormField()) {
						String field = item.getFieldName();
						if(INPUT_INOME.equals(field)) {
							inome = item.getString();
						}
						else if(INPUT_ICHAVE.equals(field)) {
							ichave = item.getString();
						}
						else if(INPUT_IUOR.equals(field)) {
							iuor = item.getString();
						}
						else if(INPUT_ICMSS.equals(field)) {
							icmss = item.getString();
						}
						else if(INPUT_IUOREQP.equals(field)) {
							iuoreqp = item.getString();
						}
						else if(INPUT_ILABEL.equals(field)) {
							ilabel = item.getString();
						}
					}
					else {
						fileup = item;
					}
				}
				
				if(fileup == null) {
					writeResponse(new UploadResponse()
								.addTitle("Falha no Carregamento!")
								.addSubTitle("Nenhum arquivo encontrado.")
								.setHidden("success", "false"),
								rsp.getWriter());
					return;
				}
				
				if(!checkPermission(iuor, iuoreqp, icmss, ichave)) {
					writeResponse(new UploadResponse()
							.addTitle("Acesso Negado!")
							.addSubTitle("Desculpe "+ inome+ ", você não possui acesso para carregar arquivos.")
							.setHidden("success", "false"),
							rsp.getWriter());
					return;
				}
				
				if(fileup.getSize() > maxFileSize) {
					writeResponse(new UploadResponse()
							.addTitle("Falha no Carregamento!")
							.addTitle("O Tamanho do arquivo excede o tamanho máximo para carregamento.")
							.openList()
							.addListItem("Tamanho do Arquivo: "+ fileup.getSize() + " bytes")
							.addListItem("Tamanho Máximo: "+ maxFileSize + " bytes")
							.closeList()
							.setHidden("success", "false"),
							rsp.getWriter());
					return;
				}
				
	            // Write the file
	            File file;
	            String filename = fileup.getName();
	            String filepath = config.getInitParameter(UPLOAD_PATH);
	            
	            if( filename.lastIndexOf("\\") >= 0 ){
	            	file = new File( filepath + 
	            			filename.substring( filename.lastIndexOf("\\"))) ;
	            }
	            else if( filename.lastIndexOf("/") >= 0 ){
	            	file = new File( filepath + 
	            			filename.substring( filename.lastIndexOf("/"))) ;
	            }
	            else {
	            	file = new File( filepath + filename ); 
	            }
	            fileup.write(file);
	            
				writeResponse(new UploadResponse()
						.addTitle("Arquivo Carregado com Sucesso!")
						.openList()
						.openListItem().openStyledText().setStyleFontBold()
						.setStyledText("Arquivo: ")
						.addSimpleText(filename).closeListItem()
						.openListItem().openStyledText().setStyleFontBold()
						.setStyledText("Tamanho: ")
						.addSimpleText(String.valueOf(fileup.getSize())+ " bytes").closeListItem()
						.closeList()
						.setHidden("success", "true"),
						rsp.getWriter());
				
			} catch (Exception e) {
				UploadResponse ur = new UploadResponse()
						.addTitle("Falha no Carregamento!")
						.addSubTitle(e.toString());
				StackTraceElement[] elts = e.getStackTrace();
				ur.openList();
				for(StackTraceElement elt : elts) {
					ur.addListItem(elt.toString());
				}
				ur.closeList().setHidden("success", "false");
				writeResponse(ur, rsp.getWriter());
			}//try-catch
		}//if isMultipart
	}
	
	
	protected boolean checkPermission(String uor, String uoreqp, String cargo, String chave) {
		boolean allowed = false;
		if(uor != null || uoreqp != null) {
			String uors = context.getInitParameter(UPLOAD_ALLOWED_UORS);
			allowed = allowed || uors.contains(uor);
			allowed = allowed || uors.contains(uoreqp);
		}
		if(cargo != null) {
			String cargos = context.getInitParameter(UPLOAD_ALLOWED_CMSS);
			allowed = allowed || cargos.contains(cargo);
		}
		if(chave != null) {
			String chaves = context.getInitParameter(UPLOAD_ALLOWED_CHAVES);
			allowed = allowed || chaves.contains(chave);
		}
		return allowed;
	}
	
	
	protected void writeResponse(UploadResponse rsp, PrintWriter out) throws IOException {
		System.out.println("|==>> UploadATB: UploadResponse: "+ rsp.toString());
		out.println(rsp.toString());
		out.flush();
	}
	
}
