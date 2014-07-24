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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.CipherOutputStream;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptUtils;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.http.HttpInputStream.HD_CONTENT_TYPE_OCTETSTREAM;
import static us.pserver.http.HttpInputStream.NAME_INPUTSTREAM;
import static us.pserver.http.HttpInputStream.STATIC_SIZE;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/07/2014
 */
public class HttpLiveStream extends HeaderEncryptable {

  private BufferedInputStream input;
  
  private CipherOutputStream secout;
  
  private boolean ensec;
  
  private long streamsize;
  
  
  public HttpLiveStream(InputStream is) {
    nullarg(InputStream.class, is);
    input = new BufferedInputStream(is);
    ensec = false;
    try {
      streamsize = input.available();
    } catch(IOException e) {
      throw new IllegalStateException(e.toString(), e);
    }
  }
  
  
  public boolean isCryptStreamEnabled() {
    return ensec;
  }
  
  
  public HttpLiveStream setCryptStreamEnabled(boolean enabled, CryptKey key) {
    if(enabled) nullarg(CryptKey.class, key);
    ensec = enabled;
    setCryptKey(key);
    return this;
  }
  
  
  @Override
  public HttpLiveStream setCryptKey(CryptKey k) {
    return setCryptStreamEnabled(k != null, k);
  }
  
  
  @Override
  public long getLength() {
    if(input == null) return 0;
    return STATIC_SIZE + streamsize;
  }
  
  
  @Override
  public void writeContent(OutputStream out) throws IOException {
    nullarg(OutputStream.class, out);
    nullarg(InputStream.class, input);
    StringBuffer start = new StringBuffer();
    start.append(CRLF).append(HYFENS).append(BOUNDARY)
        .append(CRLF).append(HD_CONTENT_DISPOSITION)
        .append(": ").append(VALUE_DISPOSITION_FORM_DATA)
        .append("; ").append(NAME_INPUTSTREAM)
        .append(CRLF).append(HD_CONTENT_TYPE_OCTETSTREAM)
        .append(CRLF);
    
    OutputStream output = out;
    if(ensec && secout == null) {
      secout = CryptUtils.createCipherOutputStream(
          out, getCryptKey());
      output = secout;
    }
    
    StringByteConverter cv = new StringByteConverter();
    out.write(cv.convert(start.toString()));
    out.write(cv.convert(BOUNDARY_XML_START));
    out.write(cv.convert(BOUNDARY_CONTENT_START));
    StreamUtils.transfer(input, output);
    out.write(cv.convert(BOUNDARY_CONTENT_END));
    out.write(cv.convert(BOUNDARY_XML_END));
    out.flush();
  }
  
  
}
