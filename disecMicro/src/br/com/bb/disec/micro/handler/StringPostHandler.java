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

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/07/2016
 */
public class StringPostHandler implements HttpHandler {
  
  private final StringBuilder data;
  
  
  public StringPostHandler() {
    data = new StringBuilder();
  }
  
  
  public String getPostData() {
    return data.toString();
  }
  

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    hse.startBlocking();
    BufferedReader read = new BufferedReader(
        new InputStreamReader(hse.getInputStream())
    );
    String line = null;
    while((line = read.readLine()) != null) {
      data.append(line).append("\n");
    }
  }

}