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

package us.pserver.job.json.adapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/02/2017
 */
public interface ByteIterator extends Iterator<ByteToken> {

  public byte currentByte();
  
  public String readString();
  
  public boolean readBoolean();
  
  public Number readNumber();
  
  public Object readNull();
  
  public String readField();
  
  public void skip();
  
  public boolean isInsideArray();
  
  
  public static ByteIterator of(ByteBuffer buf) {
    return new ByteBufferIterator(buf);
  }
  
  
  public static ByteIterator of(String str) {
    if(str == null || str.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad Null/Empty String");
    }
    return new ByteBufferIterator(ByteBuffer.wrap(
        UTF8String.from(str).getBytes())
    );
  }
  
  
  public static ByteIterator of(InputStream input) {
    return new InputStreamIterator(input);
  }
  
  
  public static ByteIterator of(InputStream input, int bufSize) {
    return new InputStreamIterator(input, bufSize);
  }
  
  
  
  
  
  public static class ByteBufferIterator implements ByteIterator {
    
    private final ByteBuffer src;
    
    private final int start;
    
    private byte current;
    
    private int inArray;
    
    
    public ByteBufferIterator(ByteBuffer buf) {
      if(buf == null || buf.remaining() < 1) {
        throw new IllegalArgumentException("Bad Null/Empty ByteBuffer");
      }
      this.src = buf;
      this.start = src.position();
      current = Byte.MIN_VALUE;
      inArray = 0;
    }
    
    
    public ByteBuffer getSource() {
      return src;
    }
    

    @Override
    public byte currentByte() {
      return current;
    }
    
    
    @Override
    public boolean isInsideArray() {
      return inArray > 0;
    }
    
    
    private void unreadByte() {
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
      return UTF8String.from(bos.toByteArray()).toString();
    }


    @Override
    public String readString() throws ByteIteratorException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      while((current = src.get()) != ByteValue.FIELD
          && current != ByteValue.END_ARRAY
          && current != ByteValue.END_OBJECT
          && src.hasRemaining()) {
        if(current == ByteValue.IGNORE 
            || current == ByteValue.STRING) continue;
        bos.write(current);
      }
      if(bos.size() < 1) throw new ByteIteratorException("Can not read String value");
      unreadByte();
      return UTF8String.from(bos.toByteArray()).toString();
    }


    @Override
    public boolean readBoolean() throws ByteIteratorException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ByteToken tk = ByteToken.of(current);
      if(tk == ByteToken.BOOLEAN) {
        bos.write(current);
      }
      while((current = src.get()) != ByteValue.FIELD
          && current != ByteValue.END_ARRAY
          && current != ByteValue.END_OBJECT
          && src.hasRemaining()) {
        if(current == ByteValue.IGNORE) continue;
        bos.write(current);
      }
      if(bos.size() < 1) throw new ByteIteratorException("Can not read boolean value");
      unreadByte();
      return Boolean.parseBoolean(UTF8String.from(bos.toByteArray()).toString());
    }


    @Override
    public Number readNumber() throws ByteIteratorException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ByteToken tk = ByteToken.of(current);
      if(tk == ByteToken.NUMBER) {
        bos.write(current);
      }
      while((current = src.get()) != ByteValue.FIELD
          && current != ByteValue.END_ARRAY
          && current != ByteValue.END_OBJECT
          && src.hasRemaining()) {
        if(current == ByteValue.IGNORE) continue;
        bos.write(current);
      }
      if(bos.size() < 1) throw new ByteIteratorException("Can not read number value");
      unreadByte();
      String str = UTF8String.from(bos.toByteArray()).toString();
      if(str.contains(".")) {
        return Double.valueOf(str);
      } else {
        return Long.valueOf(str);
      }
    }


    @Override
    public Object readNull() throws ByteIteratorException {
      String str = this.readString();
      if(str == null) throw new ByteIteratorException("Can not read boolean value");
      return null;
    }


    @Override
    public void skip() {
      this.readString();
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
          inArray++;
          break;
        case END_ARRAY:
          inArray--;
          break;
        case FIELD:
          if(inArray > 0) return next();
          break;
        case IGNORE:
          return next();
      }
      return tk;
    }
    
  }
  
  
  
  
  
  public static class InputStreamIterator implements ByteIterator {
    
    public static final int BUFFER_SIZE = 1024;
    
    
    private final InputStream src;
    
    private final ByteBuffer buf;
    
    private byte current;
    
    private int inArray;
    
    
    public InputStreamIterator(InputStream input) {
      this(input, 0);
    }
    
    
    public InputStreamIterator(InputStream input, int bufSize) {
      if(input == null) {
        throw new IllegalArgumentException("Bad Null/Empty InputStream");
      }
      this.src = input;
      this.buf = ByteBuffer.allocate((bufSize < 2 ? BUFFER_SIZE : bufSize));
      buf.flip();
      current = Byte.MIN_VALUE;
      inArray = 0;
    }
    
    
    public InputStream getSource() {
      return src;
    }
    

    @Override
    public byte currentByte() {
      return current;
    }
    
    
    @Override
    public boolean isInsideArray() {
      return inArray > 0;
    }
    
    
    private void unreadByte() {
      if(buf.position() > 0) {
        buf.position(buf.position() -1);
      }
    }
    
    
    private int get() {
      try {
        if(!buf.hasRemaining()) {
          buf.clear();
          int r = src.read(buf.array());
          if(r == 0) return get();
          else if(r == -1) return -1;
        }
        return buf.get() + 256;
      } 
      catch(IOException e) {
        throw new ByteIteratorException(e.toString(), e);
      }
    }


    @Override
    public String readField() throws ByteIteratorException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      int read;
      while((read = get()) != -1 
          && (current = (byte)read) != ByteValue.VALUE) {
        if(current == ByteValue.IGNORE 
            || current == ByteValue.STRING) continue;
        bos.write(current);
      }
      if(bos.size() < 1) throw new ByteIteratorException("Can not read field");
      unreadByte();
      return UTF8String.from(bos.toByteArray()).toString();
    }


    @Override
    public String readString() throws ByteIteratorException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ByteToken tk = ByteToken.of(current);
      if(tk == ByteToken.BOOLEAN || tk == ByteToken.NUMBER) {
        bos.write(current);
      }
      int read;
      while((read = get()) != -1 
          && (current = (byte)read) != ByteValue.FIELD
          && current != ByteValue.END_ARRAY
          && current != ByteValue.END_OBJECT) {
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
      String str = this.readString();
      if(str == null) throw new ByteIteratorException("Can not read boolean value");
      return null;
    }


    @Override
    public void skip() {
      this.readString();
    }


    @Override
    public boolean hasNext() {
      boolean b = get() != -1;
      unreadByte();
      return b;
    }


    @Override
    public ByteToken next() {
      int read = get();
      if(read == -1) return null;
      current = (byte)read;
      ByteToken tk = ByteToken.of(current);
      switch(tk) {
        case START_ARRAY:
          inArray++;
          break;
        case END_ARRAY:
          inArray--;
          break;
        case FIELD:
          if(inArray > 0) return next();
          break;
        case IGNORE:
          return next();
      }
      return tk;
    }
    
  }
  
}
