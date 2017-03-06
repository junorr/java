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

package us.pserver.job.json.iterator;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import us.pserver.job.json.adapter.ByteIteratorException;
import us.pserver.job.json.adapter.ByteToken;
import us.pserver.job.json.adapter.ByteValue;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/03/2017
 */
public class ByteBufferIterator extends AbstractIterator {

  private final ByteBuffer src;


  public ByteBufferIterator(ByteBuffer buf) {
    super();
    if(buf == null || buf.remaining() < 1) {
      throw new IllegalArgumentException("Bad Null/Empty ByteBuffer");
    }
    this.src = buf;
  }


  protected void unreadByte() {
    if(src.position() > 0) {
      src.position(src.position() -1);
    }
  }


  @Override
  public String readField() throws ByteIteratorException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while((current = src.get()) != ByteValue.VALUE
        && src.hasRemaining()) {
      if(current == ByteValue.IGNORE 
          || current == ByteValue.STRING) continue;
      bos.write(current);
    }
    if(bos.size() < 1) throw new ByteIteratorException("Can not read field");
    unreadByte();
    curfld = UTF8String.from(bos.toByteArray()).toString();
    return curfld;
  }


  @Override
  public String readString() throws ByteIteratorException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ByteToken tk = ByteToken.of(current);
    if(tk == ByteToken.NUMBER || tk == ByteToken.BOOLEAN) {
      bos.write(current);
    }
    while((current = src.get()) != ByteValue.FIELD
        && current != ByteValue.END_ARRAY
        && current != ByteValue.END_OBJECT
        && src.hasRemaining()) {
      if(current == ByteValue.IGNORE 
          || current == ByteValue.STRING) continue;
      bos.write(current);
    }
    if(bos.size() < 1) throw new ByteIteratorException("Can not read value");
    unreadByte();
    return UTF8String.from(bos.toByteArray()).toString();
  }


  @Override
  public boolean readBoolean() throws ByteIteratorException {
    return Boolean.parseBoolean(readString());
  }


  @Override
  public Number readNumber() throws ByteIteratorException {
    String str = readString();
    if(str.contains(".")) {
      return Double.valueOf(str);
    } else {
      return Long.valueOf(str);
    }
  }


  @Override
  public Object readNull() throws ByteIteratorException {
    readString();
    return null;
  }


  @Override
  public void skip() {
    readString();
  }


  @Override
  public boolean hasNext() {
    return src.hasRemaining();
  }


  @Override
  public ByteToken next() {
    current = src.get();
    ByteToken tk = ByteToken.of(current);
    switch(tk) {
      case START_ARRAY:
        arraylvl++;
        break;
      case START_OBJECT:
        objectlvl++;
        break;
      case END_ARRAY:
        arraylvl--;
        break;
      case END_OBJECT:
        objectlvl--;
        break;
      case FIELD:
        if(isInsideArray()) return next();
        break;
      case IGNORE:
        return next();
    }
    return tk;
  }

}
