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

package us.pserver.http;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/06/2014
 */
public class ResponseParser extends HttpParser {

  private ResponseLine response;
  
  
  @Override
  public ResponseParser parseInput(InputStream in) throws IOException {
    super.parseInput(in);
    if(headers().isEmpty() || headers().get(0) == null) 
      throw new IOException(
          "Error parsing response (No header identified)");
    
    Header hd = headers().get(0);
    String[] ss = hd.getValue().split(BLANK);
    if(ss == null || ss.length < 3) throw new IOException(
        "Error parsing response: Invalid response line");
    
    response = new ResponseLine();
    response.setHttpVersion(ss[0]);
    response.setCode(Integer.parseInt(ss[1]));
    response.setStatus(ss[2]);
    for(int i = 3; i < ss.length; i++) {
      response.setStatus(
          response.getStatus() + BLANK + ss[i]);
    }
    headers().set(0, response);
    return this;
  }
  
  
  public ResponseLine getResponseLine() {
    return response;
  }
  
}
