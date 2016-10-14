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

package br.com.bb.disec.micro.util;

import io.undertow.server.HttpServerExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/07/2016
 */
public class StringPostParser implements HttpParser<String> {
  
  private final StringBuilder data;
  
  
  public StringPostParser() {
    data = new StringBuilder();
  }
  
  
  public String getPostData() {
    return data.toString();
  }
  
  
  private void resetPostData() {
    data.delete(0, data.length());
  }
  

  @Override
  public String parseHttp(HttpServerExchange hse) throws IOException {
    this.resetPostData();
    hse.startBlocking();
    BufferedReader read = new BufferedReader(
        new InputStreamReader(hse.getInputStream())
    );
    String line = null;
    while((line = read.readLine()) != null) {
      if(!data.toString().isEmpty()) {
        data.append("\n");
      }
      data.append(line);
    }
    return data.toString();
  }

}
