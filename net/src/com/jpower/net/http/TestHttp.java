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

package com.jpower.net.http;

import com.jpower.net.DynamicBuffer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 20/12/2013
 */
public class TestHttp {

  
  public static void main(String[] args) {
    HttpRequest req = new HttpRequest();
    req.setAddress("www.google.com");
    req.setProxyAuth("xxx:yyy");
    DynamicBuffer db = new DynamicBuffer();
    db.writeString("HELLO!!");
    req.setContent(db);
    String sreq = req.build();
    System.out.println(sreq);
    System.out.println();
    
    RequestParser parser = new RequestParser();
    parser.parse(sreq);
    
    System.out.println("* address = "+ parser.getAddress());
    System.out.println("* content = "+ parser.getContent().readString());
    System.out.println("* host    = "+ parser.getHost());
    System.out.println("* method  = "+ parser.getMethod());
    System.out.println("* user ag = "+ parser.getUserAgent());
    System.out.println("* version = "+ parser.getVersion());
    
    System.out.println();
    System.out.println("-----------------------------");
    System.out.println();
    
    db.clear().writeString("WORLD!!");
    HttpResponse resp = new HttpResponse()
        .setContent(db)
        .setServer("Apache Tomcat");
    ResponseParser rp = new ResponseParser();
    String sresp = resp.build();
    System.out.println(sresp);
    rp.parse(sresp);
    System.out.println("* connection = "+ rp.getConnection());
    System.out.println("* cont type  = "+ rp.getContentType());
    System.out.println("* encoding   = "+ rp.getEncoding());
    System.out.println("* proxy conn = "+ rp.getProxyConnection());
    System.out.println("* resp code  = "+ rp.getResponseCode());
    System.out.println("* server     = "+ rp.getServer());
    System.out.println("* content    = "+ rp.getContent().readString());
  }
  
}
