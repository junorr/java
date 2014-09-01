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

package us.pserver.smail;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import java.nio.charset.Charset;


/**
 * <p style="font-size: medium;">
 * Encapsula as informações da
 * conta de email, servidor, porta e
 * protocolo de comunicação, além de usuário e senha, 
 * necessárias ao motor de conexão <code>SMail</code>.
 * </p>
 * 
 * @see us.pserver.smail.SMail
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "SMail",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Encapsula informações da conta e servidor de email"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class MailServer {
  
  private String name;
  
  private String server;
  
  private int port;
  
  private String protocol;
  
  private String user;
  
  private String password;
  
  private Charset charset;
  
  private boolean authenticated;
  
  
  public MailServer() {
    name = null;
    server = null;
    port = 0;
    protocol = null;
    user = null;
    password = null;
    authenticated = false;
    charset = Charset.defaultCharset();
  }
  
  
  protected MailServer(String name, String server, int port, String protocol, Charset charset) {
    this();
    this.setName(name)
        .setServer(server)
        .setPort(port)
        .setProtocol(protocol)
        .setAuthentication(user, password)
        .setCharset(charset);
  }
  
  
  /**
   * Retorna o nome do Servidor de email.
   * @return Nome do servidor.
   */
  public String getName() {
    return name;
  }
  
  
  /**
   * Define o nome do Servidor de email (opcional).
   * @param name Nome do servidor.
   * @return Esta instância modificada de MailServer
   */
  public MailServer setName(String name) {
    this.name = name;
    return this;
  }
  
  
  /**
   * Retorna a configuração de localidade de idioma.
   * @return Charset.
   */
  public Charset getCharset() {
    return charset;
  }


  /**
   * Define a localidade de idioma.
   * @param charset 
   * @return Esta instância modificada de MailServer
   */
  public MailServer setCharset(Charset charset) {
    if(charset != null)
      this.charset = charset;
    return this;
  }
  
  
  /**
   * Define o usuário e senha da conta de email
   * para conexão.
   * @param user Usuário da conta de email.
   * @param passwd Senha da conta de email.
   * @return Esta instância modificada de MailServer
   */
  public MailServer setAuthentication(String user, String passwd) {
    this.setUser(user);
    this.setPassword(passwd);
    return this;
  }


  /**
   * Retorna a senha da conta de email.
   * @return Senha.
   */
  public String getPassword() {
    return password;
  }


  /**
   * Define a senha da conta de email.
   * @param password Senha.
   * @return Esta instância modificada de MailServer
   */
  public MailServer setPassword(String password) {
    if(user != null) authenticated = true;
    else authenticated = false;
    this.password = password;
    return this;
  }


  /**
   * Retorna a porta de conexão com o servidor.
   * @return Porta para conexão.
   */
  public int getPort() {
    return port;
  }


  /**
   * Define a porta de conexão com o servidor.
   * @param port Porta para conexão.
   * @return Esta instância modificada de MailServer
   */
  public MailServer setPort(int port) {
    this.port = port;
    return this;
  }


  /**
   * Retorna o servidor remoto de email.
   * @return Servidor de email.
   */
  public String getServer() {
    return server;
  }


  /**
   * Define o servidor remoto de email.
   * @param host Servidor de email.
   * @return Esta instância modificada de MailServer
   */
  public MailServer setServer(String host) {
    this.server = host;
    return this;
  }


  /**
   * Retorna o protocolo de comunicação com o servidor de email.
   * @return Protocolo para comunicação com o servidor.
   */
  public String getProtocol() {
    return protocol;
  }


  /**
   * Define o protocolo de comunicação com o servidor de email.
   * @param protocol Protocolo para comunicação com o servidor.
   * @return Esta instância modificada de MailServer
   */
  public MailServer setProtocol(String protocol) {
    this.protocol = protocol;
    return this;
  }
  
  
  /**
   * Verifica se o acesso ao servidor
   * deve ser autenticado ou não.
   * @return <code>true</code> se o acesso
   * ao servidor é autenticado, 
   * <code>false</code> caso contrário.
   */
  public boolean isAuthenticated() {
    return authenticated;
  }


  /**
   * Retorna o nome de usuário da conta de email, 
   * utilizado na autenticação.
   * @return Nome de usuário.
   */
  public String getUser() {
    return user;
  }


  /**
   * Define o nome de usuário da conta de email, 
   * utilizado na autenticação.
   * @param user Nome de usuário.
   * @return Esta instância modificada de MailServer
   */
  public MailServer setUser(String user) {
    if(user != null) authenticated = true;
    else authenticated = false;
    this.user = user;
    return this;
  }
  
  
/******************** CAMPOS ESTÁTICOS ********************/
  
  
  /**
   * MailServer configurado para conexão 
   * com o servidor de emails SMTP 
   * (envio) do Google - GMAIL.
   */
  public static final MailServer GMAIL_SMTP_SERVER = 
      new MailServer("GMAIL SMTP", "smtp.gmail.com", 
          MailServer.DEFAULT_SMTPS_PORT, 
          MailServer.PROTOCOL_SMTPS, null);
  
      
  /**
   * MailServer configurado para conexão 
   * com o servidor de emails IMAP 
   * (recebimento) do Google - GMAIL.
   */
  public static final MailServer GMAIL_IMAP_SERVER = 
      new MailServer("GMAIL IMAP", "imap.gmail.com", 
          MailServer.DEFAULT_IMAP_PORT, 
          MailServer.PROTOCOL_IMAPS, null);
  
      
  /**
   * Nome do protocolo.
   */
  public static final String
      PROTOCOL_SMTP = "smtp",
      PROTOCOL_SMTPS = "smtps",
      PROTOCOL_IMAP = "imap",
      PROTOCOL_IMAPS = "imaps";
  
  
  /**
   * Porta padrão.
   */
  public static final int 
      DEFAULT_SMTP_PORT = 587,
      DEFAULT_SMTPS_PORT = 465,
      DEFAULT_IMAP_PORT = 993;
  
}
