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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import us.pserver.job.json.adapter.ByteIterator;
import us.pserver.job.json.adapter.ByteIteratorException;
import us.pserver.job.json.adapter.JsonToken;
import us.pserver.job.json.adapter.JsonValue;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/03/2017
 */
public class ByteChannelIterator extends AbstractIterator {

  public static final int BUFFER_SIZE = 1024;


  private final ReadableByteChannel src;

  private final ByteBuffer buffer;

  private ByteBufferIterator iter;


  public ByteChannelIterator(ReadableByteChannel channel) {
    this(channel, 0);
  }


  public ByteChannelIterator(ReadableByteChannel channel, int bufSize) {
    super();
    if(channel == null) {
      throw new IllegalArgumentException("Bad Null/Empty ReadableByteChannel");
    }
    this.src = channel;
    this.buffer = ByteBuffer.allocate(
        (bufSize < 2 ? BUFFER_SIZE : bufSize)
    );
  }
  
  
  @Override
  public int getArrayLevel() {
    return fillBuffer().getArrayLevel();
  }


  @Override
  public int getObjectLevel() {
    return fillBuffer().getObjectLevel();
  }


  @Override
  public boolean isInsideArray() {
    return fillBuffer().isInsideArray();
  }


  @Override
  public byte getCurrentByte() {
    return fillBuffer().getCurrentByte();
  }


  @Override
  public String getCurrentField() {
    return fillBuffer().getCurrentField();
  }


  protected void unreadByte() {
    if(buffer.position() > 0) {
      buffer.position(buffer.position() -1);
    }
  }


  private ByteIterator fillBuffer() {
    if(iter == null || !buffer.hasRemaining()) {
      if(iter != null) {
        this.arraylvl = iter.arraylvl;
        this.objectlvl = iter.objectlvl;
        this.curfld = iter.curfld;
        this.current = iter.current;
      }
      try {
        buffer.clear();
        int read = src.read(buffer);
        buffer.flip();
        if(buffer.hasRemaining()) {
          iter = new ByteBufferIterator(buffer);
          iter.arraylvl = this.arraylvl;
          iter.objectlvl = this.objectlvl;
          iter.curfld = this.curfld;
          iter.current = this.current;
        }
      }
      catch(IOException e) {
        throw new ByteIteratorException(e.toString(), e);
      }
    }
    return iter;
  }


  @Override
  public String readField() throws ByteIteratorException {
    return this.fillBuffer().readField();
  }


  @Override
  public String readString() throws ByteIteratorException {
    return this.fillBuffer().readString();
  }


  @Override
  public boolean readBoolean() throws ByteIteratorException {
    return this.fillBuffer().readBoolean();
  }


  @Override
  public Number readNumber() throws ByteIteratorException {
    return this.fillBuffer().readNumber();
  }


  @Override
  public Object readNull() throws ByteIteratorException {
    return this.fillBuffer().readNull();
  }


  @Override
  public JsonValue readValue() {
    return this.fillBuffer().readValue();
  }


  @Override
  public void skip() {
    this.fillBuffer().skip();
  }


  @Override
  public boolean hasNext() {
    return this.fillBuffer().hasNext();
  }


  @Override
  public JsonToken next() {
    return this.fillBuffer().next();
  }

}
