/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package com.jpower.simplemail;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import com.jpower.simplemail.event.PrinterConnectionListener;
import com.jpower.simplemail.event.PrinterMailListener;
import com.jpower.simplemail.internal.AbstractEmbeddedObject;
import com.jpower.simplemail.internal.BufferedDataSource;
import com.jpower.simplemail.internal.InputDataSource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * <p style="font-size: medium;">
 * Imagem embarcada no corpo do email. 
 * EmbeddedImage pode ser configurada através de
 * um arquivo de imagem, InputStream, ou BufferedImage.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "EmbeddedImage",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Imagem Embarcada em email"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class EmbeddedImage extends AbstractEmbeddedObject {
  
  private DataSource data;
  
  private String format;
  
  private StringBuilder tag;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public EmbeddedImage() {
    data = null;
    name = null;
    format = "PNG";
    tag = null;
  }
  
  
  /**
   * Retorna a fonte da dados interna, do
   * conteúdo da imagem.
   * @return <code>DataSource</code>.
   */
  public DataSource getDataSource() {
    return data;
  }
  
  
  /**
   * Extrai o formato da imagem do nome do arquivo 
   * informado.
   * @param f <code>File</code>.
   * @return <code>true</code> se o formato foi extraído 
   * corretamente, <code>false</code> caso contrario.
   */
  private boolean updateFileExtension(File f) {
    if(f == null || f.getName() == null || f.getName().length() < 3)
      return false;
    
    String ext = f.getName().substring(f.getName().length() -3);
    this.format = null;
    if(ext.equalsIgnoreCase("gif"))
      format = ext;
    if(ext.equalsIgnoreCase("jpg"))
      format = ext;
    if(ext.equalsIgnoreCase("peg"))
      format = "jpeg";
    if(ext.equalsIgnoreCase("png"))
      format = ext;
    if(ext.equalsIgnoreCase("bmp"))
      format = ext;
    if(ext.equalsIgnoreCase("iff"))
      format = "tiff";
    
    return format != null;
  }
  
  
  /**
   * Define a imagem através de um arquivo.
   * @param f Arquivo de imagem.
   */
  public void setImage(File f) {
    if(f == null)
      throw new IllegalArgumentException(
          "{EmbeddedImage.setImage( File )}: "
          + "Invalid image file: "+f);
    if(!updateFileExtension(f))
      throw new IllegalArgumentException(
          "{EmbeddedImage.setImage( File )}: "
          + "Unsupported image format: "+f);
    
    try {
      this.setName(f.getName());
      data = new FileDataSource(f);
    } catch(Exception ex) {
      throw new IllegalArgumentException(
          "{EmbeddedImage.setImage( File )}", ex);
    }
  }
  
  
  /**
   * Define a imgem através do
   * <code>InputStream</code> informado.
   * @param input Entrada de dados da imagem.
   * @param name Nome da imagem.
   * @param format Formato da imagem.
   */
  public void setImage(InputStream input, String name, String format) {
    String error = null;
    if(input == null) 
      error = "InputStream <input>: " + input;
    if(name == null || name.trim().equals("")) 
      error = "<name>: " + name;
    if(format == null || format.trim().equals("")) 
      error = "<format>: " + format;
    
    if(error != null) throw 
        new IllegalArgumentException(
            "{EmbeddedImage.setImage( InputStream, String, String )}: "
            + "Invalid " + error);

    this.setName(name);
    this.setFormat(format);
    InputDataSource is = new InputDataSource(input);
    is.setContentType(this.getType());
    is.setName(name);
    this.data = is;
  }
  
  
  /**
   * Define a imagem através do <code>BufferedImage</code>
   * informado.
   * @param image Imagem em buffer.
   * @param name Nome da imagem.
   * @param format Formato da imagem.
   * @throws IOException Caso ocorra erro extraindo os
   * dados do <code>BufferedImage</code>.
   */
  public void setImage(BufferedImage image, String name, 
      String format) throws IOException {
    
    if(image == null) throw 
        new IllegalArgumentException(
            "Invalid Image <image>: <null>");
    if(format == null) throw 
        new IllegalArgumentException(
            "Invalid Image Format <format>: <null>");
    if(name == null) throw 
        new IllegalArgumentException(
            "Invalid Image Name <name>: <null>");
    
    this.setName(name);
    this.setFormat(format);
    BufferedDataSource src = new BufferedDataSource();
    src.setContentType(this.getType());
    src.setName(name);
    data = src;
    OutputStream out = data.getOutputStream();
    ImageIO.write(image, format, out);
    out.flush();
    out.close();
  }
  
  
  /**
   * Atualiza a tag html pela qual 
   * a imagem será embarcada no corpo do email.
   */
  private void updateImageHtmlTag() {
    tag = new StringBuilder();
    tag.append("<img name='");
    tag.append(name);
    tag.append("' alt='");
    tag.append(name);
    tag.append("' src='cid:");
    tag.append(name);
    tag.append("'/>");
  }
  
  
  /**
   * Retorna a tag html pela qual 
   * a imagem será embarcada no corpo do email.
   * @return String contendo a tag html.
   */
  public String getImageHtmlTag() {
    if(tag == null) return null;
    return tag.toString();
  }
  
  
  /**
   * Define o nome da imagem e respectivamente
   * seu código de acesso ao conteúdo embarcado.
   * @param name Nome da imagem.
   */
  @Override
  public void setName(String name) {
    this.name = name;
    this.updateImageHtmlTag();
  }
  
  
  /**
   * Retorna o formato da imagem.
   * @return Formato da imagem.
   */
  public String getFormat() {
    return format;
  }


  /**
   * Define o formato da imagem.
   * @param format Formato da imagem.
   */
  public void setFormat(String format) {
    this.format = format;
  }
  
  
  /**
   * Define o mime-type do anexo.
   * @param type 
   */
  @Override
  public void setType(String type) {
    if(type == null || !type.contains("/"))
      return;
    super.setType(type);
    format = type.split("/")[1];
  }
  
  
  /**
   * Retorna o mime-type do anexo.
   * @return Mime-Type. Ex: "IMAGE/JPG"
   */
  @Override
  public String getType() {
    return ("image/".concat(format)).toUpperCase();
  }
  
  
  /**
   * Salva o conteúdo da imagem no arquivo informado.
   * @param f Arquivo onde será salva a imagem.
   * @throws IOException Caso ocorra erro ao salvar a imagem.
   */
  @Override
  public void saveTo(File f) throws IOException {
    if(this.getDataSource() == null) throw new IOException(
        "{EmbeddedImage.saveTo( File )}: Invalid DataSource");
    
    this.saveTo(this.getDataSource().getInputStream(), f);
  }
  
  
  public static void main(String[] args) throws Exception, SMailException {
    
    //if(directEmbeddedImageTest()) return;
    
    
    File fimg = new File("D:/images/ocelot-tux.png");
    Message m = new Message();
    m.setSubject("Foto embarcada");
    m.from("Juno Roesler <juno.rr@gmail.com>");
    m.to("Juno Roesler <juno.rr@gmail.com>");
    m.to("Juno Roesler BB <juno.roesler@bb.com.br>");
    
    String html1 = 
        "<html>"
        + "<body>"
        +   "<p>"
        +     "<h3>Foto na praia...</h3>";
    String html2 = 
        "</p>"
        + "</body>"
        + "</html>";
    
    String html = html1.concat(m.embedImage(fimg)).concat(html2);
    
    m.setContentHtml(html);
    SMail mail = new SMail(
        MailServer.GMAIL_SMTP_SERVER
        .setAuthentication("juno.rr", "passwd=$0988"));
    mail.add(new PrinterConnectionListener(System.out));
    mail.add(new PrinterMailListener(System.out));
    
    mail.send(m);
  }
  
  
  public static boolean directEmbeddedImageTest() throws Exception {
    System.out.println("[Configuring...]");
    
    SMail mail = new SMail(
        MailServer.GMAIL_SMTP_SERVER
        .setAuthentication("juno.rr", "passwd=$0988"));
    
    javax.mail.Message msg = new MimeMessage(mail.createSendSession());
    Multipart mp = new MimeMultipart();
    
    System.out.println("[Creating Html Content...]");
    BodyPart content = new MimeBodyPart();
    String html = "<html><h3>Foto embaixo</h3><br><img src='cid:img123'/></html>";
    content.setContent(html, "text/html");
    System.out.println("[content-type: "+content.getContentType()+"]");
    mp.addBodyPart(content);
    
    System.out.println("[Creating Image part...]");
    BodyPart image = new MimeBodyPart();
    File fimg = new File("D:/images/ocelot-tux.png");
    DataSource ds = new FileDataSource(fimg);
    image.setFileName("foto.jpg");
    image.setDescription("foto.jpg");
    image.setDisposition(BodyPart.INLINE);
    image.setDataHandler(new DataHandler(ds));
    image.setHeader("Content-type", "IMAGE/JPG");
    image.setHeader("Content-ID", "<img123>");
    System.out.println("[content-type: "+image.getContentType()+"]");
    mp.addBodyPart(image);
    
    System.out.println("[Setting up Message...]");
    msg.setContent(mp);
    msg.addFrom(new Address[]{new InternetAddress("juno.rr@gmail.com")});
    msg.addRecipient(RecipientType.TO, new InternetAddress("juno.roesler@bb.com.br"));
    msg.addRecipient(RecipientType.TO, new InternetAddress("juno.rr@gmail.com"));
    msg.setSubject("Direct EmbeddedImage Test");
    
    System.out.println("[Sending...]");
    mail.send(msg);
    System.out.println("[Message sended successfully!]");
    
    return true;
  }
  
}
