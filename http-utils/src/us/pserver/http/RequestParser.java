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
public class RequestParser extends HttpParser {

  private RequestLine request;
  
  @Override
  public RequestParser readFrom(InputStream in) throws IOException {
    super.readFrom(in);
    Header hd = headers().get(0);
    
    if(hd == null) throw new IOException(
        "Error parsing request: No header identified");
    String[] ss = hd.getValue().split(BLANK);
    if(ss == null || ss.length < 3) throw new IOException(
        "Error parsing request: Invalid request line");
    
    request = new RequestLine();
    request.setMethod(ss[0]);
    request.setAddress(ss[1]);
    request.setHttpVersion(ss[2]);
    headers().set(0, request);
    return this;
  }
  
  
  public RequestLine getRequestLine() {
    return request;
  }
  
}
