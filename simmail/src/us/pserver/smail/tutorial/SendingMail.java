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

package us.pserver.smail.tutorial;

import us.pserver.smail.MailServer;
import us.pserver.smail.Message;
import us.pserver.smail.SMail;
import us.pserver.smail.SMailException;
import java.io.File;


/**
 * <h2>Tutorial: Enviando email com SimpleMail</h2>
 * <p style="font-size: medium;">
 * Envio de email com anexos ou imagem embarcada é muito simples com <b>SimpleMail</b>.
 * <br><br>
 * Inicialmente, criamos e configuramos os dados básicos da mensagem:
 * </p>
 * <pre>
 *   Message msg = new Message();
 *   msg.from("Juno Roesler &lt;juno.rr@gmail.com&gt;")
 *      .to("Duke &lt;duke@java.com&gt;")
 *      .setSubject("Hello!!");
 * </pre>
 * 
 * <p style="font-size: medium;">
 * A seguir, preparamos o conteúdo html da mensagem:
 * </p>
 * <pre>
 *   String html = "&lt;h2&gt; Hello Duke! Here is a cool image: &lt;/h2&gt;";
 * </pre>
 * 
 * <p style="font-size: medium;">
 * Então, embarcamos a imagem e usamos o código html retornado:
 * </p>
 * <pre>
 *   html += msg.embedImage(new File("/home/juno/drinking-duke.jpg"));
 * 
 *   msg.setContentHtml(html);
 * </pre>
 * 
 * <p style="font-size: medium;">
 * Anexar arquivos é igualmente fácil:
 * </p>
 * <pre>
 *   msg.attach(new File("/home/juno/doc.pdf"));
 * </pre>
 * 
 * <p style="font-size: medium;">
 * Para enviar a mensagem, precisamos das configurações
 * da conta e do servidor de email smtp, que é feita com o
 * objeto <code>MailServer</code>. <br>
 * Existe um campo público 
 * chamado <code>GMAIL_SMTP_SERVER</code>, que contém as informações
 * do GMail pré-configuradas,<br> mas para o exemplo, vamos configurar um novo:
 * </p>
 * <pre>
 *   MailServer server = new MailServer();
 *   server.setName("gmail_smtp")  //nome opcional. Útil no caso de um pool de servidores.
 *       .setProtocol(MailServer.PROTOCOL_SMTPS)
 *       .setPort(MailServer.DEFAULT_SMTPS_PORT)
 *       .setServer("smtp.gmail.com")
 *       .setAuthentication("juno.rr@gmail.com", "mypassword");
 * </pre>
 * 
 * <p style="font-size: medium;">
 * Bom, essa foi a parte complicada.
 * Para enviar ou receber emails, usamos a classe <code>SMail</code>,
 * passando como argumento o servidor que acabamos de configurar:
 * </p>
 * <pre>
 *   SMail mail = new SMail(server);
 * </pre>
 * 
 * <p style="font-size: medium;">
 * E finalmente, enviamos a mensagem:
 * </p>
 * <pre>
 *    mail.send(msg);
 * </pre>
 * 
 * <p style="font-size: medium;">
 * Mas BAH! É fácil que é uma barbaridade. <br>
 * Dá pra mandar o email com uma mão enquanto segura o chimarrão com a outra. <b> <code>  ;)</code></b>
 * </p>
 * 
 * @see us.pserver.smail.Message
 * @see us.pserver.smail.MailServer
 * @see us.pserver.smail.SMail
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class SendingMail {
  
  public static void main(String[] args) {
    //Inicialmente, criamos e configuramos os dados básicos da mensagem:
    System.out.print("[Creating Message...      ] ");
    Message msg = new Message();
    msg.from("Juno Roesler <juno.rr@gmail.com>")
        .to("Duke <juno.rr@gmail.com>")
        .setSubject("Hello!!");
    System.out.println("OK");
    
    //A seguir, preparamos o conteúdo html:
    String html = "<h2> Hello Duke! Here is a cool image: </h2>";
    
    //Então, embarcamos a imagem e usamos o código html retornado:
    html += msg.embedImage(
        new File("/home/juno/cool-image.jpg"));
    
    //Definimos o corpo da mensagem com o html:
    System.out.print("[Setting Content...       ] ");
    msg.setContentHtml(html);
    System.out.println("OK");
    
    //Anexar arquivos é igualmente fácil:
    System.out.print("[Attaching File...        ] ");
    msg.attach(new File("/home/juno/doc.pdf"));
    System.out.println("OK");
    
    
    //Para enviar a mensagem, precisamos das configurações
    //da conta e do servidor de email smtp, que é feita com o
    //objeto MailServer. Existe um campo público 
    //chamado GMAIL_SMTP_SERVER, que contém as informações
    //do GMail pré-configuradas, mas para o exemplo, vamos configurar um novo:
    System.out.print("[Configuring Server...    ] ");
    MailServer server = new MailServer();
    server.setName("gmail_smtp") //nome opcional. Útil no caso de um pool de servidores.
        .setProtocol(MailServer.PROTOCOL_SMTPS)
        .setPort(MailServer.DEFAULT_SMTPS_PORT)
        .setServer("smtp.gmail.com")
        .setAuthentication("juno.rr@gmail.com", "passwd=$0988");
    
    //Bom, essa foi a parte complicada, agora vem o mais fácil.
    //Para enviar ou receber email, usamos a classe SMail,
    //passando como argumento o servidor que acabamos de configurar:
    SMail mail = new SMail(server);
    System.out.println("OK");
    
    //Finalmente, enviamos a mensagem:
    System.out.print("[Sending...               ] ");
    try {
      mail.send(msg);
    } catch(SMailException ex) {
      //Não faça isso. Trate o erro apropriadamente.
      ex.printStackTrace();
    }
    System.out.println("OK");
  }
  
}
