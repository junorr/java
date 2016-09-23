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

package br.com.bb.disec.micros.handler;

import br.com.bb.disec.micros.coder.Encoder;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/09/2016
 */
public class FileStreamHandler implements HttpHandler {

  private final Path path;
  
  
  public FileStreamHandler(Path path) {
    if(path == null) {
      throw new IllegalArgumentException("Bad Null Path");
    }
    if(!Files.exists(path)) {
      throw new IllegalArgumentException("Path Does Not Exists");
    }
    this.path = path;
  }
  
  
  public Path getPath() {
    return path;
  }
  
  
  private void setHeaders(HttpServerExchange hse) throws IOException {
    hse.getResponseHeaders().add(
        Headers.CONTENT_DISPOSITION, 
        "attachment; filename=\""
            + path.getFileName()+ "\""
    );
    hse.setResponseContentLength(Files.size(path));
  }


  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    this.setHeaders(hse);
    hse.startBlocking();
    this.write(hse.getOutputStream());
    hse.endExchange();
  }
  
  
  public void write(OutputStream out) throws Exception {
    ByteBuffer buf = ByteBuffer.allocateDirect(4096);
    try (
      WritableByteChannel och = Channels.newChannel(out);
      ReadableByteChannel ich = Files.newByteChannel(path, StandardOpenOption.READ);
    ) {
      while(ich.read(buf) > 0) {
        buf.flip();
        och.write(buf);
        buf.clear();
      }
    }
    out.flush();
    out.close();
  }
  
}
