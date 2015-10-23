/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.streams;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class PushbackInputStream extends FilterInputStream {

  
  public static final int DEFAULT_BUFFER_SIZE = 4096;
  
  
  private byte[] buffer;
  
  private int unread;
  
  private int index;
  
  private final int bufsize;
  
  
  public PushbackInputStream(InputStream in) {
    super(in);
    unread = 0;
    index = 0;
    bufsize = DEFAULT_BUFFER_SIZE;
  }
  
  
  public PushbackInputStream(InputStream in, int bufferSize) {
    super(in);
    unread = 0;
    bufsize = Valid.off(bufferSize)
        .forLesserEquals(0)
        .getOrFail("Invalid buffer size: ");
  }
  
  
  public void unread(int b) {
    if(buffer == null) {
      buffer = new byte[bufsize];
    }
    buffer[unread++] = (byte) b;
  }
  
  
  public void unread(byte[] bs, int off, int len) {
    Valid.off(bs).forEmpty().fail();
    Valid.off(off).forLesserThan(0).fail();
    Valid.off(len).forLesserThan(1)
        .and().forGreaterThan(bs.length-off).fail();
    for(int i = off; i < len; i++) {
      unread(bs[i]);
    }
  }
  
  
  public void unread(byte[] bs) {
    unread(
        Valid.off(bs).forEmpty().getOrFail(), 
        0, bs.length
    );
  }
  
  
  public int getUnreadAvailable() {
    return Math.max(unread - index, 0);
  }
  
  
  public boolean hasUnreadAvailable() {
    boolean has = getUnreadAvailable() > 0;
    if(!has && unread > 0) {
      unread = index = 0;
    }
    return has;
  }
  
  
  @Override
  public int read() throws IOException {
    if(hasUnreadAvailable()) {
      return buffer[index++];
    } else {
      return super.read();
    }
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    Valid.off(bs).forEmpty().fail();
    Valid.off(off).forLesserThan(0).fail();
    Valid.off(len)
        .forNotBetween(1, bs.length-off)
        .fail();
    int ix = off;
    while(hasUnreadAvailable()) {
      bs[ix++] = buffer[index++];
    }
    return ix + super.read(bs, ix, len-ix);
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return read(
        Valid.off(bs).forEmpty().getOrFail(), 
        0, bs.length
    );
  }
  
  
}
