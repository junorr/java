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
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;
import static us.pserver.http.HttpInputStream.HD_CONTENT_TYPE_OCTETSTREAM;
import static us.pserver.http.HttpInputStream.NAME_INPUTSTREAM;
import static us.pserver.http.HttpInputStream.STATIC_SIZE;
import us.pserver.streams.IO;
import us.pserver.streams.NullOutput;
import us.pserver.streams.ProtectedOutputStream;
import us.pserver.streams.StreamCoderFactory;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/07/2014
 */
public class HttpLiveStream extends HeaderEncryptable {

  private final BufferedInputStream input;
  
  private final StreamCoderFactory fact;
  
  private long streamsize;
  
  
  public HttpLiveStream(InputStream is) {
    nullarg(InputStream.class, is);
    input = IO.bf(is);
    fact = StreamCoderFactory.getNew();
    try {
      streamsize = IO.sz(input);
    } catch(IOException e) {
      throw new IllegalStateException(e.toString(), e);
    }
  }
  
  
  public boolean isCryptStreamEnabled() {
    return fact.isCryptCoderEnabled();
  }
  
  
  public boolean isGZipStreamEnabled() {
    return fact.isGZipCoderEnabled();
  }
  
  
  public boolean isBase64StreamEnabled() {
    return fact.isBase64CoderEnabled();
  }
  
  
  public HttpLiveStream setCryptStreamEnabled(boolean enabled, CryptKey key) {
    if(enabled) nullarg(CryptKey.class, key);
    super.setCryptKey(key);
    fact.setCryptCoderEnabled(enabled, key);
    return this;
  }
  
  
  public HttpLiveStream setBase64StreamEnabled(boolean enabled) {
    fact.setBase64CoderEnabled(enabled);
    return this;
  }
  
  
  public HttpLiveStream setGZipStreamEnabled(boolean enabled) {
    fact.setGZipCoderEnabled(enabled);
    return this;
  }
  
  
  @Override
  public HttpLiveStream setCryptKey(CryptKey k) {
    return setCryptStreamEnabled((k != null), k);
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
    
    OutputStream output = new ProtectedOutputStream(out);
    if(fact.isAnyCoderEnabled()) {
      output = fact.create(output);
    }
    
    StringByteConverter cv = new StringByteConverter();
    out.write(cv.convert(start.toString()));
    out.write(cv.convert(BOUNDARY_XML_START));
    out.write(cv.convert(BOUNDARY_CONTENT_START));
    out.flush();
    
    StreamUtils.transfer(input, output);
    output.close();
    
    out.write(cv.convert(BOUNDARY_CONTENT_END));
    out.write(cv.convert(BOUNDARY_XML_END));
    out.flush();
  }
  
  
  public static void main(String[] args) throws IOException {
    CryptKey key = CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_PKCS5);
    HttpLiveStream hl = new HttpLiveStream(
        IO.is(IO.p("c:/.local/file.txt")));
    hl.setGZipStreamEnabled(true)
        .setCryptStreamEnabled(true, key)
        .setBase64StreamEnabled(true);
    HttpBuilder hb = HttpBuilder.requestBuilder(new RequestLine(Method.POST, "172.24.77.60"));
    hb.put(hl);
    hb.writeContent(NullOutput.pout);
  }
  
}
