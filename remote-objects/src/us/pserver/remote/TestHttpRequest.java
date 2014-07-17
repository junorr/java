/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
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

package us.pserver.remote;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/06/2014
 */
public class TestHttpRequest {

  
  public static void main(String[] args) throws IOException {
    NetConnector nc = new NetConnector("172.24.75.2", 9011);
    /* set proxy 
    nc.setProxyAddress("172.24.75.19")
        .setProxyPort(6060)
        .setProxyAuthorization("f6036477:00000000");
    /**/
    HttpRequestChannel channel = new HttpRequestChannel(nc);
    
    Transport trp = new Transport();
    trp.setObject("Hello HttpUtils!!");

    
    InputStream input = Files.newInputStream(
        Paths.get("d:/grub-bg.jpg"), 
        StandardOpenOption.READ);
    trp.setInputStream(input);
    
    channel.write(trp);
    
    System.out.println("* request sent!");
    System.out.print("* "+ channel
        .getResponseParser().getResponseLine());
    trp = channel.read();
    
    System.out.println("* received: "+ trp);
    System.out.println("--------------------------------");
    channel.getResponseParser().headers()
        .forEach(System.out::print);
    //req.getHttpBuilder().writeTo(System.out);
    channel.close();
  }
  
}
