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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptUtils;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.streams.IO.os;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 01/08/2014
 */
public class StreamCoderFactory {

  private final CoderType[] types;
  
  private int index;
  
  private CryptKey key;
  
  private static StreamCoderFactory instance;
  
  
  private StreamCoderFactory() {
    types = new CoderType[3];
    index = 0;
  }
  
  
  public static StreamCoderFactory get() {
    if(instance == null)
      instance = getNew();
    return instance;
  }
  
  
  public static StreamCoderFactory getNew() {
    instance = new StreamCoderFactory();
    return instance;
  }
  
  
  private void checkIndex() {
    if(index >= types.length)
      throw new IllegalStateException("All coders already enabled");
  }
  
  
  public StreamCoderFactory clearCoders() {
    for(int i = 0; i < types.length; i++) {
      types[i] = null;
    }
    index = 0;
    return this;
  }
  
  
  public StreamCoderFactory setCryptCoderEnabled(boolean enabled, CryptKey k) {
    CoderType.CRYPT.setEnabled(enabled);
    if(enabled) {
      nullarg(CryptKey.class, k);
      checkIndex();
      key = k;
      if(!isCryptCoderEnabled())
        types[index++] = CoderType.CRYPT;
    }
    else {
      arrangeTypes(getCoderIndex(CoderType.CRYPT));
      index--;
    }
    return this;
  }
  
  
  public StreamCoderFactory setBase64CoderEnabled(boolean enabled) {
    CoderType.BASE64.setEnabled(enabled);
    if(enabled) {
      checkIndex();
      if(!isBase64CoderEnabled()) {
        types[index++] = CoderType.BASE64;
      }
    }
    else {
      arrangeTypes(getCoderIndex(CoderType.BASE64));
      index--;
    }
    return this;
  }
  
  
  public StreamCoderFactory setGZipCoderEnabled(boolean enabled) {
    CoderType.GZIP.setEnabled(enabled);
    if(enabled) {
      checkIndex();
      if(!isGZipCoderEnabled())
        types[index++] = CoderType.GZIP;
    }
    else {
      arrangeTypes(getCoderIndex(CoderType.GZIP));
      index--;
    }
    return this;
  }
  
  
  public boolean isCryptCoderEnabled() {
    return getCoderIndex(CoderType.CRYPT) != -1;
  }
  
  
  public boolean isBase64CoderEnabled() {
    return getCoderIndex(CoderType.BASE64) != -1;
  }
  
  
  public boolean isGZipCoderEnabled() {
    return getCoderIndex(CoderType.GZIP) != -1;
  }
  
  
  public boolean isAnyCoderEnabled() {
    boolean en = false;
    for(int i = 0; i < types.length; i++) {
      en = en || (types[i] != null 
          && types[i].isEnabled());
    }
    return en;
  }
  
  
  private void arrangeTypes(int ix) {
    if(ix >= 0 && ix < types.length -1) {
      for(int i = ix; i < types.length -1; i--) {
        types[ix] = types[ix+1];
        types[ix+1] = null;
      }
    }
    else if(ix == types.length -1) {
      types[ix] = null;
    }
  }
  
  
  public int getCoderIndex(CoderType tp) {
    nullarg(CoderType.class, tp);
    for(int i = 0; i < types.length; i++) {
      if(types[i] == tp)
        return i;
    }
    return -1;
  }
  
  
  public OutputStream create(OutputStream os) throws IOException {
    if(!isAnyCoderEnabled()) throw new IOException(
        "No coder configured");
    int min = Math.min(index, types.length -1);
    for(int i = min; i >= 0; i--) {
      if(types[i] != null) {
        os = create(os, types[i]);
      }
    }
    return os;
  }
  
  
  private OutputStream create(OutputStream os, CoderType tp) throws IOException {
    if(tp == null) return os;
    switch(tp) {
      case CRYPT:
        return CryptUtils.createCipherOutputStream(os, key);
      case BASE64:
        return new Base64OutputStream(os);
      case GZIP:
        return new GZIPOutputStream(os);
      default:
        throw new IOException("No such coder ["+ tp+ "]");
    }
  }
  
  
  public InputStream create(InputStream is) throws IOException {
    if(!isAnyCoderEnabled()) throw new IOException(
        "No coder configured");
    int min = Math.min(index, types.length -1);
    for(int i = min; i >= 0; i--) {
      if(types[i] != null) {
        is = create(is, types[i]);
      }
    }
    return is;
  }
  
  
  private InputStream create(InputStream is, CoderType tp) throws IOException {
    if(tp == null) return is;
    switch(tp) {
      case CRYPT:
        return CryptUtils.createCipherInputStream(is, key);
      case BASE64:
        return new Base64InputStream(is);
      case GZIP:
        return new GZIPInputStream(new UnsignedInputStream(is));
      default:
        throw new IOException("No such coder ["+ tp+ "]");
    }
  }
  
  
  @Override
  public String toString() {
    return "StreamCoderFactory{BASE64="+ isBase64CoderEnabled()+ ", GZIP="+ isGZipCoderEnabled()+ ", CRYPT="+ isCryptCoderEnabled()+ "}";
  }
  
}
