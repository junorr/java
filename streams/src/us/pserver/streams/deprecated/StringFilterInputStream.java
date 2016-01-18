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

package us.pserver.streams.deprecated;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 19/06/2015
 */
public class StringFilterInputStream extends FilterInputStream {
  
  
  public static final String DEFAULT_CHARSET = "UTF-8";
  
  
  private String charset;
  
  private InputStream source;
  
  private List<FilterAction> actions;
  
  
  public StringFilterInputStream(InputStream src) {
    super(src);
    if(src == null)
      throw new IllegalArgumentException("Invalid null InputStream");
    source = src;
    charset = DEFAULT_CHARSET;
    actions = Collections.synchronizedList(new ArrayList<FilterAction>());
  }
  
  
  public StringFilterInputStream(InputStream src, String charset) {
    super(src);
    if(src == null)
      throw new IllegalArgumentException("Invalid null InputStream");
    if(charset == null) charset = DEFAULT_CHARSET;
    source = src;
    this.charset = charset;
    actions = Collections.synchronizedList(new ArrayList<FilterAction>());
  }
  
  
  public List<FilterAction> getActionList() {
    return actions;
  }
  
  
  public StringFilterInputStream clearActions() {
    actions.clear();
    return this;
  }
  
  
  public StringFilterInputStream addAction(FilterAction action) {
    if(action != null) {
      actions.add(action);
    }
    return this;
  }
  
  
  public StringFilterInputStream addAction(String search, Consumer<FilterAction> action) {
    if(search != null && action != null) {
      actions.add(new FilterAction(search, action, this));
    }
    return this;
  }


  public StringFilterInputStream addAction(String search, boolean ignoreCase, Consumer<FilterAction> action) {
    if(search != null && action != null) {
      actions.add(new FilterAction(search, action, this).setIgnoreCase(ignoreCase));
    }
    return this;
  }


  public String getCharset() {
    return charset;
  }


  public void setCharset(String charset) {
    this.charset = charset;
  }


  public InputStream getSource() {
    return source;
  }


  public void setSource(InputStream source) {
    this.source = source;
  }
  
  
  @Override
  public int read() throws IOException {
    byte[] bs = new byte[1];
    int r = this.read(bs);
    if(r > 0) return bs[0];
    return r;
  }
  
  
  @Override
  public int read(byte[] bs, int off, int len) throws IOException {
    if(bs == null)
      throw new IllegalArgumentException("Invalid null byte array");
    if(bs.length == 0 || len < 1) return -1;
    if(off < 0 || off + len > bs.length)
      throw new IllegalArgumentException("Invalid arguments: off="+ off+ ", len="+ len);
    
    int read = super.read(bs, off, len);
    if(read < 1) return read;
    actions.forEach(f->f.doFilter(bs, off, read));
    return read;
  }
  
  
  @Override
  public int read(byte[] bs) throws IOException {
    return read(bs, 0, (bs != null ? bs.length : 0));
  }
  
  
  
  
  public static class FilterAction {
    
    private String match;
    
    private LimitedBuffer buffer;
    
    private Consumer<FilterAction> action;
    
    private StringFilterInputStream stream;
    
    private boolean ignoreCase;
    
    
    public FilterAction(String str, Consumer<FilterAction> cs, StringFilterInputStream in) {
      if(str == null)
        throw new IllegalArgumentException("Invalid null string to match");
      if(cs == null)
        throw new IllegalArgumentException("Invalid null action Consumer");
      if(in == null)
        throw new IllegalArgumentException("Invalid null StringFilterInputStream");
      match = str;
      action = cs;
      stream = in;
      buffer = new LimitedBuffer(match.length());
      ignoreCase = false;
    }


    public String getMatch() {
      return match;
    }


    public LimitedBuffer getBuffer() {
      return buffer;
    }


    public Consumer<FilterAction> getAction() {
      return action;
    }


    public StringFilterInputStream getInputStream() {
      return stream;
    }
    
    
    public boolean isIgnoreCase() {
      return ignoreCase;
    }


    public FilterAction setIgnoreCase(boolean ignoreCase) {
      this.ignoreCase = ignoreCase;
      return this;
    }
    
    
    public void doFilter(int b) {
      buffer.put(b);
      checkForAction();
    }
    
    
    public void doFilter(byte[] bs, int off, int len) {
      if(bs == null)
        throw new IllegalArgumentException("Invalid null byte array");
      if(bs.length == 0 || len < 1) return;
      if(off < 0 || off + len > bs.length)
        throw new IllegalArgumentException("Invalid arguments: off="+ off+ ", len="+ len);
      for(int i = off; i < (off+len); i++) {
        doFilter(bs[i]);
      }
    }
    
    
    public void doFilter(byte[] bs) {
      doFilter(bs, 0, (bs != null ? bs.length : 0));
    }
    
    
    private void checkForAction() {
      if(buffer.size() == match.length()) {
        String str = buffer.toString(stream.getCharset());
        boolean equals = (ignoreCase 
            ? match.equalsIgnoreCase(str) : match.equals(str));
        if(equals)
          action.accept(this);
      }
    }
    
    
  }

  
}
