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

package br.com.bb.disec.micro.handler;

import br.com.bb.disec.micro.util.URIParam;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/07/2016
 */
public class HelloHandler implements HttpHandler {

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    URIParam pars = new URIParam(hse.getRequestURI());
    System.out.println("* context: "+ pars.getContext());
    System.out.println("* param 0: "+ pars.getObject(0)+ " - "+ pars.getObject(0).getClass().getName());
    System.out.println("* param 1: "+ pars.getObject(0)+ " - "+ pars.getObject(0).getClass().getName());
    System.out.println("* param 2: "+ pars.getObject(0)+ " - "+ pars.getObject(0).getClass().getName());
    System.out.println("* param 3: "+ pars.getObject(0)+ " - "+ pars.getObject(0).getClass().getName());
    String str = this.toString();
    StringBuilder sb = new StringBuilder()
        .append("<h2 style='font-family: monospace;'>")
        .append("&bull; HelloHandler Instance ID: ")
        .append(str.substring(str.indexOf('@')))
        .append("</h2>");
    hse.getResponseSender().send(sb.toString());
    hse.endExchange();
  }
    
}
