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

package us.pserver.orb;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/02/2019
 */
public interface DataSource {

  public InputStream asInputStream() throws IOException;
  
  public default Reader asReader() throws IOException {
    return new BufferedReader(new InputStreamReader(asInputStream()));
  }
  
  public default ReadableByteChannel asByteChannel() throws IOException {
    return Channels.newChannel(asInputStream());
  }
  
  public default ByteBuffer readAsByteBuffer(boolean direct) throws IOException {
    ByteBuffer buf = direct ? ByteBuffer.allocateDirect(4096) : ByteBuffer.allocate(4096);
    ByteBuffer content = direct ? ByteBuffer.allocateDirect(4096) : ByteBuffer.allocate(4096);
    try (ReadableByteChannel ch = asByteChannel()) {
      while(ch.read(buf) != -1) {
        buf.flip();
        if(content.remaining() < buf.remaining()) {
          content.flip();
          int size = Math.round(content.remaining() * 1.25f + buf.remaining());
          ByteBuffer ncont = direct ? ByteBuffer.allocateDirect(size) : ByteBuffer.allocate(size);
          ncont.put(content);
          content = ncont;
        }
        content.put(buf);
        buf.compact();
      }
      content.flip();
      return content;
    }
  }
  
  public default String readAsString() throws IOException {
    ByteBuffer buf = readAsByteBuffer(false);
    return StandardCharsets.UTF_8.decode(buf).toString();
  }
  
  
  
  public static DataSource of(Path path) throws IOException {
    return () -> Files.newInputStream(path, StandardOpenOption.READ);
  }
  
  
  public static DataSource ofEnvironment(String name) {
    return () -> new ByteArrayInputStream(System.getenv(name).getBytes(StandardCharsets.UTF_8));
  }
  
}
