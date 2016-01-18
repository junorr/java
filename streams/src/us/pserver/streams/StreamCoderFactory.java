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

import us.pserver.streams.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptUtils;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 01/08/2014
 */
public class StreamCoderFactory {
	
	protected static final int GZIP = 1;

	protected static final int CRYPT = 2;

	protected static final int BASE64 = 4;
	

  private int coder;
  
  private CryptKey key;
  
  
  public StreamCoderFactory() {
    coder = 0;
  }
  
  
  public StreamCoderFactory clearCoders() {
    coder = 0;
    return this;
  }
  
  
  public StreamCoderFactory setCryptCoderEnabled(boolean enabled, CryptKey k) {
		if(enabled && !isCryptCoderEnabled()) {
			if(k == null) {
				throw new IllegalArgumentException("Invalid CryptKey: "+ k);
			}
			coder += CRYPT;
			this.key = k;
		}
		else if(!enabled && isCryptCoderEnabled()) {
			coder -= CRYPT;
			this.key = null;
		}
		return this;
  }
  
  
  public StreamCoderFactory setBase64CoderEnabled(boolean enabled) {
		if(enabled && !isBase64CoderEnabled()) {
			coder += BASE64;
		}
		else if(!enabled && isBase64CoderEnabled()) {
			coder -= BASE64;
		}
		return this;
  }
  
  
  public StreamCoderFactory setGZipCoderEnabled(boolean enabled) {
		if(enabled && !isGZipCoderEnabled()) {
			coder += GZIP;
		}
		else if(!enabled && isGZipCoderEnabled()) {
			coder -= GZIP;
		}
		return this;
  }
  
  
  public boolean isCryptCoderEnabled() {
    return coder == 2 || coder == 3 || coder == 6 || coder == 7;
  }
  
  
  public boolean isBase64CoderEnabled() {
    return coder == 4 || coder == 5 || coder == 6 || coder == 7;
  }
  
  
  public boolean isGZipCoderEnabled() {
    return coder == 1 || coder == 3 || coder == 5 || coder == 7;
  }
  
  
  public boolean isAnyCoderEnabled() {
    return coder > 0;
  }
  
  
  public OutputStream create(OutputStream os) throws IOException {
    if(!isAnyCoderEnabled()) throw new IOException(
        "No coder configured");
		if(isCryptCoderEnabled()) {
			os = CryptUtils.createCipherOutputStream(os, key);
		}
		if(isGZipCoderEnabled()) {
			os = new GZIPOutputStream(new BufferedOutputStream(os));
		}
		if(isBase64CoderEnabled()) {
			os = new Base64OutputStream(os);
		}
    return os;
  }
  
  
  public InputStream create(InputStream is) throws IOException {
    if(!isAnyCoderEnabled()) throw new IOException(
        "No coder configured");
		if(isCryptCoderEnabled()) {
			is = CryptUtils.createCipherInputStream(is, key);
		}
		if(isGZipCoderEnabled()) {
			is = new GZIPInputStream(new BufferedInputStream(is));
		}
		if(isBase64CoderEnabled()) {
			is = new Base64InputStream(is);
		}
    return is;
  }
  
  
  @Override
  public String toString() {
    return "StreamCoderFactory{BASE64="+ isBase64CoderEnabled()+ ", GZIP="+ isGZipCoderEnabled()+ ", CRYPT="+ isCryptCoderEnabled()+ "}";
  }
  
}
