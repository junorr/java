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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 13/12/2011
 */
public class SendTutorial {
  
  public static void main(String[] args) throws IOException, SMailException {
    MailServer server = new MailServer()
        .setServer("smtp.gmail.com")
        .setProtocol(MailServer.PROTOCOL_SMTPS)
        .setPort(MailServer.DEFAULT_SMTPS_PORT)
        .setAuthentication("juno.rr@gmail.com", "passwd=$0988");
    SMail mail = new SMail(server);
    Message m = new Message();
    m.setSubject("Tutorial");
    m.from("juno.rr@gmail.com");
    m.to("talitahmelobadra@gmail.com");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    FileInputStream fis = new FileInputStream(
        new File("H:/java/simplemail/dist/javadoc/com/jpower/simplemail/tutorial/ReceivingMail.html"));
    byte[] buff = new byte[512];
    int read = -1;
    while((read = fis.read(buff)) > 0)
      out.write(buff, 0, read);
    String c = new String(out.toByteArray());
    m.setContentHtml(c);
    mail.send(m);
  }
  
}
