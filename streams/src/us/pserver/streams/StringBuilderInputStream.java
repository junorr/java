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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/06/2015
 */
public class StringBuilderInputStream extends InputStream {
  
  private String UTF8_CHARSET = "UTF-8"; 

  private StringBuilder builder;
  
  private ByteArrayInputStream buffer;
  
  private int mark;
  
  private int readLimit;
  
  private int count;
  
  private int index;
  
  private boolean closed;
  
  private Charset charset;
  
  
  public StringBuilderInputStream() {
    clear();
    charset = Charset.forName(UTF8_CHARSET);
  }
  
  
  public StringBuilderInputStream(String value) {
    this();
    if(value != null)
      builder.append(value);
  }
  
  
  public StringBuilderInputStream(String value, Charset cs) {
    this(value);
    if(cs != null)
      charset = cs;
  }
  
  
  public StringBuilderInputStream(Charset cs) {
    clear();
    if(cs != null)
      charset = cs;
  }
  
  
  public Charset getCharset() {
    return charset;
  }
  
  
  public StringBuilderInputStream setCharset(Charset cs) {
    if(cs != null)
      charset = cs;
    return this;
  }
  
  
  public StringBuilderInputStream setCharset(String scs) {
    if(scs != null)
      charset = Charset.forName(scs);
    return this;
  }
  
  
  public StringBuilderInputStream append(String str) {
    if(str != null) builder.append(str);
    return this;
  }
  
  
  public StringBuilderInputStream append(char c) {
    builder.append(c);
    return this;
  }
  
  
  public StringBuilder builder() {
    return builder;
  }
  
  
  public StringBuilder getStringBuilder() {
    return builder;
  }
  
  
  public StringBuilderInputStream setStringBuilder(StringBuilder bld) {
    if(bld != null) {
      builder = bld;
    }
    return this;
  }
  
  
  public StringBuilderInputStream rewind() {
    index = 0;
    closed = false;
    return this;
  }
  
  
  @Override
  public void mark(int readLimit) {
    if(readLimit > 0) {
      this.readLimit = readLimit;
      index = mark;
    }
  }
  
  
  @Override
  public void reset() {
    index = mark;
  }
  
  
  public StringBuilderInputStream clear() {
    builder = new StringBuilder();
    index = mark = 0;
    count = 0;
    readLimit = -1;
    closed = false;
    return this;
  }
  
  
  private int incIndex() {
    count += (readLimit > 0 ? 1 : 0);
    return ++index;
  }


  @Override
  public int read() throws IOException {
    if(closed) return -1;
    if(buffer == null || buffer.available() < 1) {
      if(index >= builder.length()) 
        return -1;
      else 
        buffer = new ByteArrayInputStream(
            builder.substring(index, ++index).getBytes(charset));
    }
    return buffer.read();
  }
  
}
