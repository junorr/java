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

package us.pserver.streams;

import static us.pserver.streams.StreamUtils.EOF;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2014
 */
public class StreamResult {

  private long size;
  
  private String token;
  
  private String str;
  
  private boolean eof;


  public StreamResult() {
    size = 0;
    token = null;
    str = null;
    eof = false;
  }


  public long size() {
    return size;
  }


  public long getSize() {
    return size;
  }


  public StreamResult setSize(long size) {
    this.size = size;
    return this;
  }


  public String token() {
    return token;
  }


  public String getToken() {
    return token;
  }
  
  
  public boolean isTokenEOF() {
    return EOF.equals(token);
  }


  public StreamResult setToken(String until) {
    this.token = until;
    return this;
  }


  public String content() {
    return str;
  }


  public String getContent() {
    return str;
  }


  public StreamResult setContent(String s) {
    this.str = s;
    return this;
  }


  public boolean isEOFReached() {
    return eof || isTokenEOF();
  }


  public StreamResult eofOn() {
    this.eof = true;
    return this;
  }
  
  
  public StreamResult eofOff() {
    this.eof = false;
    return this;
  }
  
  
  public StreamResult increment() {
    size++;
    return this;
  }
  
  
  public StreamResult increment(int i) {
    size += i;
    return this;
  }
  
}
